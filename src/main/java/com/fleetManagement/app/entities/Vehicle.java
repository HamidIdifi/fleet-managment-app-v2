package com.fleetManagement.app.entities;

import com.fleetManagement.app.entities.GenericEnum.CarBrand;
import com.fleetManagement.app.entities.GenericEnum.Energy;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "VEHICLE")

public class Vehicle extends GenericEntity implements Serializable {
    @Column(name = "CAR_BRAND")
    @Enumerated(EnumType.STRING)
    private CarBrand carBrand;
    @Column(name = "CAR_ENERGY")
    @Enumerated(EnumType.STRING)
    private Energy energy;
    @Column(name = "FISCAL_POWER")
    private int fiscalPower;
    @Column(name = "EXPIRY_DATE")
    private Date expiryDate;
    @Column(name = "MC_DATE")
    @Temporal(TemporalType.DATE)
    private Date dateOfMC;//mise en circulation
    @Column(name = "VEHICLE_USAGE", length = 50)
    private String vehicleUsage;
    @OneToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "vehicleTax_id")
    private VehicleTax vehicleTax;
    @OneToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "vehicleVisit_id")
    private TechnicalVisit technicalVisit;
    @OneToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "insurance_id")
    private Insurance insurance;
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Travel> travels = new ArrayList<>();
    @ElementCollection(targetClass = GenericEnum.LicenseType.class)
    @Enumerated(EnumType.STRING)
    private Set<GenericEnum.LicenseType> licenseTypes;
}
