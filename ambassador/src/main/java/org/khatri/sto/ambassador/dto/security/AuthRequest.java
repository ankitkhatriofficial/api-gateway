package org.khatri.sto.ambassador.dto.security;
import jakarta.validation.constraints.NotEmpty;
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
public class AuthRequest extends BaseDto {

    @NotEmpty(message = "username can't be empty")
    private String username;
    @NotEmpty(message = "password can't be empty")
    private String password;
}
