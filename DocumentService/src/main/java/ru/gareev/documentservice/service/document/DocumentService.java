package ru.gareev.documentservice.service.document;

import ru.gareev.documentservice.api.dto.request.DocumentCreationRequest;
import ru.gareev.documentservice.api.dto.response.DocumentDto;
import ru.gareev.documentservice.api.dto.response.DocumentListItemDto;
import ru.gareev.documentservice.api.dto.response.FullDocumentDto;
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
