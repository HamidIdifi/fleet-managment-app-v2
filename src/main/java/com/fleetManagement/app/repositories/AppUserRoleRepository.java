package com.fleetManagement.app.repositories;

import com.fleetManagement.app.entities.AppUserRole;
import com.fleetManagement.app.entities.GenericEnum;

import java.util.Optional;

public interface AppUserRoleRepository<R extends AppUserRole> extends GenericRepository<R> {

    public Optional<R> findByName(GenericEnum.RoleName roleName);


}
