package com.fleetManagement.app.dto;

import com.fleetManagement.app.entities.GenericEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceDto extends DocumentationsDto {
    private GenericEnum.InsuranceCompany insuranceCompany;
}
