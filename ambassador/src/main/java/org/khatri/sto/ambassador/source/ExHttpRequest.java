package org.khatri.sto.ambassador.source;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.khatri.sto.ambassador.enums.ExternalSystem;
import org.springframework.http.HttpMethod;

import java.util.Map;

/**
 * @author Ankit Khatri
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExHttpRequest {

    private ExternalSystem system;
    private String uri;
    private Object payload;
    private HttpMethod httpMethod;
    private Map<String, String> headers;
    private Map<String, String> params;
}

