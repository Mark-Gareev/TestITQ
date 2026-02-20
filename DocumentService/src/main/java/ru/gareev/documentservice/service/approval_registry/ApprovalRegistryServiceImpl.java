package ru.gareev.documentservice.service.approval_registry;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gareev.documentservice.api.dto.response.ApprovalRegistryItemDto;
import ru.gareev.documentservice.entity.ApprovalRegistryItem;
import ru.gareev.documentservice.repository.ApprovalRegistryRepository;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ApprovalRegistryServiceImpl implements ApprovalRegistryService {
    private final ApprovalRegistryRepository registryRepository;

    @Override
    public ApprovalRegistryItem createApproved(String author, Long id) {
        ApprovalRegistryItem item = new ApprovalRegistryItem();
        item.setApprovalActor(author);
        item.setApprovalDateTime(LocalDateTime.now());
        item.setDocumentId(id);
        return registryRepository.save(item);
    }

    @Override
    public List<ApprovalRegistryItemDto> getItemsByDocumentId(Long docId) {
        List<ApprovalRegistryItem> items = registryRepository.findApprovalRegistryItemsByDocumentId(docId);
        return items.stream()
                .map((it) -> ApprovalRegistryItemDto.builder()
                        .documentId(it.getDocumentId())
                        .author(it.getApprovalActor())
                        .build())
                .toList();
    }
}
