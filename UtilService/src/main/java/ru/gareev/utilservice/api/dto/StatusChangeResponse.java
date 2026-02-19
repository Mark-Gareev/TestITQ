package ru.gareev.utilservice.api.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StatusChangeResponse {
    private List<StatusChangeResponseItem> items;
}
