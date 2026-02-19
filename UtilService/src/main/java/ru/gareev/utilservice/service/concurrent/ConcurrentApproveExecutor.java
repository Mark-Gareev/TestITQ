package ru.gareev.utilservice.service.concurrent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.gareev.utilservice.api.dto.ConcurrentAccessResponse;
import ru.gareev.utilservice.entity.ConcurrentResult;
import ru.gareev.utilservice.entity.Document;
import ru.gareev.utilservice.entity.OperationStatus;
import ru.gareev.utilservice.service.client.DocumentConnectionProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConcurrentApproveExecutor {
    private final DocumentConnectionProvider connectionProvider;
    private static final String AUTHOR = "concurrentApprove";

    public ConcurrentAccessResponse executeEveryThread(int threads, int attempts, Long docId) {

        ExecutorService executor = Executors.newFixedThreadPool(threads);
        List<Callable<ConcurrentResult>> tasks = new ArrayList<>();

        for (int i = 0; i < threads; i++) {
            tasks.add(() -> executeInThread(attempts, docId));
        }

        List<Future<ConcurrentResult>> futures = null;
        try {
            futures = executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            log.error("one or more concurrent modifications has been interrupted");
            log.error(e.getMessage());
        }

        ConcurrentResult total = new ConcurrentResult(0, 0, 0);

        if (futures != null) {
            for (Future<ConcurrentResult> future : futures) {
                try {
                    total = total.merge(future.get());
                } catch (InterruptedException e) {
                    log.error("Future.get was interrupted ",e);
                } catch (ExecutionException e) {
                    log.error("Future.get has ExecException",e);
                }
            }
        } else {
            log.error("No futures received");
        }
        executor.shutdown();
        Document document = connectionProvider.getDocument(docId);
        return ConcurrentAccessResponse
                .builder()
                .successfulAttemptCount(total.getSuccess())
                .failureAttemptCount(total.getFailure())
                .notFountAttemptCount(total.getNotFound())
                .finalStatus(document.getStatus().toString())
                .build();
    }

    private ConcurrentResult executeInThread(int attempts, Long id) {
        int success = 0;
        int failure = 0;
        int notFound = 0;
        for (int i = 0; i < attempts; i++) {
            OperationStatus status = connectionProvider.statusApprove(id, AUTHOR);
            switch (status) {
                case SUCCESS -> success++;
                case FAILURE -> failure++;
                case NOT_FOUND -> notFound++;
            }
        }
        return new ConcurrentResult(success, failure, notFound);
    }
}
