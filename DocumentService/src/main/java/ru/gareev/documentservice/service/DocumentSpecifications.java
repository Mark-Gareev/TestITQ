package ru.gareev.documentservice.service;

import org.springframework.data.jpa.domain.Specification;
import ru.gareev.documentservice.entity.Document;
import ru.gareev.documentservice.entity.DocumentStatus;

import java.time.LocalDateTime;

public class DocumentSpecifications {
    public static Specification<Document> createdByEquals(String createdBy) {
        return (root, query, cb) ->
                createdBy == null ? null : cb.equal(root.get("createdBy"), createdBy);
    }

    public static Specification<Document> createdDateFrom(LocalDateTime from) {
        return (root, query, cb) ->
                from == null ? null : cb.greaterThanOrEqualTo(root.get("createdDate"), from);
    }

    public static Specification<Document> createdDateTo(LocalDateTime to) {
        return (root, query, cb) ->
                to == null ? null : cb.lessThanOrEqualTo(root.get("createdDate"), to);
    }

    public static Specification<Document> statusEquals(DocumentStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }
}
