package ru.gareev.documentservice.service.approval_registry;

import ru.gareev.documentservice.entity.ApprovalRegistryItem;

public interface ApprovalRegistryService {
    ApprovalRegistryItem createApproved(String author, Long id);
}
