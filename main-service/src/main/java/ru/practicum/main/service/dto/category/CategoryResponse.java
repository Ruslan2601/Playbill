package ru.practicum.main.service.dto.category;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class CategoryResponse {
    private Long id;
    private String name;
}
