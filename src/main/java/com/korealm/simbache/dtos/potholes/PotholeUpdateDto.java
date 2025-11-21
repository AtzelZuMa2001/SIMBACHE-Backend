package com.korealm.simbache.dtos.potholes;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PotholeUpdateDto {
    private Long reportByCitizenId; // nullable
    private Long locationId; // nullable
    private Short categoryId; // nullable
    private Short statusId; // nullable
    private String photoUrl; // nullable
    private LocalDateTime dateValidated; // nullable
    private LocalDateTime dateClosed; // nullable
    private Boolean isActive; // nullable
}
