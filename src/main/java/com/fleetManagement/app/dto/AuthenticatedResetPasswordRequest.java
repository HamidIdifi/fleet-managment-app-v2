package com.fleetManagement.app.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticatedResetPasswordRequest {

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;


}
