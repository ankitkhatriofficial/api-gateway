package org.khatri.sto.ambassador.service.security;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.khatri.sto.ambassador.constant.Constants;
import org.khatri.sto.ambassador.dto.security.UserDto;
import org.khatri.sto.ambassador.service.RestService;
import org.khatri.sto.ambassador.service.UserService;
import org.slf4j.MDC;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

/**
 * @author Ankit Khatri
 */

@Slf4j
@Data
@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {

    private final RestService restService;
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(final String username) {
        log.info("[SecurityUserDetailsService] Received request for loading user: {}", username);
        UserDto response = this.userService.fetchUserByUsername(username);
        if(response != null && StringUtils.hasText(response.getUsername()) && StringUtils.hasText(response.getPassword()) && response.getId() != null) {
            MDC.put(Constants.USER_ID, response.getId().toString());
            return new User(response.getUsername(), response.getPassword(), new ArrayList<>());
        }
        throw new UsernameNotFoundException("User not found for username " + username);
    }

}
