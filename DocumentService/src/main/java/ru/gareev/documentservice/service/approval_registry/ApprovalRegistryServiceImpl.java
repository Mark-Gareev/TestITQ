package ru.gareev.documentservice.service.approval_registry;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gareev.documentservice.entity.ApprovalRegistryItem;
import ru.gareev.documentservice.repository.ApprovalRegistryRepository;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ApprovalRegistryServiceImpl implements ApprovalRegistryService{
    private final ApprovalRegistryRepository registryRepository;
    @Override
    public ApprovalRegistryItem createApproved(String author, Long id) {
        ApprovalRegistryItem item = new ApprovalRegistryItem();
        item.setApprovalActor(author);
        item.setApprovalDateTime(LocalDateTime.now());
        item.setDocumentId(id);
        return registryRepository.save(item);
    }
}
