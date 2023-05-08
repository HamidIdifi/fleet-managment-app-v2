package com.fleetManagement.app.dto;

import com.fleetManagement.app.entities.GenericEnum;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneNumberDto {

    private GenericEnum.Region region;
    private String phoneNumber;
}
