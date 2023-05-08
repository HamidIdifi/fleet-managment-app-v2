package com.fleetManagement.app.service.impl;

import com.fleetManagement.app.entities.License;
import com.fleetManagement.app.repositories.GenericRepository;
import com.fleetManagement.app.service.LicenseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LicenseServiceImpl extends GenericServiceImpl<License> implements LicenseService {

    public LicenseServiceImpl(GenericRepository<License> genericRepository, ModelMapper modelMapper) {
        super(genericRepository, modelMapper);
    }
}
