package com.fleetManagement.app.dto;

import com.fleetManagement.app.entities.*;
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
public class VehiclePatchDto {

    private GenericEnum.CarBrand carBrand;
    private GenericEnum.Energy energy;
    private int fiscalPower;
    private Date expiryDate;
    private Date dateOfMC;//mise en circulation
    private String vehicleUsage;
    private Set<GenericEnum.LicenseType> licenseTypes;
}
