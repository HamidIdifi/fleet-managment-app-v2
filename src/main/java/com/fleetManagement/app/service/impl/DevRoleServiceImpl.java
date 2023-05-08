package com.fleetManagement.app.service.impl;

import com.fleetManagement.app.commun.CoreConstant;
import com.fleetManagement.app.entities.DevRole;
import com.fleetManagement.app.entities.GenericEnum;
import com.fleetManagement.app.exception.ElementAlreadyExistException;
import com.fleetManagement.app.exception.ElementNotFoundException;
import com.fleetManagement.app.repositories.DevRoleRepository;
import com.fleetManagement.app.repositories.GenericRepository;
import com.fleetManagement.app.service.DevRoleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DevRoleServiceImpl extends GenericServiceImpl<DevRole> implements DevRoleService {
    private final DevRoleRepository devRoleRepository;

    public DevRoleServiceImpl(GenericRepository<DevRole> genericRepository, ModelMapper modelMapper, DevRoleRepository devRoleRepository) {
        super(genericRepository, modelMapper);
        this.devRoleRepository = devRoleRepository;
    }


    @Override
    public DevRole findByName(GenericEnum.RoleName roleName) {
        Optional<DevRole> devRole = devRoleRepository.findByName(roleName);
        if (devRole.isPresent()) return devRole.get();
        throw new ElementNotFoundException(null, new ElementNotFoundException(), CoreConstant.Exception.NOT_FOUND, new Object[]{roleName});

    }

    public DevRole save(DevRole entity) throws ElementAlreadyExistException {
        Optional<DevRole> devRole = devRoleRepository.findByName(GenericEnum.RoleName.DEV);
        return devRole.orElseGet(() -> devRoleRepository.save(new DevRole()));
    }
}
