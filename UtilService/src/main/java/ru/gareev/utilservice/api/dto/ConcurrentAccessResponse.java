package ru.gareev.utilservice.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConcurrentAccessResponse {
    private Long failureAttemptCount;
    private Long successfulAttemptCount;
    private Long notFountAttemptCount;
    private String finalStatus;
}
