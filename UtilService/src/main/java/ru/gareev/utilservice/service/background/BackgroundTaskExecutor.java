package ru.gareev.utilservice.service.background;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import ru.gareev.utilservice.config.BackgroundTaskConfig;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class BackgroundTaskExecutor {

    private final BackgroundTaskConfig config;
    private final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    private final BackgroundApproveService approveService;
    private final BackgroundSubmitService submitService;

    @PostConstruct
    public void init() {
        scheduler.initialize();
        startSubmit();
        startApprove();
    }

    private void startApprove() {
        scheduler.scheduleWithFixedDelay(approveService::approve,
                Duration.ofMillis(config.getApproveRepeatMillis()));
    }


    private void startSubmit() {
        scheduler.scheduleWithFixedDelay(submitService::submit,
                Duration.ofMillis(config.getSubmitRepeatMillis()));
    }
}
