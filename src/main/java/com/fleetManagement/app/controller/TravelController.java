package com.fleetManagement.app.controller;

import com.fleetManagement.app.commun.CoreConstant;
import com.fleetManagement.app.dto.TravelDto;
import com.fleetManagement.app.entities.Driver;
import com.fleetManagement.app.entities.Travel;
import com.fleetManagement.app.entities.Vehicle;
import com.fleetManagement.app.exception.BusinessException;
import com.fleetManagement.app.service.DriverService;
import com.fleetManagement.app.service.TravelService;
import com.fleetManagement.app.service.UserService;
import com.fleetManagement.app.service.VehicleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/travels")
@Validated
@AllArgsConstructor
public class TravelController extends GenericController<Travel, TravelDto> {
    private final UserService userService;
    private final TravelService travelService;
    private final DriverService driverService;
    private final VehicleService vehicleService;

    private Travel createTravel(Travel travelEntity) {
        return travelService.save(travelEntity);
    }

    @PostMapping("/add-travel")
    public ResponseEntity<TravelDto> save(@RequestBody TravelDto travelDto, @RequestParam Long vehicleId, @RequestParam Long driverId) {
        Travel travelEntity = convertToEntity(travelDto);
        Driver driverEntity = driverService.findById(driverId);
        Vehicle vehicleEntity = vehicleService.findById(vehicleId);
        if (vehicleService.hasVehicleTravelAfterDate(vehicleEntity, travelDto.getDateOfStart(), travelDto.getDateOfEnding())) {
            throw new BusinessException(new BusinessException(), CoreConstant.Exception.VEHICLE_ALREADY_IN_USE, null);
        }
        if (driverService.hasDriverTravelAfterDate(driverEntity, travelDto.getDateOfStart(), travelDto.getDateOfEnding())) {
            throw new BusinessException(new BusinessException(), CoreConstant.Exception.DRIVER_ALREADY_IN_USE, null);
        }
        if (!vehicleService.isVehicleExistWithLicenseType(vehicleEntity.getId(), travelDto.getLicenseType())) {
            throw new BusinessException(new BusinessException(), CoreConstant.Exception.VEHICLE_LICENSE_TYPE_NOT_MATCH, null);
        }
        if (!driverService.isDriverExistWithLicenseType(driverEntity.getId(), travelDto.getLicenseType())) {
            throw new BusinessException(new BusinessException(), CoreConstant.Exception.DRIVER_LICENSE_TYPE_NOT_MATCH, null);
        }
        driverEntity.addTravel(travelEntity);
        vehicleEntity.addTravel(travelEntity);
        Travel savedTravel = createTravel(travelEntity);
        TravelDto savedTravelDto = convertToDto(savedTravel);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTravelDto);
    }

}
