package ru.gareev.documentservice.util;

import ru.gareev.documentservice.api.dto.request.DocumentCreationRequest;
import ru.gareev.documentservice.api.dto.request.StatusChangeRequest;

import java.util.Arrays;
import java.util.List;

public class TestUtil {

    public static DocumentCreationRequest getCreationRequest(String name, String author){
        return DocumentCreationRequest.builder()
                .name(name)
                .author(author)
                .build();
    }
    public static StatusChangeRequest getStatusChangeRequest(String author, Long...ids){
        return getStatusChangeRequest(author,Arrays.asList(ids));
    }

    public static StatusChangeRequest getStatusChangeRequest(String author, List<Long> ids) {
        return StatusChangeRequest.builder()
                .author(author)
                .ids(ids)
                .build();
    }
}
