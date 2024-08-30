package org.khatri.sto.ambassador.service.security;

import org.khatri.sto.ambassador.dto.security.AuthRequest;
import org.khatri.sto.ambassador.dto.security.AuthResponse;

/**
 * @author Ankit Khatri
 */
public interface SecurityService {
    AuthResponse generateToken(final AuthRequest request);
}
