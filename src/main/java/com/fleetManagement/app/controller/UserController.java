package com.fleetManagement.app.controller;

import com.fleetManagement.app.commun.CoreConstant;
import com.fleetManagement.app.dto.AuthenticatedResetPasswordRequest;
import com.fleetManagement.app.dto.UserDto;
import com.fleetManagement.app.dto.UserPatchDto;
import com.fleetManagement.app.entities.User;
import com.fleetManagement.app.exception.UnauthorizedException;
import com.fleetManagement.app.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController extends GenericController<User, UserDto> {

    private final UserService userService;


    @Override
    public ModelMapper getModelMapper() {
        return super.getModelMapper();
    }


    @GetMapping("/me")
    public ResponseEntity<UserDto> getMe() {
        return new ResponseEntity<>(convertToDto(getCurrentUser()), HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<UserDto> update(@RequestBody @Valid UserPatchDto userDto) {
        User currentUser = getCurrentUser();
        mapWithSkipNull(userDto, currentUser);
        currentUser.setProfileCompleted(Boolean.TRUE);
        User updated = userService.update(currentUser.getId(), currentUser);

        return new ResponseEntity<>(convertToDto(updated), HttpStatus.OK);
    }


    @PatchMapping("/reset-password")
    public ResponseEntity<UserDto> resetPassword(@RequestBody @Valid AuthenticatedResetPasswordRequest resetPasswordRequest) {
        User currentUser = getCurrentUser();

        if (userService.checkPassword(currentUser, resetPasswordRequest.getOldPassword())) {
            userService.resetPassword(currentUser, resetPasswordRequest.getNewPassword());
            User updated = userService.update(currentUser.getId(), currentUser);
            return new ResponseEntity<>(convertToDto(updated), HttpStatus.OK);
        }

        throw new UnauthorizedException(new UnauthorizedException(), CoreConstant.Exception.DEFAULT, null);

    }


}
