package ru.gareev.documentservice.api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApprovalRegistryItemDto {
    private String author;
    private Long documentId;
}
