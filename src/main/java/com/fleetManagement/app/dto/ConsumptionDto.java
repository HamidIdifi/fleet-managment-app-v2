package com.fleetManagement.app.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConsumptionDto implements Serializable {
    private float distanceTraveled;
    private float fuelPurchased;//plein du réservoir en litres
    private Date travelDate;
}
