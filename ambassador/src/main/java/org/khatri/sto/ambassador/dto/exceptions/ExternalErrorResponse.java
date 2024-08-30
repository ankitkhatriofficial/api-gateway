package org.khatri.sto.ambassador.dto.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Ankit Khatri
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExternalErrorResponse implements Serializable {

    private Integer code;
    private String message;
    private String status;
    private String details;
}
