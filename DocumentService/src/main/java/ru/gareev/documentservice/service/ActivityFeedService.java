package ru.gareev.documentservice.service;

import ru.gareev.documentservice.entity.Document;
import ru.gareev.documentservice.entity.DocumentStatus;

public interface ActivityFeedService {

    void createActivityItem(Document document, DocumentStatus status, String author);
}
