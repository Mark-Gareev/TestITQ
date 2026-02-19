package ru.gareev.utilservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;
import ru.gareev.utilservice.api.dto.DocumentCreateRequest;
import ru.gareev.utilservice.api.dto.PageResponse;
import ru.gareev.utilservice.api.dto.StatusChangeRequest;
import ru.gareev.utilservice.api.dto.StatusChangeResponseItem;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
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

    public List<Long> getSubmit(long submitCount) {
        return searchByStatus(submitCount, "SUBMITTED");
    }

    public List<Long> getDraft(long draftCount) {
        return searchByStatus(draftCount, "DRAFT");
    }

    public List<StatusChangeResponseItem> approve(List<Long> submitted) {
        StatusChangeRequest request = getChangeRequest(submitted);
        return client.put()
                .uri("api/documents/approve")
                .body(request)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

    }

    public List<StatusChangeResponseItem> submit(List<Long> drafts) {
        StatusChangeRequest request = getChangeRequest(drafts);
        return client.put()
                .uri("api/documents/submit")
                .body(request)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    private StatusChangeRequest getChangeRequest(List<Long> ids) {
        return StatusChangeRequest.builder()
                .ids(ids)
                .author(this.getClass().getName())
                .build();
    }

    private List<Long> searchByStatus(long count, String status) {
        PageResponse response = client.get()
                .uri(uriBuilder -> {
                    UriBuilder builder = uriBuilder.path("api/documents/list");
                    getSearchMapForStatus(count, status)
                            .forEach(builder::queryParam);
                    return builder.build();
                })
                .retrieve()
                .body(PageResponse.class);
        if (response != null) {
            return response.getIds().stream()
                    .map(PageResponse.DocumentId::getId)
                    .toList();
        } else {
            log.error("result of searching is empty");
            return Collections.emptyList();
        }
    }

    private Map<String, String> getSearchMapForStatus(long submitCount, String status) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("status", status);
        paramMap.put("page", "0");
        paramMap.put("size", String.valueOf(submitCount));
        return paramMap;
    }

}
