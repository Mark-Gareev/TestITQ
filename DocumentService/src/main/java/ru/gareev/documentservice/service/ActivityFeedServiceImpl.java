package ru.gareev.documentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gareev.documentservice.entity.ActionType;
import ru.gareev.documentservice.entity.ActivityFeedItem;
import ru.gareev.documentservice.entity.Document;
import ru.gareev.documentservice.entity.DocumentStatus;
import ru.gareev.documentservice.repository.ActivityFeedRepository;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ActivityFeedServiceImpl implements ActivityFeedService {
    private final ActivityFeedRepository repository;

    @Override
    public void createActivityItem(Document document, DocumentStatus status, String author) {
        ActivityFeedItem item = new ActivityFeedItem();
        item.setActor(author);
        item.setActivityDateTime(LocalDateTime.now());
        item.setDocument(document);
        switch (status) {
            case SUBMITTED -> {
                item.setActionType(ActionType.SUBMIT);
            }
            case APPROVED -> {
                item.setActionType(ActionType.APPROVE);
            }
            default -> throw new UnsupportedOperationException("cannot create activity feed in draft or null state");
        }
        repository.save(item);
    }
}
