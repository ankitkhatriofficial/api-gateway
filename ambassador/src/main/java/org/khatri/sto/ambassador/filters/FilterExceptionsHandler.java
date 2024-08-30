package org.khatri.sto.ambassador.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.khatri.sto.ambassador.exceptions.AmbassadorException;
import org.khatri.sto.ambassador.exceptions.BaseExceptionDto;
import org.khatri.sto.ambassador.exceptions.ErrorCode;
import org.springframework.stereotype.Component;

/**
 * @author Ankit Khatri
 */

@Component
@RequiredArgsConstructor
public class FilterExceptionsHandler {

    private final ObjectMapper objectMapper;

    public String resolveSecurityException(AmbassadorException ex) throws JsonProcessingException {
        return this.objectMapper.writeValueAsString(ex.getExceptionDto());
    }

    public String resolveSecurityException(ErrorCode errorCode) throws JsonProcessingException {
        BaseExceptionDto baseExceptionDto = new BaseExceptionDto(errorCode);
        return this.objectMapper.writeValueAsString(baseExceptionDto);
    }
}
