package com.korealm.simbache.dtos.potholes;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisteredUserDto {
    private String firstName;
    private String lastName;
    private String roleName;
}
