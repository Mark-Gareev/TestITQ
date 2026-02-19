package ru.gareev.utilservice.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class StatusChangeResponseItem {
    private Long id;
    private String result;
}
