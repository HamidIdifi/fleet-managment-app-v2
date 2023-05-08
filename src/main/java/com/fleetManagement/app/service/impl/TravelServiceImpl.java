package com.fleetManagement.app.service.impl;

import com.fleetManagement.app.entities.Travel;
import com.fleetManagement.app.repositories.GenericRepository;
import com.fleetManagement.app.service.TravelService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class TravelServiceImpl extends GenericServiceImpl<Travel> implements TravelService {

    public TravelServiceImpl(GenericRepository<Travel> genericRepository, ModelMapper modelMapper) {
        super(genericRepository, modelMapper);
    }
}
