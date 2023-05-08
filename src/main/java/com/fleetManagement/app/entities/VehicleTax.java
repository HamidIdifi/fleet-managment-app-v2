package com.fleetManagement.app.entities;

import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("VEHICLE_TAX")
public class VehicleTax extends Documentations implements Serializable {
    private int cf;
}
