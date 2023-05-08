package com.fleetManagement.app.dto;


import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
