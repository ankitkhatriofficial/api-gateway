package org.khatri.sto.ambassador.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * @author Ankit Khatri
 */
@Data
public class WrongCredentialsException extends RuntimeException {

    private BaseExceptionDto exceptionDto;
    private HttpStatus httpStatus = HttpStatus.FORBIDDEN;

    public WrongCredentialsException(ErrorCode ex) {
        this.exceptionDto = new BaseExceptionDto(ex);
    }
}
