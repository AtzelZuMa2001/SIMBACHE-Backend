package com.korealm.simbache.dtos.citizens;

import com.korealm.simbache.models.Citizens;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitizenLookupDto {

    private Long citizenId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String secondLastName;
    private String fullName;
    private String email;
    private Long phoneNumber;

    public CitizenLookupDto(Citizens entity) {
        this.citizenId = entity.getCitizenId();
        this.firstName = entity.getFirstName();
        this.middleName = entity.getMiddleName();
        this.lastName = entity.getLastName();
        this.secondLastName = entity.getSecondLastName();
        this.email = entity.getEmail();
        this.phoneNumber = entity.getPhoneNumber();
        this.fullName = buildFullName(entity);
    }

    private String buildFullName(Citizens entity) {
        StringBuilder sb = new StringBuilder();
        sb.append(entity.getFirstName());

        if (entity.getMiddleName() != null && !entity.getMiddleName().isBlank()) {
            sb.append(" ").append(entity.getMiddleName());
        }

        sb.append(" ").append(entity.getLastName());

        if (entity.getSecondLastName() != null && !entity.getSecondLastName().isBlank()) {
            sb.append(" ").append(entity.getSecondLastName());
        }

        return sb.toString();
    }
}