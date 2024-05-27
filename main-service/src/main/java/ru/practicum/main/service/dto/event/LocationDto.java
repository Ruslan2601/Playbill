package ru.practicum.main.service.dto.event;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class LocationDto {
    private Float lat;
    private Float lon;
}
