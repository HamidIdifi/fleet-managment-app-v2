package com.fleetManagement.app.entities;

import com.fleetManagement.app.entities.GenericEnum.LicenseType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "LICENSES")

public class License extends GenericEntity implements Serializable {
    @Column(name = "DATE_OF_LICENSE")
    @Temporal(TemporalType.DATE)
    private Date dateOfLicense;
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE_OF_LICENSE")
    private LicenseType type;
    @ManyToOne
    @JoinColumn(name = "DRIVER_ID")
    private Driver driver;
}
