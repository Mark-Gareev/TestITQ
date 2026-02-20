package ru.gareev.documentservice.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gareev.documentservice.api.dto.response.ApprovalRegistryItemDto;
import ru.gareev.documentservice.service.approval_registry.ApprovalRegistryService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/registry")
public class ApprovalRegistryController {
    private final ApprovalRegistryService service;
    @GetMapping("/get/{docId}")
    public List<ApprovalRegistryItemDto> getAll(@PathVariable Long docId) {
        return service.getItemsByDocumentId(docId);
    }
}
