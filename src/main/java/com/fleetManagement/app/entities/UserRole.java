package com.fleetManagement.app.entities;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Builder
@Getter @Setter
public class UserRole extends AppUserRole {

    public UserRole() {
        setName(GenericEnum.RoleName.USER);
    }
}
