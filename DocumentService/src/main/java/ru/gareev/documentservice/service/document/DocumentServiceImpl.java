package ru.gareev.documentservice.service.document;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.gareev.documentservice.api.dto.request.DocumentCreationRequest;
import ru.gareev.documentservice.api.dto.response.DocumentDto;
import ru.gareev.documentservice.api.dto.response.DocumentListItemDto;
import ru.gareev.documentservice.api.dto.response.FullDocumentDto;
import ru.gareev.documentservice.entity.Document;
import ru.gareev.documentservice.entity.DocumentStatus;
import ru.gareev.documentservice.exceptions.DocumentNotFoundException;
import ru.gareev.documentservice.exceptions.UnsupportedStatusMove;
import ru.gareev.documentservice.repository.DocumentRepository;
import ru.gareev.documentservice.service.activity_feed.ActivityFeedService;
import ru.gareev.documentservice.service.approval_registry.ApprovalRegistryService;
import ru.gareev.documentservice.service.mapper.DocumentDtoMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository repository;
    private final DocumentDtoMapper documentDtoMapper;
    private final ApprovalRegistryService registryService;
    private final ActivityFeedService activityFeedService;

    @Override
    public DocumentDto createDocument(DocumentCreationRequest request) {
        Document document = new Document();
        document.setName(request.getName());
        document.setCreatedBy(request.getAuthor());
        document.setStatus(DocumentStatus.DRAFT);
        document.setCreationDateTime(LocalDateTime.now());
        document.setUpdateDateTime(LocalDateTime.now());
        document = repository.save(document);
        log.info("document with id {} successfully created",document.getId());
        return documentDtoMapper.toDto(document);
    }

    /**
     * Document has optimistic version lock.
     * There is no retry logic, because in our demo app no operations performed except status changing.
     * So, if we have optimistic lock ex, it means smb already change status here and we can do nothing.
     */
    @Transactional(Transactional.TxType.REQUIRED)
    @Override
    public Document changeDocumentStatus(String author, Long id, DocumentStatus targetStatus) {
        Document document = repository.findDocumentById(id);
        if (document == null) {
            log.info("not found document with id {}", id);
            throw new NoSuchElementException();
        }
        if (!validateStatus(document.getStatus(), targetStatus)) {
            log.info("wrong status moving for document with id {}", id);
            throw new UnsupportedStatusMove();
        }
        try {
            document.setStatus(targetStatus);
            document.setUpdateDateTime(LocalDateTime.now());
            document = repository.save(document);
            log.info("status successfully changed to {} for document with id {}", targetStatus, id);
            if (targetStatus == DocumentStatus.APPROVED) {
                try {
                    registryService.createApproved(author, document.getId());
                } catch (Exception e) {
                    //should be other exception to other answer, but in demo app it is no difference
                    //between registry fail and incorrect status move
                    log.info("error while creating registry item for document with id {}", id);
                    throw new UnsupportedStatusMove();
                }

            }
        } catch (OptimisticLockException e) {
            // swap exceptions to add correct results in documentBatchService
            log.info("optimistic lock ex for document with id {}", id);
            throw new UnsupportedStatusMove();
        }
        activityFeedService.createActivityItem(document, document.getStatus(), author);
        log.info("activity item saved for document with id {}",id);

        return document;
    }

    @Override
    public DocumentDto getDocument(Long id) {
        try {
            Document document = repository.findDocumentById(id);
            return documentDtoMapper.toDto(document);
        } catch (EntityNotFoundException e) {
            throw new DocumentNotFoundException("Document with id " + id + "not found", e);
        }
    }

    @Override
    public List<DocumentListItemDto> getDocuments(List<Long> ids) {
        List<Document> documents = repository.findAllById(ids);
        return documents.stream()
                .map(documentDtoMapper::toListDto)
                .toList();
    }

    @Override
    public FullDocumentDto getFullDocument(Long id) {
        try {
            Document document = repository.findFullDocumentById(id);
            return documentDtoMapper.toFullDocumentDto(document);
        } catch (EntityNotFoundException e) {
            throw new DocumentNotFoundException("Document with id " + id + "not found", e);
        }
    }

    private boolean validateStatus(DocumentStatus previous, DocumentStatus target) {
        switch (target) {
            case SUBMITTED -> {
                return previous == DocumentStatus.DRAFT;
            }
            case APPROVED -> {
                return previous == DocumentStatus.SUBMITTED;
            }
            default -> {
                return false;
            }
        }
    }
}
