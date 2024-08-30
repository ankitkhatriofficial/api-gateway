package org.khatri.sto.ambassador.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

/**
 * @author Ankit Khatri
 */

@Slf4j
@RestControllerAdvice
public class GlobalExceptionsHandler {

    @ExceptionHandler(AmbassadorException.class)
    public ResponseEntity<?> handlePlatformExceptions(AmbassadorException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(ex.getExceptionDto());
    }

    @ExceptionHandler({ HttpClientErrorException.class, HttpRequestMethodNotSupportedException.class, HttpMediaTypeNotSupportedException.class})
    public ResponseEntity<?> handleNotFoundResource(Exception ex) {
        BaseExceptionDto exceptionDto = new BaseExceptionDto(ErrorCode.RESOURCE_NOT_FOUND, ex.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionDto);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException ex) {
        BaseExceptionDto exceptionDto = new BaseExceptionDto(ErrorCode.WRONG_CREDENTIALS);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exceptionDto);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> handleInternalServerError(Exception ex) {
//        BaseExceptionDto exceptionDto = new BaseExceptionDto(ErrorCode.INTERNAL_SERVER_ERROR, "Please reach out to support for more information");
//        log.error("[Internal Server Error] root caused:{}", ex.getStackTrace());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionDto);
//    }

}
