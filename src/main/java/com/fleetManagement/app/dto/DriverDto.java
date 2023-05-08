package com.fleetManagement.app.dto;

import com.fleetManagement.app.entities.GenericEnum;
import com.fleetManagement.app.entities.License;
import com.fleetManagement.app.entities.Travel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverDto extends GenericDto{
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String cin;
    private GenericEnum.Status status;
    private List<License> licenses;
    private Set<Travel> travels;
}
