package ru.gareev.documentservice.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StatusChangeRequest {
    @NotBlank
    private String author;
    @Size(min = 1)
    private List<Long> ids;
}
