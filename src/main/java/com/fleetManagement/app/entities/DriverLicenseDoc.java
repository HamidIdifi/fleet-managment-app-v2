package com.fleetManagement.app.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("DRIVER_LICENSE")
public class DriverLicenseDoc extends Documentations implements Serializable {
    @OneToMany(mappedBy = "driverLicense", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<License> licenses = new ArrayList<>();
    @OneToOne
    private Driver driver;
}
