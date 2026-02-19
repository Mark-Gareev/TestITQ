package ru.gareev.utilservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.gareev.utilservice.api.dto.DocumentCreateRequest;
import ru.gareev.utilservice.aspect.CreatingTask;
import ru.gareev.utilservice.service.client.DocumentConnectionProvider;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentServiceImpl implements DocumentService {
    private final DocumentConnectionProvider provider;

    @Override
    @CreatingTask
    public void createDocuments(Long documentsCount) {
        DocumentCreateRequest request = DocumentCreateRequest.builder()
                .author("utilService")
                .build();
        log.info("start creating {} documents",documentsCount);
        long createdCount = 0L;
        for (long l = 0; l < documentsCount; l++) {
            request.setName("cycle creating " + l);
            ResponseEntity<Void> response = provider.create(request);
            if (!response.getStatusCode().is2xxSuccessful()) {
                log.info("get error while creating document http code {}", response.getStatusCode().value());
            }else{
                log.info("successfully create document");
                createdCount++;
            }
            log.info("created {} documents",createdCount);
        }
    }
}
