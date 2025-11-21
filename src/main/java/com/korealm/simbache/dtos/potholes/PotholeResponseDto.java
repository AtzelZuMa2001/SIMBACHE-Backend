package com.korealm.simbache.dtos.potholes;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PotholeResponseDto {
    private Long potholeId;
    private Long reportByCitizenId; // nullable
    private Long registeredByUserId; // not null
    private Long locationId; // not null
    private Short categoryId; // not null
    private Short statusId; // not null
    private String photoUrl; // nullable
    private LocalDateTime dateReported; // not null
    private LocalDateTime dateValidated; // nullable
    private LocalDateTime dateClosed; // nullable
    private boolean isActive;
}
