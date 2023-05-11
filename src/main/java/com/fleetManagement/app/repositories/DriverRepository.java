package com.fleetManagement.app.repositories;

import com.fleetManagement.app.entities.Driver;
import com.fleetManagement.app.entities.GenericEnum;

import java.util.List;

public interface DriverRepository extends GenericRepository<Driver> {
    List<Driver> findByDriverLicense_LicensesType(GenericEnum.LicenseType licenseType);
}
