package org.khatri.sto.ambassador.exceptions;

import lombok.Getter;

/**
 * @author Ankit Khatri
 */

@Getter
public enum ErrorCode {

    REQUIRES_AUTHORIZATION_HEADER(1000,"Authorization token is required"),
    MISSING_BEARER_PREFIX(1002, "Required Bearer as prefix in accessToken"),
    INVALID_ACCESS_TOKEN(1003, "Invalid or Expired token"),
    RESOURCE_NOT_FOUND(1004, "Requested resource not found"),
    API_CALL_FAILED(1005, "External API call failed"),
    EXTERNAL_SYSTEM_DOWN(1006, "Server is down"),
    API_CALL_CLIENT_EXCEPTION(1007, ""),
    INVALID_TOKEN(1008, "Invalid or Expired token"),
    INVALID_REQUEST(1009, "Invalid request"), WRONG_CREDENTIALS(1010, "Wrong credential");


    private int code;
    private String errorMessage;
    ErrorCode(int code, String errorMessage){
        this.code = code;
        this.errorMessage = errorMessage;
    }
}
