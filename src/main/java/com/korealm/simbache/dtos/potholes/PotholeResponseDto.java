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
    private ReporterCitizenDto reporterCitizen; // nullable
    private RegisteredUserDto registeredByUser; // not null
    private LocationDetailsDto location; // not null
    private CategoryDetailsDto category; // not null
    private Short statusId; // not null
    private String photoUrl; // nullable
    private LocalDateTime dateReported; // not null
    private LocalDateTime dateValidated; // nullable
    private LocalDateTime dateClosed; // nullable
    private boolean isActive;
}
