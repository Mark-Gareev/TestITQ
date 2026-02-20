package ru.gareev.documentservice.api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DocumentDto {
    private Long id;
    private String name;
    private String author;
    private String status;
    private LocalDateTime creationDateTime;
    private LocalDateTime lastUpdateDateTime;
}
