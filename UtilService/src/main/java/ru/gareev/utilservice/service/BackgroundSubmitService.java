package ru.gareev.utilservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.gareev.utilservice.api.dto.StatusChangeResponseItem;
import ru.gareev.utilservice.aspect.BackgroundSubmit;
import ru.gareev.utilservice.config.BackgroundTaskConfig;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class BackgroundSubmitService {
    private final BackgroundTaskConfig config;
    private final DocumentConnectionProvider connectionProvider;
    private static final String AUTHOR = "backgroundSubmit";
    @BackgroundSubmit
    public void submit() {
        List<Long> drafts = connectionProvider.getDraft(config.getSubmitBatchSize());
        if (drafts.size() == config.getSubmitBatchSize()) {
            List<StatusChangeResponseItem> resp = connectionProvider.submit(drafts,AUTHOR);
            for (StatusChangeResponseItem item : resp) {
                log.info("for submit document with id {} has operation state {}",
                        item.getId(),
                        item.getResult());
            }
        } else {
            log.info("not full batch in documents for executing");
        }
    }
}
