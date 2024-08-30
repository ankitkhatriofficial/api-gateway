package org.khatri.sto.ambassador.service;

import org.khatri.sto.ambassador.constant.Constants;
import org.khatri.sto.ambassador.dto.security.UserDto;
import org.khatri.sto.ambassador.enums.ExternalSystem;
import org.khatri.sto.ambassador.source.ExHttpRequest;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author Ankit Khatri
 */
@Service
public class UserService {

    @Value("${user.service.url}")
    private String userServiceBaseUrl;

    @Autowired private RestService restService;

    private ExHttpRequest.ExHttpRequestBuilder getBasicExHttpRequest(final String path, final HttpMethod httpMethod, final Object payload){
        return ExHttpRequest.builder().system(ExternalSystem.USER_SERVICE).httpMethod(httpMethod)
                .uri(this.userServiceBaseUrl.concat("/users").concat(path)).payload(payload);
    }

    public Object fetchAllUsers() {
        ExHttpRequest exHttpRequest = this.getBasicExHttpRequest("/fetch-all", HttpMethod.GET, null)
                .headers(new HashMap<>(){{put(Constants.USER_ID, MDC.get(Constants.USER_ID));}}).build();
        return this.restService.execute(exHttpRequest, Object.class);
    }

    public Object registerNewUser(Object request) {
        final String url = this.userServiceBaseUrl.concat("/register");
        ExHttpRequest exHttpRequest = this.getBasicExHttpRequest("/register", HttpMethod.POST, request)
                .headers(new HashMap<>(){{put(Constants.USER_ID, MDC.get(Constants.USER_ID));}}).build();
        return this.restService.execute(exHttpRequest, Object.class);
    }

    public Object fetchUserById(Long userId) {
        ExHttpRequest exHttpRequest = this.getBasicExHttpRequest("/".concat(userId.toString()), HttpMethod.GET, null)
                .headers(new HashMap<>(){{put(Constants.USER_ID, MDC.get(Constants.USER_ID));}}).build();
        return this.restService.execute(exHttpRequest, Object.class);
    }

    public UserDto fetchUserByUsername(final String username){
        ExHttpRequest exHttpRequest = this.getBasicExHttpRequest("/username/".concat(username), HttpMethod.GET, null)
                .headers(new HashMap<>(){{put(Constants.USER_ID, MDC.get(Constants.USER_ID));}}).build();
        return this.restService.execute(exHttpRequest, UserDto.class);
    }
}
