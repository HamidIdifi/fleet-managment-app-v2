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
@DiscriminatorValue("TECHNICAL_VISIT")
public class TechnicalVisit extends Documentations implements Serializable {
    private boolean inGoodCondition;
}
