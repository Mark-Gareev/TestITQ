package ru.gareev.utilservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.gareev.utilservice.api.dto.DocumentCreateRequest;

@Service
public class DocumentConnectionProvider {
    private final RestClient client;

    public DocumentConnectionProvider(@Value("${documents.base-url}") String baseUrl) {
        this.client = RestClient.create(baseUrl);
    }

    public boolean ping() {
        ResponseEntity<String> entity = client.get()
                .uri("/api/documents/{id}", 1)
                .retrieve()
                .toEntity(String.class);
        return entity.getStatusCode().is2xxSuccessful();
    }

    public ResponseEntity<Void> create(DocumentCreateRequest request) {
        return client.post()
                .uri("api/documents/create")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toEntity(Void.class);
    }
}
