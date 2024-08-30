package org.khatri.sto.ambassador.dto.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.khatri.sto.ambassador.dto.BaseDto;

import java.util.Date;

/**
 * @author Ankit Khatri
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationBody extends BaseDto {

    private boolean isValid;
    private String username;
    private Date expiration;
}
