package com.fleetManagement.app.repositories;

import com.fleetManagement.app.entities.Driver;
import com.fleetManagement.app.entities.GenericEnum;
import com.fleetManagement.app.entities.Travel;
import com.fleetManagement.app.entities.Vehicle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface VehicleRepository extends GenericRepository<Vehicle>{
    List<Vehicle> findByLicenseTypesContaining(GenericEnum.LicenseType licenseType);
}
