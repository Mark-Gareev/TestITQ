package ru.gareev.documentservice.service.approval_registry;

import ru.gareev.documentservice.api.dto.response.ApprovalRegistryItemDto;
import ru.gareev.documentservice.entity.ApprovalRegistryItem;

import java.util.List;

public interface ApprovalRegistryService {
    ApprovalRegistryItem createApproved(String author, Long id);

    List<ApprovalRegistryItemDto> getItemsByDocumentId(Long docId);
}
