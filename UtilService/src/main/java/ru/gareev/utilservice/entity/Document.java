package ru.gareev.utilservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Document {
    private Long id;
    private DocumentStatus status;
}
