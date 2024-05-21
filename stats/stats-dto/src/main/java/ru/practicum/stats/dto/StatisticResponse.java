package ru.practicum.stats.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class StatisticResponse {
    private String app;
    private String uri;
    private long hits;
}
