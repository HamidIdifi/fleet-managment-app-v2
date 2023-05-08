package com.fleetManagement.app.dto;


import com.fleetManagement.app.commun.CoreConstant;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginDto {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Length(min = CoreConstant.Validation.PASSWORD_SIZE_MIN, max = CoreConstant.Validation.PASSWORD_SIZE_MAX)
    private String password;
}
