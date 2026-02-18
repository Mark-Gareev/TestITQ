package ru.gareev.documentservice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@Entity(name="approval_registry")
public class ApprovalRegistryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @Column(name="approval_actor", nullable = false)
    private String approvalActor;

    @Column(name="approval_date_time", nullable = false)
    private LocalDateTime approvalDateTime;
}
