package com.fleetManagement.app.dto;

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
public class ConsumptionDocumentDto extends GenericDto {
    private String driverName;
    private GenericEnum.CarBrand vehicleName;
    private Date travelDate;
    private GenericEnum.Energy vehicleEnergy;
    private float distanceTraveled;
    private float fuelPurchased;//plein du réservoir en litres
    private float amountOfFuelConsumed;
}
