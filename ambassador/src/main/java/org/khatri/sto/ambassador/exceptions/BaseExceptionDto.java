package org.khatri.sto.ambassador.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Ankit Khatri
 */

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@NoArgsConstructor
public class BaseExceptionDto implements Serializable {

    private static final long serialVersionUID = 778269823698239823L;

    private Integer code;
    private String message;
    private String details;

    public BaseExceptionDto(ErrorCode errorCode){
        this.code = errorCode.getCode();
        this.message = errorCode.getErrorMessage();
    }

    public BaseExceptionDto(ErrorCode errorCode, String details){
        this.details = details;
        this.code = errorCode.getCode();
        this.message = errorCode.getErrorMessage();
    }

    public BaseExceptionDto(Integer code, String message, String details){
        this.code = code;
        this.message = message;
        this.details = details;
    }
}
