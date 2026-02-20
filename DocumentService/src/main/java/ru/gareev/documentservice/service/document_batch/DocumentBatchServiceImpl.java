package ru.gareev.documentservice.service.document_batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import ru.gareev.documentservice.api.dto.request.StatusChangeRequest;
import ru.gareev.documentservice.api.dto.response.StatusMovingResultItem;
import ru.gareev.documentservice.entity.DocumentStatus;
import ru.gareev.documentservice.exceptions.UnsupportedStatusMove;
import ru.gareev.documentservice.service.document.DocumentService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * if we have huge batches we can use Spring Batch mechanics, now optimistic lock + cycle
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class DocumentBatchServiceImpl implements DocumentBatchService {
    private final DocumentService documentService;


    @Override
    public List<StatusMovingResultItem> batchSubmit(StatusChangeRequest request) {
        return this.batchStatusMove(request.getAuthor(), request.getIds(), DocumentStatus.SUBMITTED);
    }

    @Override
    public List<StatusMovingResultItem> batchApprove(StatusChangeRequest request) {
        return this.batchStatusMove(request.getAuthor(), request.getIds(), DocumentStatus.APPROVED);
    }

    private List<StatusMovingResultItem> batchStatusMove(String author, List<Long> ids, DocumentStatus targetStatus) {
        List<StatusMovingResultItem> res = new ArrayList<>(ids.size());
        log.info("received batch update with size {}", ids.size());
        long start = System.currentTimeMillis();
        for (Long id : ids) {
            try {
                documentService.changeDocumentStatus(author, id, targetStatus);
                res.add(StatusMovingResultItem.builder()
                        .id(id)
                        .result("успешно")
                        .build());
            } catch (NoSuchElementException e) {
                res.add(StatusMovingResultItem.builder()
                        .result("не найдено")
                        .id(id)
                        .build()
                );
            } catch (UnsupportedStatusMove us) {
                res.add(StatusMovingResultItem.builder()
                        .result("конфликт")
                        .id(id)
                        .build()
                );
            } catch (OptimisticLockingFailureException e){
                log.info("optimistic failure, rollback transaction for id {}",id);
                res.add(StatusMovingResultItem.builder()
                        .result("конфликт")
                        .id(id)
                        .build()
                );
            }
        }
        long end = System.currentTimeMillis();
        log.info("batch update proceed for {}ms", end - start);
        return res;
    }

}
