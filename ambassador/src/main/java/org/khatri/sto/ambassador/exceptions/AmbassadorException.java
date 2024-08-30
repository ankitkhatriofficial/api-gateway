package org.khatri.sto.ambassador.exceptions;

import lombok.Data;
import org.khatri.sto.ambassador.dto.exceptions.ExternalErrorResponse;
import org.springframework.http.HttpStatus;

/**
 * @author Ankit Khatri
 */

@Data
public class AmbassadorException extends RuntimeException {
    private BaseExceptionDto exceptionDto;
    private HttpStatus httpStatus;

    public AmbassadorException(ErrorCode errorCode, HttpStatus httpStatus, String details){
        this.httpStatus = httpStatus;
        this.exceptionDto = new BaseExceptionDto(errorCode, details);
    }

    public AmbassadorException(Integer code, HttpStatus httpStatus, String message){
        this.httpStatus = httpStatus;
        this.exceptionDto = new BaseExceptionDto(code, message, null);
    }

    public AmbassadorException(HttpStatus httpStatus, ExternalErrorResponse errorResponse){
        this.httpStatus = httpStatus;
        if(errorResponse != null) {
            this.exceptionDto = new BaseExceptionDto(errorResponse.getCode(), errorResponse.getMessage(), errorResponse.getDetails());
        }
    }
}
