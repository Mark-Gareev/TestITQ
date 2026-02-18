package ru.gareev.documentservice.service;

import ru.gareev.documentservice.api.dto.DocumentCreationRequest;
import ru.gareev.documentservice.api.dto.DocumentDto;
import ru.gareev.documentservice.api.dto.DocumentListItemDto;
import ru.gareev.documentservice.api.dto.FullDocumentDto;
import ru.gareev.documentservice.entity.Document;
import ru.gareev.documentservice.entity.DocumentStatus;

import java.util.List;

public interface DocumentService {
    DocumentDto createDocument(DocumentCreationRequest request);

    Document changeDocumentStatus(String author, Long id, DocumentStatus targetStatus);

    DocumentDto getDocument(Long id);

    List<DocumentListItemDto> getDocuments(List<Long> ids);

    FullDocumentDto getFullDocument(Long id);
}
