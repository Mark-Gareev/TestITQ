package ru.gareev.documentservice.api.controller;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import ru.gareev.documentservice.api.dto.*;
import ru.gareev.documentservice.entity.ActionType;
import ru.gareev.documentservice.entity.DocumentStatus;
import ru.gareev.documentservice.util.TestUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DocumentControllerTest {
    @Autowired
    private DocumentController controller;

    @Autowired
    EntityManager manager;

    @Test
    public void fromCreateToApproveDocumentTest() {
        String name = "teste";
        String author = "tester";
        //create
        DocumentCreationRequest request = DocumentCreationRequest.builder()
                .name(name)
                .author(author)
                .build();
        DocumentDto dto = controller.createDocument(request);
        Assertions.assertNotNull(dto);
        Assertions.assertNotNull(dto.getId());
        Assertions.assertNotNull(dto.getCreationDateTime());
        Assertions.assertNotNull(dto.getLastUpdateDateTime());
        Assertions.assertEquals(dto.getName(), name);
        Assertions.assertEquals(dto.getAuthor(), author);
        //submit
        String submitterName = "Submitter";
        StatusChangeRequest changeRequest = StatusChangeRequest.builder()
                .ids(Collections.singletonList(dto.getId()))
                .author(submitterName)
                .build();
        List<StatusMovingResultItem> res = controller.submitDocuments(changeRequest);
        Assertions.assertNotNull(res);
        Assertions.assertFalse(res.isEmpty());
        Assertions.assertEquals(res.get(0).getId(), dto.getId());
        Assertions.assertEquals(res.get(0).getResult(), "успешно");
        //approve
        String approverName = "Approver";
        changeRequest.setAuthor(approverName);
        res = controller.approveDocuments(changeRequest);
        Assertions.assertNotNull(res);
        Assertions.assertFalse(res.isEmpty());
        Assertions.assertEquals(res.get(0).getId(), dto.getId());
        Assertions.assertEquals(res.get(0).getResult(), "успешно");
    }

    @Test
    public void pageSearchingByStatusTest() {
        String name = "teste";
        String author = "tester";
        //create
        DocumentCreationRequest request = DocumentCreationRequest.builder()
                .name(name)
                .author(author)
                .build();
        List<DocumentDto> documents = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            documents.add(controller.createDocument(request));
        }
        //confirm two of them
        List<Long> confirming = new ArrayList<>(2);
        confirming.add(documents.get(0).getId());
        confirming.add(documents.get(1).getId());
        StatusChangeRequest changeRequest = StatusChangeRequest.builder()
                .author(author)
                .ids(confirming)
                .build();
        controller.submitDocuments(changeRequest);
        // find submitted
        String status = DocumentStatus.SUBMITTED.toString();
        Page<DocumentListItemDto> res = controller.getDocumentList(
                null,
                null,
                null,
                status,
                0,
                10,
                Collections.singletonList("id,desc")
        );
        Assertions.assertNotNull(res);
        Assertions.assertFalse(res.isEmpty());
        Assertions.assertEquals(res.getTotalElements(), 2);
    }

    @Test
    public void pageSearchByAuthorTest() {
        //even only!
        long count = 10;
        String name1 = "odd";
        String name2 = "even";
        String docname = "doc";
        //create
        DocumentCreationRequest request = DocumentCreationRequest.builder()
                .name(docname)
                .build();
        for (int i = 1; i <= count; i++) {
            String n = i % 2 == 0 ? name2 : name1;
            request.setAuthor(n);
            controller.createDocument(request);
        }
        //find by name 1
        Page<DocumentListItemDto> n1 = controller.getDocumentList(
                name1,
                null,
                null,
                null,
                0,
                10,
                Collections.singletonList("id,desc"));
        //find by name2
        Page<DocumentListItemDto> n2 = controller.getDocumentList(
                name2,
                null,
                null,
                null,
                0,
                10,
                Collections.singletonList("id,desc"));
        Assertions.assertEquals(n2.getTotalElements(), n1.getTotalElements());
        Assertions.assertEquals(n2.getTotalElements(), count / 2);
        for (DocumentListItemDto dto : n1) {
            Assertions.assertEquals(name1, dto.getAuthor());
        }
    }

    @Test
    public void createSubmitAndGetFullDocumentTest() {
        DocumentDto dto = controller.createDocument(TestUtil.getCreationRequest("n", "a"));
        controller.submitDocuments(
                TestUtil.getStatusChangeRequest("s", dto.getId())
        );
        manager.flush();
        manager.clear();
        FullDocumentDto full = controller.getFullDocumentById(dto.getId());
        //assert activities
        Assertions.assertFalse(full.getActivity().isEmpty());
        Assertions.assertEquals(1, full.getActivity().size());
        ActivityFeedItemDto activity = full.getActivity().get(0);
        Assertions.assertEquals(activity.getAction(), ActionType.SUBMIT.toString());
        //assert document
        DocumentDto resDocument = full.getDocumentDto();
        Assertions.assertEquals(resDocument.getStatus(), DocumentStatus.SUBMITTED.toString());
    }
}