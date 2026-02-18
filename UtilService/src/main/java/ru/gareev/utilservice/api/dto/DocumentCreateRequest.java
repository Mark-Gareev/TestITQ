package ru.gareev.utilservice.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DocumentCreateRequest {
    private String author;
    private String name;
}
