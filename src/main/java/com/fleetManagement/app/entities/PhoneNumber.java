package com.fleetManagement.app.entities;


import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneNumber {

    @Enumerated(EnumType.STRING)
    private GenericEnum.Region region;
    private String phoneNumber;

}
