package org.khatri.sto.ambassador.service;

import org.khatri.sto.ambassador.constant.Constants;
import org.khatri.sto.ambassador.enums.ExternalSystem;
import org.khatri.sto.ambassador.source.ExHttpRequest;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @author Ankit Khatri
 */

@Service
public class TagService {

    @Value("${tag.service.url}")
    private String tagBaseUrl;

    @Autowired private RestService restService;

    private ExHttpRequest.ExHttpRequestBuilder getBasicExHttpRequest(final String path, final HttpMethod httpMethod, final Object payload){
        return ExHttpRequest.builder().system(ExternalSystem.TAG_SERVICE).httpMethod(httpMethod)
                .uri(this.tagBaseUrl.concat(path)).payload(payload);
    }

    public Object mapTagsWithRefId(String refId, String refType, List<String> tagNames) {
        ExHttpRequest exHttpRequest = this.getBasicExHttpRequest("/tags/mapping/".concat(refId).concat("?refType=").concat(refType), HttpMethod.POST, tagNames)
                .headers(new HashMap<>(){{put(Constants.USER_ID, MDC.get(Constants.USER_ID));}}).build();
        return this.restService.execute(exHttpRequest, Object.class);
    }

    public Object searchByTags(Object request) {
        ExHttpRequest exHttpRequest = this.getBasicExHttpRequest("/tags/search", HttpMethod.POST, request)
                .headers(new HashMap<>(){{put(Constants.USER_ID, MDC.get(Constants.USER_ID));}}).build();
        return this.restService.execute(exHttpRequest, Object.class);
    }

    public Object findTagWithRefId(String refId, String refType) {
        ExHttpRequest exHttpRequest = this.getBasicExHttpRequest("/tags/find/".concat(refId).concat("?refType=").concat(refType), HttpMethod.GET, null)
                .headers(new HashMap<>(){{put(Constants.USER_ID, MDC.get(Constants.USER_ID));}}).build();
        return this.restService.execute(exHttpRequest, Object.class);
    }
}
