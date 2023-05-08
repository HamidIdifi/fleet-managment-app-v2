package com.fleetManagement.app.service;

import com.fleetManagement.app.entities.DevRole;
import com.fleetManagement.app.entities.GenericEnum;

public interface DevRoleService extends GenericService<DevRole> {

    public DevRole findByName(GenericEnum.RoleName roleName);
}
