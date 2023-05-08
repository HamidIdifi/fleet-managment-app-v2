package com.fleetManagement.app.service.impl;

import com.fleetManagement.app.commun.CoreConstant;
import com.fleetManagement.app.entities.AdminRole;
import com.fleetManagement.app.entities.GenericEnum;
import com.fleetManagement.app.exception.ElementAlreadyExistException;
import com.fleetManagement.app.exception.ElementNotFoundException;
import com.fleetManagement.app.repositories.AdminRoleRepository;
import com.fleetManagement.app.repositories.GenericRepository;
import com.fleetManagement.app.service.AdminRoleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminRoleServiceImpl extends GenericServiceImpl<AdminRole> implements AdminRoleService {
    private final AdminRoleRepository adminRoleRepository;

    public AdminRoleServiceImpl(GenericRepository<AdminRole> genericRepository, ModelMapper modelMapper, AdminRoleRepository adminRoleRepository) {
        super(genericRepository, modelMapper);
        this.adminRoleRepository = adminRoleRepository;
    }


    @Override
    public AdminRole findByName(GenericEnum.RoleName roleName) {
        Optional<AdminRole> userRole = adminRoleRepository.findByName(roleName);
        if (userRole.isPresent()) return userRole.get();
        throw new ElementNotFoundException(null, new ElementNotFoundException(), CoreConstant.Exception.NOT_FOUND, new Object[]{roleName});

    }

    public AdminRole save(AdminRole entity) throws ElementAlreadyExistException {
        Optional<AdminRole> userRole = adminRoleRepository.findByName(GenericEnum.RoleName.ADMIN);
        return userRole.orElseGet(() -> adminRoleRepository.save(new AdminRole()));
    }
}
