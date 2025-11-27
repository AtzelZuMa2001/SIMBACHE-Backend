package com.korealm.simbache.dtos.potholes;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PotholeCreateDto {
    // Optional: the citizens who reported it in case it was a citizens. If it was reported by an employee or whatever, then it is null.
    private Long reporterCitizenId;

    @NotNull
    private Long locationId;

    @NotNull
    private Short categoryId;

    @NotNull
    private Short statusId;

    private String photoUrl;
    private LocalDateTime dateReported; // If null, the backend will set now
}
