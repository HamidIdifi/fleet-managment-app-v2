package com.fleetManagement.app.dto;

import com.fleetManagement.app.commun.CoreConstant;
import lombok.*;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {

    private String country;
    private String city;

    @Pattern(regexp = CoreConstant.Validation.POSTAL_CODE_REGEX)
    private String postalCode;


}
