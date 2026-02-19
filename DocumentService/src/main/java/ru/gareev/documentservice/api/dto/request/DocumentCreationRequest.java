package ru.gareev.documentservice.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocumentCreationRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String author;
}
