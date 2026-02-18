package ru.gareev.documentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.gareev.documentservice.entity.Document;

public interface DocumentRepository extends JpaRepository<Document,Long>,
        JpaSpecificationExecutor<Document> {

    Document findDocumentById(Long id);

    @Query("select d from document d left join fetch d.activities where d.id = :id")
    Document findFullDocumentById(Long id);
}
