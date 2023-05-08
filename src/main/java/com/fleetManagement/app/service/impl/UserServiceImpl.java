package com.fleetManagement.app.service.impl;

import com.fleetManagement.app.commun.CoreConstant;
import com.fleetManagement.app.dto.JwtToken;
import com.fleetManagement.app.entities.*;
import com.fleetManagement.app.exception.BusinessException;
import com.fleetManagement.app.exception.ElementAlreadyExistException;
import com.fleetManagement.app.exception.ElementNotFoundException;
import com.fleetManagement.app.repositories.GenericRepository;
import com.fleetManagement.app.repositories.UserRepository;
import com.fleetManagement.app.service.*;
import com.fleetManagement.app.utils.JwtProvider;
import net.bytebuddy.utility.RandomString;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class UserServiceImpl extends GenericServiceImpl<User> implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSenderService mailSenderService;
    private final UserRoleService userRoleService;
    private final AdminRoleService adminRoleService;
    //Todo add manager role service
    private final DevRoleService devRoleService;
    private final JwtProvider jwtProvider;
    @Value("${admin.account.username}")
    private String adminUsername;
    @Value("${admin.account.password}")
    private String adminPassword;
    @Value("${admin.account.name}")
    private String adminName;
    @Value("${dev.account.username}")
    private String devUsername;
    @Value("${dev.account.password}")
    private String devPassword;
    @Value("${dev.account.name}")
    private String devName;
    @Value("${origin.url}")
    private String originUrl;


    //Todo add manager
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, MailSenderService mailSenderService,
                           UserRoleService userRoleService, AdminRoleService adminRoleService,
                           DevRoleService devRoleService, JwtProvider jwtProvider, GenericRepository<User> genericRepository, ModelMapper modelMapper) {
        super(genericRepository, modelMapper);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSenderService = mailSenderService;
        this.userRoleService = userRoleService;
        this.adminRoleService = adminRoleService;
        this.devRoleService = devRoleService;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public User save(User entity) throws ElementAlreadyExistException {
        if (userRepository.findByEmail(entity.getEmail()).isPresent())
            throw new ElementAlreadyExistException(null, new ElementAlreadyExistException(), CoreConstant.Exception.ALREADY_EXISTS,
                    new Object[]{entity.getEmail()});
        final UserRole userRole = userRoleService.findByName(GenericEnum.RoleName.USER);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity.setProfileCompleted(Boolean.FALSE);
        entity.addRole(userRole);
        return userRepository.save(entity);
    }


    @Override
    public User findByEmail(String email) throws ElementNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) return user.get();
        throw new ElementNotFoundException(null, new ElementNotFoundException(), CoreConstant.Exception.NOT_FOUND, new Object[]{email});
    }

    @Override
    public User findByEmail_v2(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public boolean sendVerificationEmail(User user) {
        String subject = "Please Verify your email address";

        Map<String, Object> mailModel = new HashMap<>();
        mailModel.put("token", user.getVerificationCode());
        mailModel.put("user", user);
        mailModel.put("signature", "https://fleet-management-app.com");
        mailModel.put("activationUrl", this.originUrl + "/verify-account?code=" + user.getVerificationCode());

        mailSenderService.sendEmail(user.getEmail(), subject, mailModel, "activate-account.html");
        return true;
    }

    @Override
    public void generateVerificationCode(User user) {
        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        user.setEnabled(false);
    }

    @Override
    public User verify(String code) {
        Optional<User> userFound = userRepository.findByVerificationCode(code);

        if (userFound.isEmpty())
            return null;
        else {
            User user = userFound.get();
            if (user.isEnabled())
                return user;
            user.setVerificationCode(null);
            user.setEnabled(true);

            return userRepository.save(user);
        }
    }

    @Override
    public void saveAdmin() {
        User admin = User.
                builder().
                email(adminUsername).
                password(adminPassword).
                firstName(adminName).
                build();
        if (userRepository.findByEmail(admin.getEmail()).isPresent())
            return;
        final UserRole userRole = userRoleService.findByName(GenericEnum.RoleName.USER);
        final AdminRole adminRole = adminRoleService.findByName(GenericEnum.RoleName.ADMIN);
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.addRole(adminRole);
        admin.addRole(userRole);
        admin.setEnabled(Boolean.TRUE);
        userRepository.save(admin);
    }


    @Override
    public void saveDev() {
        User dev = User.
                builder().
                email(devUsername).
                password(devPassword).
                firstName(devName).
                build();
        if (userRepository.findByEmail(dev.getEmail()).isPresent())
            if (userRepository.findByEmail(dev.getEmail()).isPresent())
                return;
        final DevRole devRole = devRoleService.findByName(GenericEnum.RoleName.DEV);
        dev.setPassword(passwordEncoder.encode(dev.getPassword()));
        dev.addRole(devRole);
        dev.setEnabled(Boolean.TRUE);
        userRepository.save(dev);

    }

    @Override
    public JwtToken generateResetPasswordToken(User user) {
        final String tokenId = UUID.randomUUID().toString();
        final JwtToken resetToken = jwtProvider.generateToken(user, GenericEnum.JwtTokenType.RESET, tokenId);
        user.setResetId(tokenId);
        userRepository.save(user);
        return resetToken;
    }

    @Override
    public void resetPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetId(null);
        userRepository.save(user);
    }

    @Override
    public boolean checkPassword(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    public List<User> searchByKeywordAndRole(String keyword, Pageable pageable, GenericEnum.RoleName roleName) {
        return userRepository.searchByKeywordAndRole(keyword, pageable, roleName);
    }

    @Override
    public long countByRoles(GenericEnum.RoleName roleName) {
        return userRepository.countByRoles_Name(roleName);
    }
}
