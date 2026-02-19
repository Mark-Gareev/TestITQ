package ru.gareev.documentservice.api.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FullDocumentDto{
    private DocumentDto documentDto;
    private List<ActivityFeedItemDto> activity;
}
