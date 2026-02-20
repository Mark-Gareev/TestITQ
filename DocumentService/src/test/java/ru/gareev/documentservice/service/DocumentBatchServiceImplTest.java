package ru.gareev.documentservice.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.gareev.documentservice.api.dto.request.DocumentCreationRequest;
import ru.gareev.documentservice.api.dto.request.StatusChangeRequest;
import ru.gareev.documentservice.api.dto.response.DocumentDto;
import ru.gareev.documentservice.api.dto.response.DocumentListItemDto;
import ru.gareev.documentservice.api.dto.response.StatusMovingResultItem;
import ru.gareev.documentservice.entity.DocumentStatus;
import ru.gareev.documentservice.repository.DocumentRepository;
import ru.gareev.documentservice.service.approval_registry.ApprovalRegistryService;
import ru.gareev.documentservice.service.document.DocumentService;
import ru.gareev.documentservice.service.document_batch.DocumentBatchService;
import ru.gareev.documentservice.util.TestUtil;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class DocumentBatchServiceImplTest {
    @Autowired
    private DocumentRepository repository;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DocumentBatchService documentBatchService;

    @MockitoBean
    private ApprovalRegistryService approvalRegistryService;

    @Test
    void rollbackApproveWithoutRegistry() {
        int count = 10;
        List<Long> documentIds = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            documentIds.add(documentService.createDocument(TestUtil.getCreationRequest("n", "d")).getId());
        }
        documentBatchService.batchSubmit(TestUtil.getStatusChangeRequest("s", documentIds));
        when(approvalRegistryService.createApproved("a", documentIds.get(0)))
                .thenThrow(RuntimeException.class);
        List<StatusMovingResultItem> res = documentBatchService.batchApprove(TestUtil.getStatusChangeRequest("a", documentIds));
        for (StatusMovingResultItem item : res) {
            if (item.getId().equals(documentIds.get(0))) {
                Assertions.assertSame("конфликт", item.getResult());
            } else {
                Assertions.assertSame("успешно", item.getResult());
            }
        }
        DocumentDto dto = documentService.getDocument(documentIds.get(0));
        Assertions.assertNotEquals(dto.getStatus(), DocumentStatus.APPROVED.toString());
    }

    @Test
    void batchSubmit() {
        int counter = 10;
        DocumentCreationRequest req = TestUtil.getCreationRequest("na", "au");
        List<DocumentDto> created = new ArrayList<>();
        for (int i = 0; i < counter; i++) {
            created.add(documentService.createDocument(req));
        }
        List<Long> ids = created.stream().map(DocumentDto::getId).toList();
        StatusChangeRequest changeRequest = TestUtil.getStatusChangeRequest("sub", ids);
        documentBatchService.batchSubmit(changeRequest);
        List<DocumentListItemDto> submitted = documentService.getDocuments(ids);
        for (DocumentListItemDto dto : submitted) {
            Assertions.assertEquals(DocumentStatus.SUBMITTED.toString(), dto.getStatus());
        }
    }
}