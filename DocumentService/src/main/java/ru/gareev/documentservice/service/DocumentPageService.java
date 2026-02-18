package ru.gareev.documentservice.service;

import org.springframework.data.domain.Page;
import ru.gareev.documentservice.api.dto.DocumentListItemDto;

import java.time.LocalDateTime;
import java.util.List;

public interface DocumentPageService {
    Page<DocumentListItemDto> getPage(String createdBy,
                                      LocalDateTime createdDateFrom,
                                      LocalDateTime createdDateTo,
                                      String status,
                                      int page,
                                      int size,
                                      List<String> sortBy);
}
