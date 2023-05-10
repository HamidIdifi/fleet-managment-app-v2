package com.fleetManagement.app.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsumptionDocument extends GenericEntity {
    private String driverName;
    private GenericEnum.CarBrand vehicleName;
    @Temporal(TemporalType.DATE)
    private Date travelDate;
    private GenericEnum.Energy vehicleEnergy;
    private float distanceTraveled;
    private float fuelPurchased;//plein du réservoir en litres
    private float amountOfFuelConsumed;
    @ManyToOne
    @JoinColumn(name = "TRAVEL_ID")
    private Travel travel;

    public void setAmount_of_fuel_consumed(float nbrKM, float tankFull) {
        this.amountOfFuelConsumed = (tankFull * 100 / nbrKM);
    }

}
