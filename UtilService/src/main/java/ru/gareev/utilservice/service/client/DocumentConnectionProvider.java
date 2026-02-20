package ru.gareev.utilservice.service.client;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;
import ru.gareev.utilservice.api.dto.DocumentCreateRequest;
import ru.gareev.utilservice.api.dto.PageResponse;
import ru.gareev.utilservice.api.dto.StatusChangeRequest;
import ru.gareev.utilservice.api.dto.StatusChangeResponseItem;
import ru.gareev.utilservice.aspect.DocumentServiceConnection;
import ru.gareev.utilservice.entity.Document;
import ru.gareev.utilservice.entity.OperationStatus;
import ru.gareev.utilservice.exceptions.LocalNetworkException;
import ru.gareev.utilservice.util.Constants;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class DocumentConnectionProvider {
    private final RestClient client;

    public DocumentConnectionProvider(@Value("${documents.base-url}") String baseUrl) {
        this.client = RestClient.create(baseUrl);
    }

    @DocumentServiceConnection
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

    @DocumentServiceConnection
    public List<Long> getSubmit(long submitCount) {
        return searchByStatus(submitCount, "SUBMITTED");
    }

    @DocumentServiceConnection
    public List<Long> getDraft(long draftCount) {
        return searchByStatus(draftCount, "DRAFT");
    }

    @DocumentServiceConnection
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

    @DocumentServiceConnection
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

    @DocumentServiceConnection
    public Document getDocument(Long docId) {
        try {
            return client.get()
                    .uri("/api/documents/" + docId)
                    .header(Constants.corIdHeader, MDC.get(Constants.corIdKey))
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (req, resp) -> {
                        if (resp.getStatusCode().equals(HttpStatusCode.valueOf(404))) {
                            throw new NoSuchElementException();
                        }
                    })
                    .body(Document.class);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    private boolean is404Err(ClientHttpResponse clientHttpResponse) throws IOException {
        return clientHttpResponse.getStatusCode().value() == 404;
    }

    @DocumentServiceConnection
    public OperationStatus statusApprove(Long id, String author) {
        StatusChangeRequest request = getChangeRequest(Collections.singletonList(id), author);
        List<StatusChangeResponseItem> response = client.put()
                .uri("api/documents/approve")
                .header(Constants.corIdHeader, MDC.get(Constants.corIdKey))
                .body(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, resp) -> {
                    throw new LocalNetworkException(resp.getStatusCode() + " " + resp.getStatusText());
                })
                .body(new ParameterizedTypeReference<>() {
                });
        if (response != null && !response.isEmpty()) {
            return OperationStatus.forString(response.get(0).getResult());
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
