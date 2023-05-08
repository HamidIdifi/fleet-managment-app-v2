package com.fleetManagement.app.service;

import com.fleetManagement.app.entities.GenericEnum;
import com.fleetManagement.app.entities.Vehicle;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface VehicleService extends GenericService<Vehicle> {
    //void addDocumentationsToVehicle(String vehicleRegister,String documentationRegister);
    List<Vehicle> getVehiclesByLicenseType(GenericEnum.LicenseType licenseType);
    boolean isVehicleExistWithLicenseType(Long vehicleId,GenericEnum.LicenseType licenseType);
    List<Vehicle> findAvailableVehicles(int page, int size);
    boolean hasVehicleTravelAfterDate(Vehicle vehicle, Date dateOfStart,Date dateOfEnding);
    Long countAvailableVehicles();
}
