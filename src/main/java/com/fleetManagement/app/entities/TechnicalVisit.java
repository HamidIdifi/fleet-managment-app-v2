package com.fleetManagement.app.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("TECHNICAL_VISIT")
public class TechnicalVisit extends Documentations implements Serializable {
    private boolean inGoodCondition;
    @OneToOne
    private Vehicle vehicle;
}
