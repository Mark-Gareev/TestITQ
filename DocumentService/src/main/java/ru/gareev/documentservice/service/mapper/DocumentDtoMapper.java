package ru.gareev.documentservice.service.mapper;

import org.springframework.stereotype.Service;
import ru.gareev.documentservice.api.dto.response.ActivityFeedItemDto;
import ru.gareev.documentservice.api.dto.response.DocumentDto;
import ru.gareev.documentservice.api.dto.response.DocumentListItemDto;
import ru.gareev.documentservice.api.dto.response.FullDocumentDto;
import ru.gareev.documentservice.entity.ActivityFeedItem;
import ru.gareev.documentservice.entity.Document;

@Service
public class DocumentDtoMapper {
    public FullDocumentDto toFullDocumentDto(Document document) {
        FullDocumentDto res = new FullDocumentDto();
        res.setDocumentDto(toDto(document));
        res.setActivity(document.getActivities().stream()
                .map(this::toActivityFeedDto)
                .toList());
        return res;
    }

    private ActivityFeedItemDto toActivityFeedDto(ActivityFeedItem activityFeedItem) {
        return ActivityFeedItemDto.builder()
                .action(activityFeedItem.getActionType().toString())
                .author(activityFeedItem.getActor())
                .actionDateTime(activityFeedItem.getActivityDateTime())
                .build();

    }

    public DocumentDto toDto(Document document) {
        return DocumentDto.builder()
                .id(document.getId())
                .name(document.getName())
                .status(document.getStatus().toString())
                .author(document.getCreatedBy())
                .creationDateTime(document.getCreationDateTime())
                .lastUpdateDateTime(document.getUpdateDateTime())
                .build();
    }

    public DocumentListItemDto toListDto(Document document) {
        return DocumentListItemDto.builder()
                .id(document.getId())
                .name(document.getName())
                .author(document.getCreatedBy())
                .status(document.getStatus().toString())
                .creationDateTime(document.getCreationDateTime())
                .updateDateTime(document.getUpdateDateTime())
                .build();
    }
}
