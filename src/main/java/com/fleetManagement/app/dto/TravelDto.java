package com.fleetManagement.app.dto;

import com.fleetManagement.app.entities.ConsumptionDocument;
import com.fleetManagement.app.entities.GenericEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TravelDto extends GenericDto {
    private Date dateOfStart;
    private Date dateOfEnding;
    private GenericEnum.LicenseType licenseType;
    private List<ConsumptionDocumentDto> docs;
}
