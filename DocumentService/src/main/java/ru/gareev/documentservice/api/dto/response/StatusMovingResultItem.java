package ru.gareev.documentservice.api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatusMovingResultItem {
    private Long id;
    //should be enum
    private String result;
}
