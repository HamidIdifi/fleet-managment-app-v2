package com.fleetManagement.app;

import com.fleetManagement.app.commun.CoreConstant;
import com.fleetManagement.app.entities.AdminRole;
import com.fleetManagement.app.entities.DevRole;
import com.fleetManagement.app.entities.ManagerRole;
import com.fleetManagement.app.entities.UserRole;
import com.fleetManagement.app.exception.ElementAlreadyExistException;
import com.fleetManagement.app.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@AllArgsConstructor
public class FleetManagementAppApplication implements CommandLineRunner {
    private final DevRoleService devRoleService;
    private final AdminRoleService adminRoleService;
    private final UserRoleService userRoleService;
    private final UserService userService;
    private final ManagerRoleService managerRoleService;



    public static void main(String[] args) {
        SpringApplication.run(FleetManagementAppApplication.class, args);
    }

    @Override
    public void run(String... args) {
        userRoleService.save(new UserRole());
        managerRoleService.save(new ManagerRole());
        adminRoleService.save(new AdminRole());
        devRoleService.save(new DevRole());
        try {
            userService.saveAdmin();
            userService.saveDev();
            userService.saveManager();
            userService.saveDriver();
            log.info(CoreConstant.Success.USER_ROLE_CREATED_SUCCESSFULLY);
        } catch (ElementAlreadyExistException ex) {
            log.info(CoreConstant.Exception.ADMIN_ACCOUNT_ALREADY_CREATED);
        }

    }

    ;


}
