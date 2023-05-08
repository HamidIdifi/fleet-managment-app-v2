package com.fleetManagement.app.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@Builder
public class DevRole extends AppUserRole {

    public DevRole() {
        setName(GenericEnum.RoleName.DEV);
    }
}
