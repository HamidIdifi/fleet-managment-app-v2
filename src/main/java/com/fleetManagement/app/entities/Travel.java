package com.fleetManagement.app.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.fleetManagement.app.entities.GenericEnum.LicenseType;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TRAVELS")

public class Travel extends GenericEntity implements Serializable {
    @Column(name = "DATE_OF_START")
    @Temporal(TemporalType.DATE)
    private Date dateOfStart;
    @Column(name = "DATE_OF_ENDING")
    @Temporal(TemporalType.DATE)
    private Date dateOfEnding;
    @Enumerated(EnumType.STRING)
    private LicenseType licenseType;
    @ManyToOne
    @JoinColumn(name = "DRIVER_ID")
    private Driver driver;
    @ManyToOne
    @JoinColumn(name = "VEHICLE_ID")
    private Vehicle vehicle;
    @OneToMany(mappedBy = "travel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConsumptionDocument> docs = new ArrayList<>();
}
