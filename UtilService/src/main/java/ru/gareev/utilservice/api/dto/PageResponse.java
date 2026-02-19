package ru.gareev.utilservice.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * class to deserialize JPA page of Documents
 */
@Getter
@Setter
public class PageResponse {
    @JsonProperty("content")
    private List<DocumentId> ids;

    @Getter
    @Setter
    public static class DocumentId {
        private Long id;

    }

}
