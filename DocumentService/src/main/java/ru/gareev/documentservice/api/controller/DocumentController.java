package ru.gareev.documentservice.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.gareev.documentservice.api.dto.request.DocumentCreationRequest;
import ru.gareev.documentservice.api.dto.request.StatusChangeRequest;
import ru.gareev.documentservice.api.dto.response.DocumentDto;
import ru.gareev.documentservice.api.dto.response.DocumentListItemDto;
import ru.gareev.documentservice.api.dto.response.FullDocumentDto;
import ru.gareev.documentservice.api.dto.response.StatusMovingResultItem;
import ru.gareev.documentservice.service.document.DocumentPageService;
import ru.gareev.documentservice.service.document.DocumentService;
import ru.gareev.documentservice.service.document_batch.DocumentBatchService;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/documents")
public class DocumentController {
    private final DocumentService documentService;
    private final DocumentBatchService documentBatchService;
    private final DocumentPageService documentPageService;

    @GetMapping("/{id}")
    public DocumentDto getDocumentById(@PathVariable Long id) {
        return documentService.getDocument(id);
    }
    @GetMapping("/full/{id}")
    public FullDocumentDto getFullDocumentById(@PathVariable Long id){
        return documentService.getFullDocument(id);
    }

    @GetMapping("/batch")
    public List<DocumentListItemDto> getAllById(@RequestParam List<Long> ids) {
        return documentService.getDocuments(ids);
    }

    @GetMapping("/search")
    public Page<DocumentListItemDto> getDocumentList(
            @RequestParam(required = false) String createdBy,
            @RequestParam(required = false) LocalDateTime createdDateFrom,
            @RequestParam(required = false) LocalDateTime createdDateTo,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) List<String> sortBy
    ) {
        return documentPageService.getPage(
                createdBy,
                createdDateFrom,
                createdDateTo,
                status,
                page,
                size,
                sortBy
        );
    }

    @PostMapping("/create")
    public DocumentDto createDocument(@Valid @RequestBody DocumentCreationRequest request) {
        return documentService.createDocument(request);
    }

    @PutMapping("/submit")
    public List<StatusMovingResultItem> submitDocuments(@Valid @RequestBody StatusChangeRequest request) {
        return documentBatchService.batchSubmit(request);
    }

    @PutMapping("/approve")
    public List<StatusMovingResultItem> approveDocuments(@Valid @RequestBody StatusChangeRequest request) {
        return documentBatchService.batchApprove(request);
    }
}
