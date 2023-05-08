package com.fleetManagement.app.dto;


import lombok.*;

import java.time.Instant;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtToken {
    private String token;
    private final String tokenType = "Bearer";
    private Instant createdAt;
    private Instant expiresIn;
}
