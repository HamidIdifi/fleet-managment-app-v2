package com.fleetManagement.app.controller;

import com.fleetManagement.app.dto.VehicleDto;
import com.fleetManagement.app.dto.VehiclePatchDto;
import com.fleetManagement.app.entities.Insurance;
import com.fleetManagement.app.entities.TechnicalVisit;
import com.fleetManagement.app.entities.Vehicle;
import com.fleetManagement.app.entities.VehicleTax;
import com.fleetManagement.app.exception.ResourceDeletionNotAllowedException;
import com.fleetManagement.app.service.DocumentationsService;
import com.fleetManagement.app.service.UserService;
import com.fleetManagement.app.service.VehicleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.fleetManagement.app.commun.CoreConstant.Exception.AUTHORIZATION_RESOURCE_DELETION_NOT_ALLOWED;
import static com.fleetManagement.app.commun.CoreConstant.Pagination.DEFAULT_PAGE_NUMBER;
import static com.fleetManagement.app.commun.CoreConstant.Pagination.DEFAULT_PAGE_SIZE;

@RestController
@RequestMapping("/api/vehicles")
@Validated
@AllArgsConstructor
public class VehicleController extends GenericController<Vehicle, VehicleDto> {
    private final VehicleService vehicleService;
    private final UserService userService;
    private final DocumentationsService documentationsService;


    private Vehicle createVehicle(Vehicle vehicleEntity) {
        VehicleTax vehicleTax = vehicleEntity.getVehicleTax();
        vehicleTax.setVehicle(vehicleEntity);
        vehicleEntity.setVehicleTax(vehicleTax);
        TechnicalVisit technicalVisit = vehicleEntity.getTechnicalVisit();
        technicalVisit.setVehicle(vehicleEntity);
        vehicleEntity.setTechnicalVisit(technicalVisit);
        Insurance insurance = vehicleEntity.getInsurance();
        insurance.setVehicle(vehicleEntity);
        vehicleEntity.setInsurance(insurance);
        return vehicleService.save(vehicleEntity);
    }

    @PostMapping("/add-vehicle")
    public ResponseEntity<VehicleDto> save(@RequestBody VehicleDto vehicleDto) {
        Vehicle vehicleEntity = convertToEntity(vehicleDto);
        Vehicle savedVehicle = createVehicle(vehicleEntity);
        VehicleDto savedVehicleDto = convertToDto(savedVehicle);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicleDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") Long id) {
        final Vehicle vehicle = vehicleService.findById(id);
        Date currentDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        if (vehicleService.hasVehicleTravelAfterDate(vehicle, currentDate, currentDate)) {
            throw new ResourceDeletionNotAllowedException(new ResourceDeletionNotAllowedException(), AUTHORIZATION_RESOURCE_DELETION_NOT_ALLOWED, null);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(vehicleService.delete(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<VehicleDto> update(@PathVariable("id") Long id, @RequestBody VehiclePatchDto vehicleDto) {
        final Vehicle vehicle = vehicleService.findById(id);
        Date currentDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        if (vehicleService.hasVehicleTravelAfterDate(vehicle, currentDate, currentDate)) {
            throw new ResourceDeletionNotAllowedException(new ResourceDeletionNotAllowedException(), AUTHORIZATION_RESOURCE_DELETION_NOT_ALLOWED, null);
        }
        mapWithSkipNull(vehicleDto, vehicle);
        Vehicle updateVehicle = vehicleService.patch(vehicle);
        return ResponseEntity.status(HttpStatus.OK).body(convertToDto(updateVehicle));
    }

    @GetMapping("/available-vehicles")
    public ResponseEntity<List<VehicleDto>> findAllAvailableVehicles(@RequestParam(value = "page", defaultValue = "" + DEFAULT_PAGE_NUMBER) Integer page,
                                                                     @RequestParam(value = "size", defaultValue = "" + DEFAULT_PAGE_SIZE) Integer size) {
        Date currentDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<Vehicle> availableVehicles = vehicleService.findAvailableVehicles(page,size);
        List<VehicleDto> vehicleDtos = availableVehicles.stream()
                .map(this::convertToDto)
                .collect(java.util.stream.Collectors.toList());
        long totalVehicles = vehicleService.countAvailableVehicles();
        int totalPages = (int) Math.ceil((double) totalVehicles / size);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Pages", String.valueOf(totalPages));
        headers.add("Access-Control-Expose-Headers", "X-Total-Pages");

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(vehicleDtos);
    }


}
