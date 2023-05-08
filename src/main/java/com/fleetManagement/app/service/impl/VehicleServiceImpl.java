package com.fleetManagement.app.service.impl;

import com.fleetManagement.app.entities.GenericEnum;
import com.fleetManagement.app.entities.Vehicle;
import com.fleetManagement.app.repositories.GenericRepository;
import com.fleetManagement.app.repositories.VehicleRepository;
import com.fleetManagement.app.service.VehicleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.fleetManagement.app.commun.CoreConstant.Pagination.DEFAULT_PAGE_NUMBER;
import static com.fleetManagement.app.commun.CoreConstant.Pagination.DEFAULT_PAGE_SIZE;

@Service
public class VehicleServiceImpl extends GenericServiceImpl<Vehicle> implements VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    public VehicleServiceImpl(GenericRepository<Vehicle> genericRepository, ModelMapper modelMapper) {
        super(genericRepository, modelMapper);
    }

    @Override
    public List<Vehicle> getVehiclesByLicenseType(GenericEnum.LicenseType licenseType) {

        return vehicleRepository.findByLicenseTypesContaining(licenseType);
    }

    @Override
    public boolean isVehicleExistWithLicenseType(Long vehicleId, GenericEnum.LicenseType licenseType) {
        List<Vehicle> supportedVehicles = getVehiclesByLicenseType(licenseType);
        return supportedVehicles.stream().anyMatch(vehicle -> vehicle.getId().equals(vehicleId));

    }


    @Override
    public List<Vehicle> findAvailableVehicles(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Vehicle> allVehicles = vehicleRepository.findAll(pageable).getContent();
        Date currentDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        return allVehicles.stream()
                .filter(vehicle ->
                        vehicle.getTravels().stream()
                                .allMatch(travel ->
                                        travel.getDateOfEnding().before(currentDate) || travel.getDateOfStart().after(currentDate)
                                )
                )
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasVehicleTravelAfterDate(Vehicle vehicle, Date dateOfStart, Date dateOfEnding) {
        return vehicle.getTravels().stream().anyMatch(travel -> travel.getDateOfEnding().after(dateOfStart) && travel.getDateOfStart().before(dateOfEnding));
    }

    @Override
    public Long countAvailableVehicles() {
        return (long) findAvailableVehicles(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE + 1).size();
    }
}
