package com.fleetManagement.app.controller;

import com.fleetManagement.app.dto.ConsumptionDocumentDto;
import com.fleetManagement.app.dto.ConsumptionDto;
import com.fleetManagement.app.entities.ConsumptionDocument;
import com.fleetManagement.app.entities.Travel;
import com.fleetManagement.app.service.ConsumptionDocumentService;
import com.fleetManagement.app.service.TravelService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/consumptions")
@Validated
@AllArgsConstructor
public class ConsumptionDocumentController extends GenericController<ConsumptionDocument, ConsumptionDocumentDto> {
    private final ConsumptionDocumentService consumptionDocumentService;
    private final TravelService travelService;

    private ConsumptionDocument createConsumptionDocument(Long travelId, ConsumptionDto consumptionDto) {
        ConsumptionDocument consumptionDocumentEntity = new ConsumptionDocument();
        Travel travelEntity = travelService.findById(travelId);
        consumptionDocumentEntity.setDriverName(travelEntity.getDriver().getFirstName() + " " + travelEntity.getDriver().getLastName());
        consumptionDocumentEntity.setVehicleName(travelEntity.getVehicle().getCarBrand());
        float distanceTraveled = consumptionDto.getDistanceTraveled();
        consumptionDocumentEntity.setDistanceTraveled(distanceTraveled);
        float fuelPurchased = consumptionDto.getFuelPurchased();
        consumptionDocumentEntity.setFuelPurchased(fuelPurchased);
        consumptionDocumentEntity.setAmount_of_fuel_consumed(distanceTraveled, fuelPurchased);
        consumptionDocumentEntity.setVehicleEnergy(travelEntity.getVehicle().getEnergy());
        Date travelDate = consumptionDto.getTravelDate();
        consumptionDocumentEntity.setTravelDate(travelDate);
        consumptionDocumentEntity.setTravel(travelEntity);
        return consumptionDocumentService.save(consumptionDocumentEntity);
    }

    @PostMapping("/{travelId}")
    public ResponseEntity<ConsumptionDocumentDto> save(@PathVariable("travelId") Long travelId, @RequestBody ConsumptionDto consumptionDto) {
        ConsumptionDocument savedConsumptionDocument = createConsumptionDocument(travelId, consumptionDto);
        ConsumptionDocumentDto savedConsumptionDocumentDto = convertToDto(savedConsumptionDocument);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedConsumptionDocumentDto);
    }
}
