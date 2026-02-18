package ru.gareev.documentservice.service;

import ru.gareev.documentservice.entity.ApprovalRegistryItem;

public interface ApprovalRegistryService {
    ApprovalRegistryItem createApproved(String author, Long id);
}
