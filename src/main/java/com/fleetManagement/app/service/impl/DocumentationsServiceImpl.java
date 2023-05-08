package com.fleetManagement.app.service.impl;

import com.fleetManagement.app.entities.Documentations;
import com.fleetManagement.app.repositories.GenericRepository;
import com.fleetManagement.app.service.DocumentationsService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class DocumentationsServiceImpl extends GenericServiceImpl<Documentations> implements DocumentationsService {

    public DocumentationsServiceImpl(GenericRepository<Documentations> genericRepository, ModelMapper modelMapper) {
        super(genericRepository, modelMapper);
    }
}
