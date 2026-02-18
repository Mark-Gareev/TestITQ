package ru.gareev.documentservice.service;

import ru.gareev.documentservice.api.dto.StatusChangeRequest;
import ru.gareev.documentservice.api.dto.StatusMovingResultItem;

import java.util.List;

public interface DocumentBatchService {
    List<StatusMovingResultItem> batchSubmit(StatusChangeRequest request);

    List<StatusMovingResultItem> batchApprove(StatusChangeRequest request);
}
