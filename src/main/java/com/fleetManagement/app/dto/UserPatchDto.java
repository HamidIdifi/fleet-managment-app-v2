package com.fleetManagement.app.dto;

import com.fleetManagement.app.entities.GenericEnum;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPatchDto {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private LocalDate birthDay;
    private String bio;
    private GenericEnum.Gender gender;
    private AddressDto address;
    private PhoneNumberDto phoneNumber;
}
