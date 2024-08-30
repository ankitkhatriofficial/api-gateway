package org.khatri.sto.ambassador.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.khatri.sto.ambassador.filters.AmbassadorEntryPointFilter;
import org.khatri.sto.ambassador.filters.RequestLoggingFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

/**
 * @author Ankit Khatri
 */

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityWebConfiguration {

    private final AmbassadorEntryPointFilter ambassadorEntryPointFilter;
    private final RequestLoggingFilter requestLoggingFilter;
    @Qualifier("allOpenEndPointsBean") private final String[] allOpenEndPoints;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests()
                .requestMatchers(this.allOpenEndPoints).permitAll()
                .anyRequest().authenticated()
            .and()
                .addFilterBefore(this.ambassadorEntryPointFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(this.requestLoggingFilter, AmbassadorEntryPointFilter.class);
        log.info("[SecurityWebConfiguration] Marking these all endpoints as OPEN:{}", Arrays.toString(allOpenEndPoints));
        return http.build();
    }
}
