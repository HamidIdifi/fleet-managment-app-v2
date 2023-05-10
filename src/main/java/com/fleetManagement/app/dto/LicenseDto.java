package com.fleetManagement.app.dto;

import com.fleetManagement.app.entities.Driver;
import com.fleetManagement.app.entities.GenericEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LicenseDto extends DocumentationsDto {
    private Date dateOfLicense;
    private GenericEnum.LicenseType type;
}
