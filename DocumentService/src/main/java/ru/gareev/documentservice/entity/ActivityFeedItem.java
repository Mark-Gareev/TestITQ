package ru.gareev.documentservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "activity_feed")
public class ActivityFeedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    @Setter(AccessLevel.NONE)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private Document document;

    /**
     * @see Document in current package
     */
    @Column(name="actor")
    private String actor;

    @Column(name="activity_date_time")
    private LocalDateTime activityDateTime;

    @Enumerated(EnumType.STRING)
    @Column(name="action_type")
    private ActionType actionType;

}
