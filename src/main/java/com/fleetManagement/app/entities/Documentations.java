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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", length = 20)
public class Documentations extends GenericEntity {
    @Temporal(TemporalType.DATE)
    private Date dateOfStart;
    @Temporal(TemporalType.DATE)
    private Date dateOfEnding;
    private boolean valid;
}
