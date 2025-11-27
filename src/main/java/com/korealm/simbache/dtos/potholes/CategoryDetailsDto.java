package com.korealm.simbache.dtos.potholes;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDetailsDto {
    private Short categoryId;
    private String categoryName;
    private String description;
    private int priorityLevel;
}
