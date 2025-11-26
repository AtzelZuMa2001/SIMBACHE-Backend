package com.korealm.simbache.dtos.potholes;

import com.korealm.simbache.models.PotholeCategory;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PotholeCategoryResponseDto {

    private Short categoryId;
    private String name;
    private String description;
    private int priorityLevel;

    public PotholeCategoryResponseDto(PotholeCategory entity) {
        this.categoryId = entity.getCategoryId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.priorityLevel = entity.getPriorityLevel();
    }
}