package ru.gareev.documentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.gareev.documentservice.api.dto.DocumentListItemDto;
import ru.gareev.documentservice.entity.Document;
import ru.gareev.documentservice.entity.DocumentStatus;
import ru.gareev.documentservice.repository.DocumentRepository;
import ru.gareev.documentservice.service.mapper.DocumentDtoMapper;

import java.time.LocalDateTime;
import java.util.List;

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
        Specification<Document> specification = Specification.allOf(
                DocumentSpecifications.createdByEquals(createdBy),
                DocumentSpecifications.createdDateFrom(createdDateFrom),
                DocumentSpecifications.createdDateTo(createdDateTo),
                DocumentSpecifications.statusEquals(DocumentStatus.forString(status))
        );
        Sort sorting = Sort.unsorted();
        if (!sortBy.isEmpty()) {
            for (String sortParam : sortBy) {
                String[] parts = sortParam.split(",");
                String field = parts[0];
                Sort.Direction direction = parts.length > 1 && parts[1].equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC : Sort.Direction.ASC;
                sorting = sorting.and(Sort.by(direction, field));
            }
        }
        Pageable pageable = PageRequest.of(page, size, sorting);
        Page<Document> docPage = repository.findAll(specification, pageable);
        return docPage.map(mapper::toListDto);
    }
}
