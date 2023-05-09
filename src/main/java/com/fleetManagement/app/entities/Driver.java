package com.fleetManagement.app.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

import static com.fleetManagement.app.entities.GenericEnum.Status;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "DRIVERS")
public class Driver extends GenericEntity implements Serializable {
    @Column(name = "FIRST_NAME", length = 20)
    private String firstName;
    @Column(name = "LAST_NAME", length = 20)
    private String lastName;
    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_OF_BIRTH")
    private Date dateOfBirth;
    @Column(name = "CIN", length = 20)
    private String cin;
    @Enumerated(EnumType.STRING)
    private Status status;
    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<License> licenses = new ArrayList<>();
    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Travel> travels = new ArrayList<>();

}
