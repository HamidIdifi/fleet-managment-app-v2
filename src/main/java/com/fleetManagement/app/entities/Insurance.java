package com.fleetManagement.app.entities;

import com.fleetManagement.app.entities.GenericEnum.InsuranceCompany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("INSURANCE")
public class Insurance extends Documentations implements Serializable {
    @Enumerated(EnumType.STRING)
    private InsuranceCompany insuranceCompany;
    @OneToOne
    private Vehicle vehicle;
}
