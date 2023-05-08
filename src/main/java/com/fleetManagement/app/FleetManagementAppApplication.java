package com.fleetManagement.app;

import com.fleetManagement.app.commun.CoreConstant;
import com.fleetManagement.app.entities.*;
import com.fleetManagement.app.entities.GenericEnum.CarBrand;
import com.fleetManagement.app.entities.GenericEnum.Energy;
import com.fleetManagement.app.entities.GenericEnum.InsuranceCompany;
import com.fleetManagement.app.entities.GenericEnum.Status;
import com.fleetManagement.app.exception.ElementAlreadyExistException;
import com.fleetManagement.app.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.fleetManagement.app.entities.GenericEnum.LicenseType.B;
import static com.fleetManagement.app.entities.GenericEnum.LicenseType.D;

@Slf4j
@SpringBootApplication
@AllArgsConstructor
public class FleetManagementAppApplication implements CommandLineRunner {
    private LicenseService licenseService;
    private VehicleService vehicleService;
    private DriverService driverService;
    private DocumentationsService documentationsService;
    private TravelService travelService;
    private DevRoleService devRoleService;
    private AdminRoleService adminRoleService;
    private UserRoleService userRoleService;
    private UserService userService;
    static List<String> driversNamesList;


    public static List<String> randomFirstNameGenerator() {
        return Stream
                .generate(() -> ((new Faker()).name().firstName()))
                .limit(10)
                .collect(Collectors.toList());
    }


    public static void main(String[] args) {
        SpringApplication.run(FleetManagementAppApplication.class, args);
    }
    @Override
    public void run(String... args) {
        userRoleService.save(new UserRole());
        adminRoleService.save(new AdminRole());
        devRoleService.save(new DevRole());
        try {
            userService.saveAdmin();
            userService.saveDev();
            log.info(CoreConstant.Success.USER_ROLE_CREATED_SUCCESSFULLY);
        } catch (ElementAlreadyExistException ex) {
            log.info(CoreConstant.Exception.ADMIN_ACCOUNT_ALREADY_CREATED);
        }
        /*driversNamesList = randomFirstNameGenerator();
        driversNamesList.stream()
                .forEach(firstname -> {
                    Driver driver = new Driver();
                    driver.setFirstName(firstname);
                    driver.setCin((new Faker()).code().imei());
                    driver.setStatus(Status.ENABLED);
                    driver.setLastName((new Faker()).name().lastName());
                    driver.setDateOfBirth(new Date());
                    driverService.save(driver);
                });
        System.out.println("before deleting ");
        driversNamesList.stream().forEach(System.out::println);
        //driverService.delete(driverService.findAll().get(0).getRegister());
        Driver driver = driverService.findById(driverService.findAll().get(0).getId());
        License license1 = new License();
        license1.setType(D);
        license1.setDateOfLicense(new Date());
        License license2 = new License();
        license2.setType(B);
        license2.setDateOfLicense(new Date());
        List<License> licenses= new ArrayList<>();
        licenses.add(license1);
        licenses.add(license2);
        System.out.println(licenses.size());
        IntStream.range(0, licenses.size()).forEachOrdered(i -> {
            License license = licenses.get(i);
            System.out.println(license);
            license.setDriver(driver);
            licenseService.patch(license);
        });
        driverService.patch(driver);*/
            Vehicle vehicle=new Vehicle();
            vehicle.setEnergy(Energy.DIESEL);
            vehicle.setCarBrand(CarBrand.AUDI);
            vehicle.setDateOfMC(new Date());
            vehicle.setVehicleUsage("for travel");
            vehicle.setExpiryDate(new Date());
            vehicle.setFiscalPower(120);
            Insurance insurance=new Insurance();
            insurance.setDateOfStart(new Date());
            insurance.setDateOfEnding(new Date());
            insurance.setValid(true);
            insurance.setInsuranceCompany(InsuranceCompany.CAT);
            insurance.setVehicle(vehicle);

            /*VehicleTax vehicleTax=new VehicleTax();
            vehicleTax.setDateOfStart(new Date());
            vehicleTax.setValid(true);
            vehicleTax.setDateOfEnding(new Date());
            vehicleTax.setCf(1200);
            vehicleTax.setVehicle(vehicle);
            documentationsService.save(vehicleTax);*/

            vehicleService.save(vehicle);

    };



}
