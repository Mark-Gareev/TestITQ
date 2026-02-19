package ru.gareev.utilservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.gareev.utilservice.api.dto.StatusChangeResponseItem;
import ru.gareev.utilservice.aspect.BackgroundApprove;
import ru.gareev.utilservice.config.BackgroundTaskConfig;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class BackgroundApproveService {
    private final DocumentConnectionProvider connectionProvider;
    private final BackgroundTaskConfig config;
    @BackgroundApprove
    public void approve() {
        List<Long> submitted = connectionProvider.getSubmit(config.getApproveBatchSize());
        if (submitted.size() == config.getApproveBatchSize()) {
            List<StatusChangeResponseItem> resp = connectionProvider.approve(submitted);
            for (StatusChangeResponseItem item : resp) {
                log.info("for approve document with id {} has operation state {}",
                        item.getId(),
                        item.getResult());
            }
        } else {
            log.info("not full batch in documents for executing");
        }
    }
}
