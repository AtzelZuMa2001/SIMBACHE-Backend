package com.korealm.simbache.dtos.potholes;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporterCitizenDto {
    private String firstName;
    private String lastName;
    private String email;
    private Long phoneNumber;
}

