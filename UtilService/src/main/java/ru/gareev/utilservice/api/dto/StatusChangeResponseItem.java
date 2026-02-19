package ru.gareev.utilservice.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatusChangeResponseItem {
    private Long id;
    private String result;
}
