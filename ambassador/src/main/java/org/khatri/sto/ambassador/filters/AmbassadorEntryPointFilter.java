package org.khatri.sto.ambassador.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.khatri.sto.ambassador.constant.Constants;
import org.khatri.sto.ambassador.dto.security.AuthenticationBody;
import org.khatri.sto.ambassador.exceptions.AmbassadorException;
import org.khatri.sto.ambassador.exceptions.ErrorCode;
import org.khatri.sto.ambassador.service.security.AuthenticationService;
import org.khatri.sto.ambassador.service.security.SecurityUserDetailsService;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author Ankit Khatri
 */

@Component
@RequiredArgsConstructor
public class AmbassadorEntryPointFilter extends OncePerRequestFilter {

    private final AuthenticationService authenticationService;
    private final SecurityUserDetailsService securityUserDetailsService;
    private final FilterExceptionsHandler filterExceptionsHandler;
    @Qualifier("allOpenEndPointsBean") private final String[] allOpenEndpoints;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if(this.isRequestedUriSecured(request)){
            try{
                this.validateHttpRequest(request);
                String accessToken = this.retrieveToken(request);
                this.validateAccessToken(accessToken);
                chain.doFilter(request, response);
            } catch(AmbassadorException ex){
                response.setStatus(ex.getHttpStatus().value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
                response.getWriter().write(this.filterExceptionsHandler.resolveSecurityException(ex));
            } catch(Exception ex){
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
                response.getWriter().write(this.filterExceptionsHandler.resolveSecurityException(ErrorCode.INVALID_TOKEN));
            }
        } else{
            chain.doFilter(request, response);
        }
    }

    private String retrieveToken(final HttpServletRequest request){
        String authToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        return authToken.substring(Constants.SECURITY_HEADER_AUTH_TYPE.length());
    }

    private void validateAccessToken(final String accessToken) {
        UserDetails userDetails = this.securityUserDetailsService.loadUserByUsername(this.authenticationService.getSubject(accessToken));
        AuthenticationBody tokenBody = this.authenticationService.validateToken(accessToken, userDetails.getUsername());
        if (tokenBody.isValid() && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
    }

    private boolean isRequestedUriSecured(final HttpServletRequest request) {
        if(this.allOpenEndpoints != null && request.getRequestURI() != null){
            String contextPath = request.getContextPath();
            String requestedUri = request.getRequestURI().substring(contextPath != null ? contextPath.length() : 0);
            Optional<String> result = Arrays.stream(this.allOpenEndpoints).filter(endPoint -> requestedUri.startsWith(endPoint)).findAny();
            if(result.isPresent()){
                return false;
            }
        }
        return true;
    }

    private void validateHttpRequest(final HttpServletRequest request) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(!StringUtils.hasText(accessToken)){
            throw new AmbassadorException(ErrorCode.REQUIRES_AUTHORIZATION_HEADER, HttpStatus.FORBIDDEN, null);
        }
        if(!accessToken.startsWith(Constants.SECURITY_HEADER_AUTH_TYPE)){
            throw new AmbassadorException(ErrorCode.MISSING_BEARER_PREFIX, HttpStatus.FORBIDDEN, null);
        }
    }

}