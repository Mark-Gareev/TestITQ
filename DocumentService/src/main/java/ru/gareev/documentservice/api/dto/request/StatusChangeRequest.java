package ru.gareev.documentservice.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StatusChangeRequest {
    @NotBlank
    private String author;
    @NotBlank
    private List<Long> ids;
}
