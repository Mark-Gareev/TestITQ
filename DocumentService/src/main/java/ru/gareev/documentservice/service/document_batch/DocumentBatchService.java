package ru.gareev.documentservice.service.document_batch;

import ru.gareev.documentservice.api.dto.request.StatusChangeRequest;
import ru.gareev.documentservice.api.dto.response.StatusMovingResultItem;

import java.util.List;

public interface DocumentBatchService {
    List<StatusMovingResultItem> batchSubmit(StatusChangeRequest request);

    List<StatusMovingResultItem> batchApprove(StatusChangeRequest request);
}
