package com.fleetManagement.app.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForgetPasswordRequest {

    private String email;
}
