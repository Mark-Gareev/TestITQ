package ru.gareev.documentservice.service;

import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gareev.documentservice.api.dto.DocumentCreationRequest;
import ru.gareev.documentservice.api.dto.DocumentDto;
import ru.gareev.documentservice.api.dto.DocumentListItemDto;
import ru.gareev.documentservice.api.dto.FullDocumentDto;
import ru.gareev.documentservice.entity.Document;
import ru.gareev.documentservice.entity.DocumentStatus;
import ru.gareev.documentservice.exceptions.UnsupportedStatusMove;
import ru.gareev.documentservice.repository.DocumentRepository;
import ru.gareev.documentservice.service.mapper.DocumentDtoMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

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
        return documentDtoMapper.toDto(document);
    }

    /**
     * Document has optimistic version lock.
     * There is no retry logic, because in our demo app no operations performed except status changing.
     * So, if we have optimistic lock ex, it means smb already change status here and we can do nothing.
     */
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Override
    public Document changeDocumentStatus(String author, Long id, DocumentStatus targetStatus) {
        Document document = repository.findDocumentById(id);
        if (document == null) {
            throw new NoSuchElementException();
        }
        if (!validateStatus(document.getStatus(), targetStatus)) {
            throw new UnsupportedStatusMove();
        }
        try {
            document.setStatus(targetStatus);
            document.setUpdateDateTime(LocalDateTime.now());
            document = repository.save(document);
            if (targetStatus == DocumentStatus.APPROVED) {
                try {
                    registryService.createApproved(author, document.getId());
                } catch (Exception e) {
                    //should be other exception to other answer, but in demo app it is no difference
                    //between registry fail and incorrect status move
                    throw new UnsupportedStatusMove();
                }

            }
        } catch (OptimisticLockException e) {
            // swap exceptions to add correct results in documentBatchService
            throw new UnsupportedStatusMove();
        }
        activityFeedService.createActivityItem(document, document.getStatus(), author);
        //TODO add logs when Loki
        return document;
    }

    @Override
    public DocumentDto getDocument(Long id) {
        return documentDtoMapper.toDto(repository.findDocumentById(id));
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
        return documentDtoMapper.toFullDocumentDto(repository.findFullDocumentById(id));
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
