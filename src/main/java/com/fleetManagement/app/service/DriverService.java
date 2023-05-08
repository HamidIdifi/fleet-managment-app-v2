package com.fleetManagement.app.service;

import com.fleetManagement.app.entities.Driver;
import com.fleetManagement.app.entities.GenericEnum;
import com.fleetManagement.app.entities.Travel;
import com.fleetManagement.app.entities.Vehicle;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface DriverService extends GenericService<Driver> {
    List<Driver> getDriversByLicenseType(GenericEnum.LicenseType licenseType);
    boolean isDriverExistWithLicenseType(Long driverId,GenericEnum.LicenseType licenseType);
    List<Driver> findAvailableDrivers(int page, int size);
    boolean hasDriverTravelAfterDate(Driver driver, Date dateOfStart,Date dateOfEnding);
    Long countAvailableDrivers();
}
