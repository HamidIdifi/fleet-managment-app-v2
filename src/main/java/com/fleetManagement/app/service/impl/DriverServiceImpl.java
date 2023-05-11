package com.fleetManagement.app.service.impl;

import com.fleetManagement.app.entities.Driver;
import com.fleetManagement.app.entities.GenericEnum;
import com.fleetManagement.app.repositories.DriverRepository;
import com.fleetManagement.app.repositories.GenericRepository;
import com.fleetManagement.app.service.DriverService;
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
public class DriverServiceImpl extends GenericServiceImpl<Driver> implements DriverService {
    @Autowired
    DriverRepository driverRepository;

    public DriverServiceImpl(GenericRepository<Driver> genericRepository, ModelMapper modelMapper) {
        super(genericRepository, modelMapper);
    }

    @Override
    public List<Driver> getDriversByLicenseType(GenericEnum.LicenseType licenseType) {

        return driverRepository.findByDriverLicense_LicensesType(licenseType);
    }

    @Override
    public boolean isDriverExistWithLicenseType(Long driverId, GenericEnum.LicenseType licenseType) {
        List<Driver> supportedDrivers = getDriversByLicenseType(licenseType);
        return supportedDrivers.stream().anyMatch(driver -> driver.getId().equals(driverId));
    }


    @Override
    public List<Driver> findAvailableDrivers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Driver> allDrivers = driverRepository.findAll(pageable).getContent();
        Date currentDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        return allDrivers.stream()
                .filter(driver ->
                        driver.getTravels().stream()
                                .allMatch(travel ->
                                        travel.getDateOfEnding().before(currentDate) || travel.getDateOfStart().after(currentDate)
                                )
                )
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasDriverTravelAfterDate(Driver driver, Date dateOfStart, Date dateOfEnding) {
        return driver.getTravels().stream().anyMatch(travel -> travel.getDateOfEnding().after(dateOfStart) && travel.getDateOfStart().before(dateOfEnding));
    }

    @Override
    public Long countAvailableDrivers() {
        return (long) findAvailableDrivers(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE + 1).size();
    }
}
