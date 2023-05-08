package com.fleetManagement.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fleetManagement.app.entities.GenericEnum.LicenseType;
import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
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
    @JsonIgnore
    private Driver driver;
}
