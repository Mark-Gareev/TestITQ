package ru.gareev.documentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gareev.documentservice.entity.ApprovalRegistryItem;

import java.util.List;

@Repository
public interface ApprovalRegistryRepository extends JpaRepository<ApprovalRegistryItem,Long> {
    List<ApprovalRegistryItem> findApprovalRegistryItemsByDocumentId(Long id);
}
