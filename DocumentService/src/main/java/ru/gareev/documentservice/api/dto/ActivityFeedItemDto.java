package ru.gareev.documentservice.api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ActivityFeedItemDto {
    /**
     * may be api enum item, if we use it in other services, now in demo app it toString result of inner enum
     */
    private String action;
    private LocalDateTime actionDateTime;
    private String author;
}
