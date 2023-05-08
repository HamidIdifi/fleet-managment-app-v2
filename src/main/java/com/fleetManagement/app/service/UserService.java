package com.fleetManagement.app.service;

import com.fleetManagement.app.dto.JwtToken;
import com.fleetManagement.app.entities.GenericEnum;
import com.fleetManagement.app.entities.User;
import com.fleetManagement.app.exception.BusinessException;
import com.fleetManagement.app.exception.ElementNotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService extends GenericService<User> {

    User findByEmail(String email) throws ElementNotFoundException;

    User findByEmail_v2(String email);

    List<User> searchByKeywordAndRole(String keyword, Pageable pageable, GenericEnum.RoleName roleName);

    long countByRoles(GenericEnum.RoleName roleName);


    boolean sendVerificationEmail(User user);

    void generateVerificationCode(User user);

    User verify(String verificationCode);

    void saveAdmin();

    void saveDev();

    JwtToken generateResetPasswordToken(User user);

    void resetPassword(User user, String newPassword);

    boolean checkPassword(User user, String password);
}
