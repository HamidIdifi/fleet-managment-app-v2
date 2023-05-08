package com.fleetManagement.app.service.impl;

import com.fleetManagement.app.commun.CoreConstant;
import com.fleetManagement.app.entities.GenericEnum;
import com.fleetManagement.app.entities.UserRole;
import com.fleetManagement.app.exception.ElementAlreadyExistException;
import com.fleetManagement.app.exception.ElementNotFoundException;
import com.fleetManagement.app.repositories.GenericRepository;
import com.fleetManagement.app.repositories.UserRoleRepository;
import com.fleetManagement.app.service.UserRoleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRoleServiceImpl extends GenericServiceImpl<UserRole> implements UserRoleService {
    private final UserRoleRepository userRoleRepository;


    public UserRoleServiceImpl(GenericRepository<UserRole> genericRepository, ModelMapper modelMapper, UserRoleRepository userRoleRepository) {
        super(genericRepository, modelMapper);
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserRole findByName(GenericEnum.RoleName roleName) {
        Optional<UserRole> userRole = userRoleRepository.findByName(roleName);
        if (userRole.isPresent()) return userRole.get();
        throw new ElementNotFoundException(null, new ElementNotFoundException(), CoreConstant.Exception.NOT_FOUND, new Object[]{roleName});
    }

    public UserRole save(UserRole entity) throws ElementAlreadyExistException {
        Optional<UserRole> userRole = userRoleRepository.findByName(GenericEnum.RoleName.USER);
        return userRole.orElseGet(() -> userRoleRepository.save(new UserRole()));
    }
}
