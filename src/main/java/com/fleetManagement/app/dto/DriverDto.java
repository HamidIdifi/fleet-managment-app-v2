package com.fleetManagement.app.dto;

import com.fleetManagement.app.entities.GenericEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverDto extends GenericDto {
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String cin;
    private GenericEnum.Status status;
    private DriverLicenseDocDto driverLicense;
    private Set<TravelDto> travels;
}
