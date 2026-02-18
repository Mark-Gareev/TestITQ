package ru.gareev.utilservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.gareev.utilservice.api.dto.DocumentCreateRequest;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService{
    private final DocumentConnectionProvider provider;
    @Override
    public boolean ping() {
        return provider.ping();
    }

    @Override
    public void createDocuments(Long documentsCount) {
        DocumentCreateRequest request = DocumentCreateRequest.builder()
                .author("utilService")
                .build();
        for(long l = 0; l < documentsCount; l++){
            request.setName("cycle creating " + l);
            ResponseEntity<Void> response = provider.create(request);
            if(!response.getStatusCode().is2xxSuccessful()){
                //log error when adding logs
                System.out.println("error creating document");
            }
        }
    }
}
