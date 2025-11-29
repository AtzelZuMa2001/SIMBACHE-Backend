package com.korealm.simbache.dtos.potholes;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporterCitizenDto {
    private Long citizenId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String secondLastName;
    private String email;
    private Long phoneNumber;
}

