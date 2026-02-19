package ru.gareev.utilservice.service.background;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.gareev.utilservice.api.dto.StatusChangeResponseItem;
import ru.gareev.utilservice.aspect.BackgroundApprove;
import ru.gareev.utilservice.config.BackgroundTaskConfig;
import ru.gareev.utilservice.service.client.DocumentConnectionProvider;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class BackgroundApproveService {
    private final DocumentConnectionProvider connectionProvider;
    private final BackgroundTaskConfig config;
    private static final String AUTHOR = "backgroundApprove";

    @BackgroundApprove
    public void approve() {
        List<Long> submitted = connectionProvider.getSubmit(config.getApproveBatchSize());
        if (submitted.size() == config.getApproveBatchSize()) {
            List<StatusChangeResponseItem> resp = connectionProvider.approve(submitted, AUTHOR);
            long start = System.currentTimeMillis();
            for (StatusChangeResponseItem item : resp) {
                log.info("for approve document with id {} has operation state {}",
                        item.getId(),
                        item.getResult());
            }
            long end = System.currentTimeMillis();
            log.info("batch with size {}executed for {}ms", resp.size(), end - start);
        } else {
            log.info("not full batch in documents for executing");
        }
    }
}
