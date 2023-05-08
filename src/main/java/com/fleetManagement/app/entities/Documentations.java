package com.fleetManagement.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", length = 20)
public class Documentations extends GenericEntity {
    @Temporal(TemporalType.DATE)
    private Date dateOfStart;
    @Temporal(TemporalType.DATE)
    private Date dateOfEnding;
    private boolean valid;
    @OneToOne
    @JsonIgnore
    private Vehicle vehicle;
}
