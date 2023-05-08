package com.fleetManagement.app.service;

import com.fleetManagement.app.entities.AdminRole;
import com.fleetManagement.app.entities.GenericEnum;

public interface AdminRoleService extends GenericService<AdminRole> {

    public AdminRole findByName(GenericEnum.RoleName roleName);
}
