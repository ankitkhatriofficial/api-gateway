package org.khatri.sto.ambassador.dto.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.khatri.sto.ambassador.dto.BaseDto;

/**
 * @author Ankit Khatri
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AuthResponse extends BaseDto {

    private String accessToken;
    private String authType;
}