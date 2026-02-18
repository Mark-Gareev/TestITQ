package ru.gareev.documentservice.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gareev.documentservice.api.dto.ApprovalRegistryItemDto;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/registry")
public class ApprovalRegistryController {
    @GetMapping("/get")
    public List<ApprovalRegistryItemDto> getAll() {
        //TODO add logic
        return Collections.singletonList(ApprovalRegistryItemDto.builder().build());
    }
}
