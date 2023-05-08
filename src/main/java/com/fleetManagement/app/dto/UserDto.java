package com.fleetManagement.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fleetManagement.app.commun.CoreConstant;
import com.fleetManagement.app.entities.GenericEnum;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto extends GenericDto {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Email
    @NotNull
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Length(min = CoreConstant.Validation.PASSWORD_SIZE_MIN, max = CoreConstant.Validation.PASSWORD_SIZE_MAX)
    @NotBlank
    private String password;
    private LocalDate birthDay;

    private String profilePicture;

    private String verificationCode;

    private boolean profileCompleted;

    private String bio;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean enabled;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String RefreshTokenId;
    @NotNull
    private GenericEnum.Gender gender;
    private AddressDto address;

    private PhoneNumberDto phoneNumber;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<AppUserRoleDto> roles;

}
