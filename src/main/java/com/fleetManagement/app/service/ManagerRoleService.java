package com.fleetManagement.app.service;

import com.fleetManagement.app.entities.GenericEnum;
import com.fleetManagement.app.entities.ManagerRole;

public interface ManagerRoleService extends GenericService<ManagerRole>{
    public ManagerRole findByName(GenericEnum.RoleName roleName);
}
