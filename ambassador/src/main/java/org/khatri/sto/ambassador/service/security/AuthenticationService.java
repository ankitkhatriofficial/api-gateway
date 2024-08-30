package org.khatri.sto.ambassador.service.security;

import org.khatri.sto.ambassador.dto.security.AuthenticationBody;

/**
 * @author Ankit Khatri
 */

public interface AuthenticationService {

    String generateToken(final String subject);
    String generateRefreshToken(final String accessToken);
    AuthenticationBody validateToken(final String token, final String subject);
    String getSubject(final String token);
    String getSubjectEvenTokenExpired(final String token);
}
