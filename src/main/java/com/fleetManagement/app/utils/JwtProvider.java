package com.fleetManagement.app.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fleetManagement.app.commun.CoreConstant;
import com.fleetManagement.app.dto.JwtToken;
import com.fleetManagement.app.entities.GenericEnum;
import com.fleetManagement.app.entities.User;
import com.fleetManagement.app.exception.BusinessException;
import com.fleetManagement.app.exception.UnauthorizedException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    @Value("${jwt.access-token.secret}")
    private String accessTokenSecret;

    @Value("${jwt.access-token.expiration-in-mins}")
    private int accessTokenExpirationInMins;

    @Value("${jwt.refresh-token.secret}")
    private String refreshTokenSecret;

    @Value("${jwt.reset-token.secret}")
    private String resetTokenSecret;

    @Value("${jwt.reset-token.expiration-in-mins}")
    private int resetTokenExpirationInMins;

    @Value("${jwt.refresh-token.expiration-in-weeks}")
    private int refreshTokenExpirationInWeeks;


    public JwtToken generateToken(User user, GenericEnum.JwtTokenType tokenType) throws BusinessException {

        Instant creationDate = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant();


        JWTCreator.Builder builder = JWT.create()
                .withJWTId(UUID.randomUUID().toString())
                .withSubject(user.getId().toString())
                .withIssuedAt(creationDate);

        return checkTokenType(user, tokenType, creationDate, builder);
    }

    public JwtToken generateToken(User user, GenericEnum.JwtTokenType tokenType, String tokenId) throws BusinessException {
        Instant creationDate = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant();


        JWTCreator.Builder builder = JWT.create()
                .withJWTId(tokenId)
                .withSubject(user.getId().toString())
                .withIssuedAt(creationDate);

        return checkTokenType(user, tokenType, creationDate, builder);
    }

    private JwtToken checkTokenType(User user, GenericEnum.JwtTokenType tokenType, Instant creationDate, JWTCreator.Builder builder) {
        String secret;
        Instant expiryDate;
        String token;
        try {
            switch (tokenType) {
                case ACCESS -> {
                    secret = accessTokenSecret;
                    expiryDate = LocalDateTime.now().plusMinutes(accessTokenExpirationInMins)
                            .atZone(ZoneId.systemDefault()).toInstant();

                    List<String> claims = user.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList());

                    builder
                            .withExpiresAt(expiryDate)
                            .withClaim("roles", claims);
                }
                case REFRESH -> {
                    secret = refreshTokenSecret;
                    expiryDate = LocalDateTime.now().plusWeeks(refreshTokenExpirationInWeeks)
                            .atZone(ZoneId.systemDefault()).toInstant();

                    builder
                            .withExpiresAt(expiryDate);
                }
                case RESET -> {
                    secret = resetTokenSecret;
                    expiryDate = LocalDateTime.now().plusMinutes(resetTokenExpirationInMins)
                            .atZone(ZoneId.systemDefault()).toInstant();

                    builder
                            .withExpiresAt(expiryDate);

                }
                default -> throw new BusinessException("Invalid token type", new BusinessException(), null, null);
            }

            token = builder.sign(Algorithm.HMAC256(secret));

        } catch (IllegalArgumentException | JWTCreationException e) {
            throw new BusinessException(e.getMessage(), e.getCause(), null, null);
        }

        return JwtToken.builder()
                .token(token)
                .createdAt(creationDate)
                .expiresIn(expiryDate)
                .build();
    }

    public DecodedJWT getDecodedJWT(String token, GenericEnum.JwtTokenType tokenType) throws UnauthorizedException {
        String secret = (Objects.equals(GenericEnum.JwtTokenType.ACCESS, tokenType)) ? accessTokenSecret : refreshTokenSecret;

        try {
            return JWT
                    .require(Algorithm.HMAC256(secret))
                    .build()
                    .verify(token);
        } catch (JWTVerificationException e) {
            throw new UnauthorizedException(null, e.getCause(), CoreConstant.Exception.AUTHORIZATION_INVALID_TOKEN, null);
        }
    }

    public String extractTokenFromRequest(HttpServletRequest request) throws UnauthorizedException {
        final String bearerPrefix = "Bearer ";

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || authorizationHeader.isBlank())
            throw new UnauthorizedException(null, new UnauthorizedException(), CoreConstant.Exception.AUTHORIZATION_MISSING_HEADER, null);

        if (!authorizationHeader.startsWith(bearerPrefix))
            throw new UnauthorizedException(null, new UnauthorizedException(), CoreConstant.Exception.AUTHORIZATION_INVALID_HEADER, null);

        String token = authorizationHeader.replace(bearerPrefix, Strings.EMPTY);

        if (token.isBlank())
            throw new UnauthorizedException(null, new UnauthorizedException(), CoreConstant.Exception.AUTHORIZATION_MISSING_TOKEN, null);

        return token;
    }

}
