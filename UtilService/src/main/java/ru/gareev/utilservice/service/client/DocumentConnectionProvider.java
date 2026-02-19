package ru.gareev.utilservice.service.client;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;
import ru.gareev.utilservice.api.dto.DocumentCreateRequest;
import ru.gareev.utilservice.api.dto.PageResponse;
import ru.gareev.utilservice.api.dto.StatusChangeRequest;
import ru.gareev.utilservice.api.dto.StatusChangeResponseItem;
import ru.gareev.utilservice.entity.Document;
import ru.gareev.utilservice.entity.OperationStatus;
import ru.gareev.utilservice.exceptions.LocalNetworkException;
import ru.gareev.utilservice.util.Constants;

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

    public ResponseEntity<Void> create(DocumentCreateRequest request) {
        return client.post()
                .uri("api/documents/create")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.corIdHeader, MDC.get(Constants.corIdKey))
                .body(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, resp) -> {
                    throw new LocalNetworkException(resp.getStatusText());
                })
                .toEntity(Void.class);
    }

    public List<Long> getSubmit(long submitCount) {
        return searchByStatus(submitCount, "SUBMITTED");
    }

    public List<Long> getDraft(long draftCount) {
        return searchByStatus(draftCount, "DRAFT");
    }

    public List<StatusChangeResponseItem> approve(List<Long> submitted, String author) {
        StatusChangeRequest request = getChangeRequest(submitted, author);
        return client.put()
                .uri("api/documents/approve")
                .header(Constants.corIdHeader, MDC.get(Constants.corIdKey))
                .body(request)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

    }

    public List<StatusChangeResponseItem> submit(List<Long> drafts, String author) {
        StatusChangeRequest request = getChangeRequest(drafts, author);
        return client.put()
                .uri("api/documents/submit")
                .body(request)
                .header(Constants.corIdHeader, MDC.get(Constants.corIdKey))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public Document getDocument(Long docId) {
        return client.get()
                .uri("/api/documents/{}", docId)
                .header(Constants.corIdHeader, MDC.get(Constants.corIdKey))
                .retrieve()
                .body(Document.class);
    }

    public OperationStatus statusApprove(Long id, String author) {
        StatusChangeRequest request = getChangeRequest(Collections.singletonList(id), author);
        StatusChangeResponseItem response = client.put()
                .uri("api/documents/approve")
                .header(Constants.corIdHeader, MDC.get(Constants.corIdKey))
                .body(request)
                .retrieve()
                .body(StatusChangeResponseItem.class);
        if (response != null) {
            return OperationStatus.forString(response.getResult());
        } else {
            throw new LocalNetworkException("approve request to document service returns null");
        }
    }

    private StatusChangeRequest getChangeRequest(List<Long> ids, String author) {
        return StatusChangeRequest.builder()
                .ids(ids)
                .author(author == null ? this.getClass().getName() : author)
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
                .header(Constants.corIdHeader, MDC.get(Constants.corIdKey))
                .retrieve()
                .body(PageResponse.class);
        if (response != null) {
            return response.getIds().stream()
                    .map(PageResponse.DocumentId::getId)
                    .toList();
        } else {
            throw new LocalNetworkException("result of searching is null");
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
