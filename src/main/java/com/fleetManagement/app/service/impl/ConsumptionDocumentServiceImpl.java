package com.fleetManagement.app.service.impl;

import com.fleetManagement.app.entities.ConsumptionDocument;
import com.fleetManagement.app.repositories.GenericRepository;
import com.fleetManagement.app.service.ConsumptionDocumentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ConsumptionDocumentServiceImpl extends GenericServiceImpl<ConsumptionDocument> implements ConsumptionDocumentService {
    public ConsumptionDocumentServiceImpl(GenericRepository<ConsumptionDocument> genericRepository, ModelMapper modelMapper) {
        super(genericRepository, modelMapper);
    }
}
