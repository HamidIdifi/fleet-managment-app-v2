package com.fleetManagement.app.dto;

import com.fleetManagement.app.entities.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentationsDto extends GenericDto{
    private Date dateOfStart;
    private Date dateOfEnding;
    private boolean valid;
    private Vehicle vehicle;
}
