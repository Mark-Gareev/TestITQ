package ru.gareev.documentservice.api.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StatusChangeRequest {
    private String author;
    private List<Long> ids;
}
