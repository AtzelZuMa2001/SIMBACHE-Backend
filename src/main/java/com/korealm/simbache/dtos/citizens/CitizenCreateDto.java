package com.korealm.simbache.dtos.citizens;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitizenCreateDto {

    @NotBlank(message = "El nombre es requerido.")
    @Size(max = 255, message = "El nombre no puede exceder 255 caracteres.")
    private String firstName;

    @Size(max = 255, message = "El segundo nombre no puede exceder 255 caracteres.")
    private String middleName;

    @NotBlank(message = "El apellido paterno es requerido.")
    @Size(max = 255, message = "El apellido paterno no puede exceder 255 caracteres.")
    private String lastName;

    @Size(max = 255, message = "El apellido materno no puede exceder 255 caracteres.")
    private String secondLastName;

    @NotBlank(message = "El correo electrónico es requerido.")
    @Email(message = "El correo electrónico debe tener un formato válido.")
    private String email;

    private Long phoneNumber;

    @NotNull(message = "La ubicación de registro es requerida.")
    private Long registeredLocationId;
}