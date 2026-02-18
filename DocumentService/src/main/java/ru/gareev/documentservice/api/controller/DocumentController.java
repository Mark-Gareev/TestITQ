package ru.gareev.documentservice.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.gareev.documentservice.api.dto.*;
import ru.gareev.documentservice.service.DocumentBatchService;
import ru.gareev.documentservice.service.DocumentPageService;
import ru.gareev.documentservice.service.DocumentService;

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

    @GetMapping("/list")
    public Page<DocumentListItemDto> getDocumentList(
            @RequestParam(required = false) String createdBy,
            @RequestParam(required = false) LocalDateTime createdDateFrom,
            @RequestParam(required = false) LocalDateTime createdDateTo,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam List<String> sortBy
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
    public DocumentDto createDocument(@RequestBody DocumentCreationRequest request) {
        return documentService.createDocument(request);
    }

    @PutMapping("/submit")
    public List<StatusMovingResultItem> submitDocuments(@RequestBody StatusChangeRequest request) {
        return documentBatchService.batchSubmit(request);
    }

    @PutMapping("/approve")
    public List<StatusMovingResultItem> approveDocuments(@RequestBody StatusChangeRequest request) {
        return documentBatchService.batchApprove(request);
    }
}
