package com.fleetManagement.app.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fleetManagement.app.commun.CoreConstant;
import com.fleetManagement.app.dto.*;
import com.fleetManagement.app.entities.GenericEnum;
import com.fleetManagement.app.entities.User;
import com.fleetManagement.app.exception.BusinessException;
import com.fleetManagement.app.exception.ElementAlreadyExistException;
import com.fleetManagement.app.exception.ElementNotFoundException;
import com.fleetManagement.app.exception.UnauthorizedException;
import com.fleetManagement.app.service.MailSenderService;
import com.fleetManagement.app.service.UserService;
import com.fleetManagement.app.utils.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
public class AuthController extends GenericController<User, UserDto> {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final MailSenderService mailSenderService;
    @Value("${origin.url}")
    private String originApi;


    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtProvider jwtProvider, MailSenderService mailSenderService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;

        this.mailSenderService = mailSenderService;
    }


    @PostMapping("/register")
    public ResponseEntity<UserDto> saveUser(@RequestBody @Valid UserRegisterDto userDto) throws ElementAlreadyExistException {
        User convertedUser = getModelMapper().map(userDto, User.class);
        userService.generateVerificationCode(convertedUser);
        boolean mailSentFlag = userService.sendVerificationEmail(convertedUser);
        if (mailSentFlag) {
            User savedUser = userService.save(convertedUser);
            return new ResponseEntity<>(convertToDto(savedUser), HttpStatus.CREATED);
        }
        throw new BusinessException();
    }

    @GetMapping("/verify")
    public ResponseEntity<JwtTokenResponseDto> verifyUser(@RequestParam("code") String code) {
        User user = userService.verify(code);
        if (Objects.isNull(user)) {
            throw new UnauthorizedException();
        }
        JwtToken accessToken = jwtProvider.generateToken(user, GenericEnum.JwtTokenType.ACCESS);
        JwtToken refreshToken = jwtProvider.generateToken(user, GenericEnum.JwtTokenType.REFRESH);

        return ResponseEntity.ok().body(JwtTokenResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken).build());
    }

    @PostMapping("/login")
    public ResponseEntity<JwtTokenResponseDto> login(@RequestBody @Valid UserLoginDto userLoginDto) throws UnauthorizedException, ElementNotFoundException {

        Authentication authToken = new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword());

        Authentication authResult;

        try {
            authResult = authenticationManager.authenticate(authToken);
        } catch (org.springframework.security.core.AuthenticationException e) {
            throw new UnauthorizedException(null, e.getCause(), CoreConstant.Exception.AUTHENTICATION_BAD_CREDENTIALS, null);
        }

        User authenticatedUser = (User) authResult.getPrincipal();

        JwtToken accessToken = jwtProvider.generateToken(authenticatedUser, GenericEnum.JwtTokenType.ACCESS);
        JwtToken refreshToken = jwtProvider.generateToken(authenticatedUser, GenericEnum.JwtTokenType.REFRESH);
        String refreshTokenId = jwtProvider.getDecodedJWT(refreshToken.getToken(), GenericEnum.JwtTokenType.REFRESH).getId();

        User connectedUser = userService.findById(authenticatedUser.getId());
        connectedUser.setRefreshTokenId(refreshTokenId);
        userService.update(connectedUser.getId(), connectedUser);
        return ResponseEntity.ok().body(JwtTokenResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken).build());
    }

    @PostMapping("/forget-password")
    public ResponseEntity<ForgetPasswordResponse> sendForgetPassword(@RequestBody @Valid ForgetPasswordRequest forgetPasswordRequest) {
        User user = userService.findByEmail(forgetPasswordRequest.getEmail());
        JwtToken resetToken = userService.generateResetPasswordToken(user);
        Map<String, Object> mailModel = new HashMap<>();
        mailModel.put("token", resetToken.getToken());
        mailModel.put("user", user);
        mailModel.put("signature", "https://fleet-management-app.com");
        mailModel.put("resetUrl", originApi + "/forget-password-verify?code=" + resetToken.getToken());
        mailSenderService.sendEmail(user.getEmail(), "reset password", mailModel, "reset-password.html");

        return ResponseEntity.ok().body(ForgetPasswordResponse.builder().message("email send successfully").build());
    }

    // TODO add end point resend verification mail

    private DecodedJWT getDecodedResetToken(HttpServletRequest request) {
        String jwtResetToken = jwtProvider.extractTokenFromRequest(request);
        return jwtProvider.getDecodedJWT(jwtResetToken, GenericEnum.JwtTokenType.RESET);
    }

    @GetMapping("/forget-password/verify-token")
    public ResponseEntity<JwtToken> sendForgetPassword(HttpServletRequest request) {
        DecodedJWT decodedResetToken = getDecodedResetToken(request);
        Long userId = Long.valueOf(decodedResetToken.getSubject());
        String resetTokenId = decodedResetToken.getId();

        User user = userService.findById(userId);

        try {
            if (!resetTokenId.equals(user.getResetId()))
                throw new UnauthorizedException(null, new UnauthorizedException(), CoreConstant.Exception.AUTHORIZATION_INVALID_TOKEN, null);
        } catch (NullPointerException e) {
            throw new BusinessException(e.getMessage(), e.getCause(), null, null);
        }

        return new ResponseEntity<>(JwtToken.builder().token(decodedResetToken.getToken()).expiresIn(decodedResetToken.getExpiresAtAsInstant()).createdAt(decodedResetToken.getIssuedAtAsInstant()).build(), HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest, HttpServletRequest request) {
        DecodedJWT decodedResetToken = getDecodedResetToken(request);
        Long userId = Long.valueOf(decodedResetToken.getSubject());
        String resetTokenId = decodedResetToken.getId();

        User user = userService.findById(userId);

        try {
            if (!resetTokenId.equals(user.getResetId()))
                throw new UnauthorizedException(null, new UnauthorizedException(), CoreConstant.Exception.AUTHORIZATION_INVALID_TOKEN, null);
        } catch (NullPointerException e) {
            throw new BusinessException(e.getMessage(), e.getCause(), null, null);
        }

        userService.resetPassword(user, resetPasswordRequest.getNewPassword());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/token")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public ResponseEntity<JwtTokenResponseDto> refreshToken(HttpServletRequest request) throws BusinessException {

        String refreshToken = jwtProvider.extractTokenFromRequest(request);

        DecodedJWT decodedRefreshToken = jwtProvider.getDecodedJWT(refreshToken, GenericEnum.JwtTokenType.REFRESH);
        Long userId = Long.valueOf(decodedRefreshToken.getSubject());
        String refreshTokenId = decodedRefreshToken.getId();

        User user = userService.findById(userId);

        try {
            if (!refreshTokenId.equals(user.getRefreshTokenId()))
                throw new UnauthorizedException(null, new UnauthorizedException(), CoreConstant.Exception.AUTHORIZATION_INVALID_TOKEN, null);
        } catch (NullPointerException e) {
            throw new BusinessException(e.getMessage(), e.getCause(), null, null);
        }

        JwtToken newAccessToken = jwtProvider.generateToken(user, GenericEnum.JwtTokenType.ACCESS);
        JwtToken newRefreshToken = jwtProvider.generateToken(user, GenericEnum.JwtTokenType.REFRESH);

        user.setRefreshTokenId(jwtProvider.getDecodedJWT(newRefreshToken.getToken(), GenericEnum.JwtTokenType.REFRESH).getId());
        userService.update(userId, user);

        return ResponseEntity.ok().body(JwtTokenResponseDto.builder().accessToken(newAccessToken).refreshToken(newRefreshToken).build());
    }
}
