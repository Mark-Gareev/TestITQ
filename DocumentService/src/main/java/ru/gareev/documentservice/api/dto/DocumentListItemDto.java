package ru.gareev.documentservice.api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DocumentListItemDto {
    private Long id;
    private String status;
    private String name;
    private String author;
    private LocalDateTime creationDateTime;
    private LocalDateTime updateDateTime;

}
