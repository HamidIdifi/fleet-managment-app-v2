package com.fleetManagement.app.entities;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter @Setter
@Builder
public class AdminRole extends AppUserRole {

    public AdminRole() {
        setName(GenericEnum.RoleName.ADMIN);
    }
}
