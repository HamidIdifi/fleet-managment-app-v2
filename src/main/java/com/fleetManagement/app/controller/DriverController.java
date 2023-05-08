package com.fleetManagement.app.controller;

import com.fleetManagement.app.dto.DriverDto;
import com.fleetManagement.app.dto.DriverPatchDto;
import com.fleetManagement.app.entities.Driver;
import com.fleetManagement.app.entities.License;
import com.fleetManagement.app.exception.ResourceDeletionNotAllowedException;
import com.fleetManagement.app.service.DriverService;
import com.fleetManagement.app.service.UserService;
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
import java.util.stream.IntStream;

import static com.fleetManagement.app.commun.CoreConstant.Exception.AUTHORIZATION_RESOURCE_DELETION_NOT_ALLOWED;
import static com.fleetManagement.app.commun.CoreConstant.Pagination.DEFAULT_PAGE_NUMBER;
import static com.fleetManagement.app.commun.CoreConstant.Pagination.DEFAULT_PAGE_SIZE;

@RestController
@RequestMapping("/api/drivers")
@Validated
@AllArgsConstructor
public class DriverController extends GenericController<Driver, DriverDto> {
    private final DriverService driverService;
    private final UserService userService;

    private Driver createDriver(Driver driverEntity) {
        List<License> licenses = List.copyOf(driverEntity.getLicenses());
        IntStream.range(0, licenses.size()).forEachOrdered(i -> {
            License license = licenses.get(i);
            license.setDriver(driverEntity);
        });
        return driverService.save(driverEntity);
    }

    @PostMapping("/add-driver")
    public ResponseEntity<DriverDto> save(@RequestBody DriverDto driverDto) {
        Driver driverEntity = convertToEntity(driverDto);
        Driver savedDriver = createDriver(driverEntity);
        DriverDto savedDriverDto = convertToDto(savedDriver);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDriverDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") Long id) {
        final Driver driver = driverService.findById(id);
        Date currentDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        if (driverService.hasDriverTravelAfterDate(driver, currentDate, currentDate)) {
            throw new ResourceDeletionNotAllowedException(new ResourceDeletionNotAllowedException(), AUTHORIZATION_RESOURCE_DELETION_NOT_ALLOWED, null);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(driverService.delete(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DriverDto> update(@PathVariable("id") Long id, @RequestBody DriverPatchDto driverDto) {
        final Driver driver = driverService.findById(id);
        Date currentDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        if (driverService.hasDriverTravelAfterDate(driver, currentDate, currentDate)) {
            throw new ResourceDeletionNotAllowedException(new ResourceDeletionNotAllowedException(), AUTHORIZATION_RESOURCE_DELETION_NOT_ALLOWED, null);
        }
        mapWithSkipNull(driverDto, driver);
        Driver updateDriver = driverService.patch(driver);
        return ResponseEntity.status(HttpStatus.OK).body(convertToDto(updateDriver));
    }

    @GetMapping("/available-drivers")
    public ResponseEntity<List<DriverDto>> findAllAvailableDrivers(@RequestParam(value = "page", defaultValue = "" + DEFAULT_PAGE_NUMBER) Integer page,
                                                                   @RequestParam(value = "size", defaultValue = "" + DEFAULT_PAGE_SIZE) Integer size) {
        List<Driver> availableDrivers = driverService.findAvailableDrivers(page, size);
        List<DriverDto> driverDtos = availableDrivers.stream()
                .map(this::convertToDto)
                .collect(java.util.stream.Collectors.toList());
        long totalDrivers = driverService.countAvailableDrivers();
        int totalPages = (int) Math.ceil((double) totalDrivers / size);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Pages", String.valueOf(totalPages));
        headers.add("Access-Control-Expose-Headers", "X-Total-Pages");

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(driverDtos);
    }

}
