package org.khatri.sto.ambassador.service;

import lombok.extern.slf4j.Slf4j;
import org.khatri.sto.ambassador.constant.Constants;
import org.khatri.sto.ambassador.dto.exceptions.ExternalErrorResponse;
import org.khatri.sto.ambassador.enums.ExternalSystem;
import org.khatri.sto.ambassador.exceptions.AmbassadorException;
import org.khatri.sto.ambassador.exceptions.ErrorCode;
import org.khatri.sto.ambassador.filters.RequestLoggingFilter;
import org.khatri.sto.ambassador.source.ExHttpRequest;
import org.khatri.sto.ambassador.util.ControllerUtil;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Ankit Khatri
 */
@Slf4j
@Service
public class RestService {

    @Autowired private RestTemplate restTemplate;

    private HttpHeaders getHttpHeaders(final Map<String, String> extraHeaders){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set(Constants.HEADER_SERVICE_NAME, Constants.AMBASSADOR_MICROSERVICE_NAME);
        headers.set("AMBASSADOR-REQUEST-ID", MDC.get(RequestLoggingFilter.REQUEST_ID));
        if(extraHeaders != null){
            extraHeaders.keySet().stream().forEach(headerName -> headers.set(headerName, extraHeaders.get(headerName)));
        }
        return headers;
    }

    private <T> HttpEntity<T> prepareHttpEntity(final T payload, final Map<String, String> providedHeaders){
        return new HttpEntity<>(payload, this.getHttpHeaders(providedHeaders));
    }

    private String prepareExternalUri(final String uri, final Map<String, String> params){
        if(params != null){
            String requestParams = params.keySet().stream().map(paramName -> paramName.concat("=").concat(params.get(paramName))).collect(Collectors.joining(","));
            return uri.concat("?").concat(requestParams);
        }
        return uri;
    }

    public <T> T execute(final ExHttpRequest request, final Class<T> returnType){
        this.retainLogBeforeCall(request, returnType);
        String externalUri = this.prepareExternalUri(request.getUri(), request.getParams());
        HttpEntity<Object> httpEntity = this.prepareHttpEntity(request.getPayload(), request.getHeaders());
        final long startTime = System.currentTimeMillis();
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(externalUri, request.getHttpMethod(), httpEntity, String.class);
            if(response.getStatusCode().is2xxSuccessful()){
                return ControllerUtil.jsonToObject(response.hasBody() ? response.getBody() : null, returnType);
            } else if(response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()){
                throw new AmbassadorException(HttpStatus.valueOf(response.getStatusCode().value()), null);
            }
        } catch (ResourceAccessException ex){
            throw new AmbassadorException(ErrorCode.EXTERNAL_SYSTEM_DOWN, HttpStatus.INTERNAL_SERVER_ERROR, request.getSystem().name());
        } catch (AmbassadorException ex){
            ExternalErrorResponse errorResponseBody = response.hasBody() ? ControllerUtil.jsonToObject(response.getBody(), ExternalErrorResponse.class) : null;
            throw new AmbassadorException(ex.getHttpStatus(), errorResponseBody);
        } catch(Exception ex){
            throw new AmbassadorException(ErrorCode.API_CALL_FAILED, HttpStatus.INTERNAL_SERVER_ERROR, request.getSystem().name());
        } finally{
            final long endTime = System.currentTimeMillis();
            this.retainLogAfterCall(startTime, endTime, request.getSystem(), response);
        }
        return ControllerUtil.jsonToObject(response.hasBody() ? response.getBody() : null, returnType);
    }

    private void retainLogAfterCall(final long startTime, final long endTime, final ExternalSystem externalSystem, final ResponseEntity<String> response) {
        StringBuilder logger = new StringBuilder("[RestService] Response captured in {}ms from external System: {}, responseCode: {}");
        log.info(logger.toString(), (endTime - startTime), externalSystem, response != null ? response.getStatusCode() : "response came null");
    }

    private <T> void retainLogBeforeCall(final ExHttpRequest request, final Class<T> returnType) {
        StringBuilder logger = new StringBuilder("[RestService] Calling external uri:{} with method:{}, params:{}, headers:{}, returnType:{}");
        log.info(logger.toString(), request.getUri(), request.getHttpMethod(), request.getParams(), request.getHeaders(), returnType);
    }
}
