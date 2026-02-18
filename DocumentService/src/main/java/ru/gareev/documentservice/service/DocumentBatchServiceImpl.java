package ru.gareev.documentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gareev.documentservice.api.dto.StatusChangeRequest;
import ru.gareev.documentservice.api.dto.StatusMovingResultItem;
import ru.gareev.documentservice.entity.DocumentStatus;
import ru.gareev.documentservice.exceptions.UnsupportedStatusMove;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * if we have huge batches we can use Spring Batch mechanics, now optimistic lock + cycle
 */
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
        for (Long id : ids) {
            try {
                documentService.changeDocumentStatus(author,id, targetStatus);
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
            }
        }
        return res;
    }

}
