package ru.gareev.documentservice.service.document_batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.gareev.documentservice.api.dto.response.DocumentListItemDto;
import ru.gareev.documentservice.entity.Document;
import ru.gareev.documentservice.entity.DocumentStatus;
import ru.gareev.documentservice.repository.DocumentRepository;
import ru.gareev.documentservice.service.document.DocumentPageService;
import ru.gareev.documentservice.service.document.DocumentSpecifications;
import ru.gareev.documentservice.service.mapper.DocumentDtoMapper;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DocumentPageServiceImpl implements DocumentPageService {
    private final DocumentRepository repository;
    private final DocumentDtoMapper mapper;

    @Override
    public Page<DocumentListItemDto> getPage(
            String createdBy,
            LocalDateTime createdDateFrom,
            LocalDateTime createdDateTo,
            String status,
            int page,
            int size,
            List<String> sortBy) {
        log.info("received page query");
        long start = System.currentTimeMillis();
        Specification<Document> specification = Specification.allOf(
                DocumentSpecifications.createdByEquals(createdBy),
                DocumentSpecifications.createdDateFrom(createdDateFrom),
                DocumentSpecifications.createdDateTo(createdDateTo),
                DocumentSpecifications.statusEquals(DocumentStatus.forString(status))
        );
        Sort sorting = Sort.unsorted();
        if (sortBy != null && !sortBy.isEmpty()) {
            for (String sortParam : sortBy) {
                String[] parts = sortParam.split("_");
                String field = parts[0];
                Sort.Direction direction = parts.length > 1 && parts[1].equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC : Sort.Direction.ASC;
                sorting = sorting.and(Sort.by(direction, field));
            }
        }
        Pageable pageable = PageRequest.of(page, size, sorting);
        Page<Document> docPage = repository.findAll(specification, pageable);
        long end = System.currentTimeMillis();
        log.info("page query proceed for {}ms", end - start);
        return docPage.map(mapper::toListDto);
    }
}
