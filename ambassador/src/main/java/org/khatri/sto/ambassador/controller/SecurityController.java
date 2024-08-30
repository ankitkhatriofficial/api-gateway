package org.khatri.sto.ambassador.controller;

import jakarta.validation.Valid;
import org.khatri.sto.ambassador.annotations.OpenEndPoint;
import org.khatri.sto.ambassador.dto.security.AuthRequest;
import org.khatri.sto.ambassador.exceptions.AmbassadorException;
import org.khatri.sto.ambassador.exceptions.ErrorCode;
import org.khatri.sto.ambassador.service.security.AuthenticationService;
import org.khatri.sto.ambassador.service.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

/**
 * @author Ankit Khatri
 */

@RestController
public class SecurityController {

    @Autowired private AuthenticationService authenticationService;
    @Autowired private SecurityService securityService;

    private void throwValidationErrorIfAny(final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = "Validation failed in fields: ".concat(bindingResult.getFieldErrors().stream().map(fe -> fe.getDefaultMessage()).collect(Collectors.joining(",")));
            throw new AmbassadorException(ErrorCode.INVALID_REQUEST, HttpStatus.BAD_REQUEST, errorMessage);
        }
    }

    @OpenEndPoint
    @PostMapping("/login")
    public ResponseEntity<?> generateToken(@Valid @RequestBody AuthRequest request, BindingResult result){
        this.throwValidationErrorIfAny(result);
        return ResponseEntity.status(HttpStatus.OK).body(this.securityService.generateToken(request));
    }
}
