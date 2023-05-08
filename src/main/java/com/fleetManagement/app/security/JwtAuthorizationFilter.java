package com.fleetManagement.app.security;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.fleetManagement.app.entities.GenericEnum;
import com.fleetManagement.app.exception.BusinessException;
import com.fleetManagement.app.exception.UnauthorizedException;
import com.fleetManagement.app.utils.JwtProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    final Logger LOG = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    private final JwtProvider jwtProvider;
    private final List<String> authorizationWhiteList;
    private final HandlerExceptionResolver handlerExceptionResolver;


    public JwtAuthorizationFilter(JwtProvider jwtProvider, List<String> authorizationWhiteList,
                                  HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtProvider = jwtProvider;
        this.authorizationWhiteList = authorizationWhiteList;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws BusinessException {

        try {

            String accessToken = jwtProvider.extractTokenFromRequest(request);

            DecodedJWT decodedJWT = jwtProvider.getDecodedJWT(accessToken, GenericEnum.JwtTokenType.ACCESS);

            Long id = Long.valueOf(decodedJWT.getSubject());
            String[] rolesClaim = decodedJWT.getClaim("roles").asArray(String.class);

            List<GrantedAuthority> roles = stream(rolesClaim)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            Authentication authToken = new UsernamePasswordAuthenticationToken(id, null, roles);
            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);

        } catch (IOException | ServletException e) {
            LOG.error("Error", e);
            throw new BusinessException(e.getMessage(), e.getCause(), null, null);
        } catch (UnauthorizedException e) {
            LOG.error("Error", e);
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        List<RequestMatcher> antPathRequestMatchers = authorizationWhiteList.stream()
                .map(AntPathRequestMatcher::new)
                .collect(Collectors.toList());

        OrRequestMatcher requestMatcher = new OrRequestMatcher(antPathRequestMatchers);

        return requestMatcher.matches(request);
    }
}
