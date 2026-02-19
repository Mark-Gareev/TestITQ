package ru.gareev.documentservice.api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class DocumentDto {
    private Long id;
    private String name;
    private String author;
    private String status;
    private LocalDateTime creationDateTime;
    private LocalDateTime lastUpdateDateTime;
    private List<ActivityFeedItemDto> activityFeed;
}
