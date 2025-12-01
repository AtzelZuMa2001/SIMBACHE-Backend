package com.korealm.simbache.dtos.citizens;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitizenUpdateDto {

    @Size(max = 255, message = "El nombre no puede exceder 255 caracteres.")
    private String firstName;

    @Size(max = 255, message = "El segundo nombre no puede exceder 255 caracteres.")
    private String middleName;

    @Size(max = 255, message = "El apellido paterno no puede exceder 255 caracteres.")
    private String lastName;

    @Size(max = 255, message = "El apellido materno no puede exceder 255 caracteres.")
    private String secondLastName;

    @Email(message = "El correo electrónico debe tener un formato válido.")
    private String email;

    private Long phoneNumber;

    private Long registeredLocationId;
}