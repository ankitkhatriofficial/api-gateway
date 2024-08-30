package org.khatri.sto.ambassador.service.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.khatri.sto.ambassador.constant.Constants;
import org.khatri.sto.ambassador.dto.security.AuthRequest;
import org.khatri.sto.ambassador.dto.security.AuthResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * @author Ankit Khatri
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final AuthenticationManager authenticationManager;
    private final AuthenticationService authenticationService;

    private AuthResponse generateAuthToken(final String username) {
        String accessToken = this.authenticationService.generateToken(username);
        log.info("[SecurityServiceImpl] Access token:{} has been generated for username:{}", accessToken, username);
        return AuthResponse.builder().accessToken(accessToken).authType(Constants.SECURITY_HEADER_AUTH_TYPE).build();
    }

    @Override
    public AuthResponse generateToken(final AuthRequest request) {
        log.info("[SecurityServiceImpl] Request received for generating token:{}", request);
        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        return this.generateAuthToken(request.getUsername());
    }

}
