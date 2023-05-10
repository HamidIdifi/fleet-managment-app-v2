package com.fleetManagement.app.service.impl;

import com.fleetManagement.app.commun.CoreConstant;
import com.fleetManagement.app.entities.GenericEnum;
import com.fleetManagement.app.entities.ManagerRole;
import com.fleetManagement.app.exception.ElementAlreadyExistException;
import com.fleetManagement.app.exception.ElementNotFoundException;
import com.fleetManagement.app.repositories.GenericRepository;
import com.fleetManagement.app.repositories.ManagerRoleRepository;
import com.fleetManagement.app.service.ManagerRoleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ManagerRoleServiceImpl extends GenericServiceImpl<ManagerRole> implements ManagerRoleService {
    private final ManagerRoleRepository managerRoleRepository;

    public ManagerRoleServiceImpl(GenericRepository<ManagerRole> genericRepository, ModelMapper modelMapper, ManagerRoleRepository managerRoleRepository) {
        super(genericRepository, modelMapper);
        this.managerRoleRepository = managerRoleRepository;
    }

    @Override
    public ManagerRole findByName(GenericEnum.RoleName roleName) {
        Optional<ManagerRole> userRole = managerRoleRepository.findByName(roleName);
        if (userRole.isPresent()) return userRole.get();
        throw new ElementNotFoundException(null, new ElementNotFoundException(), CoreConstant.Exception.NOT_FOUND, new Object[]{roleName});

    }

    public ManagerRole save(ManagerRole entity) throws ElementAlreadyExistException {
        Optional<ManagerRole> userRole = managerRoleRepository.findByName(GenericEnum.RoleName.MANAGER);
        return userRole.orElseGet(() -> managerRoleRepository.save(new ManagerRole()));
    }

}
