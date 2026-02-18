package ru.gareev.documentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gareev.documentservice.entity.ActivityFeedItem;

public interface ActivityFeedRepository extends JpaRepository<ActivityFeedItem,Long> {
}
