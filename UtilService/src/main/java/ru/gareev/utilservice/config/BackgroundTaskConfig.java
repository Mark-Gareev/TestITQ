package ru.gareev.utilservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class BackgroundTaskConfig {

    @Value("${background-task.submit.repeat-millis}")
    private long submitRepeatMillis;

    @Value("${background-task.submit.batch-size}")
    private int submitBatchSize;

    @Value("${background-task.approve.repeat-millis}")
    private long approveRepeatMillis;

    @Value("${background-task.approve.batch-size}")
    private int approveBatchSize;
}
