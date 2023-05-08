package com.fleetManagement.app.service;

import com.fleetManagement.app.entities.GenericEnum;
import com.fleetManagement.app.entities.UserRole;

public interface UserRoleService extends GenericService<UserRole> {


    UserRole findByName(GenericEnum.RoleName roleName);


}
