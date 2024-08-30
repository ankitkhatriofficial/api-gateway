//package org.khatri.sto.ambassador.config;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.Serializable;
//import java.util.Enumeration;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author Ankit Khatri
// */
//
//@Slf4j
//@Component
//public class ExceptionHandlerEntryPoint implements AuthenticationEntryPoint, Serializable {
//
//    private static final long serialVersionUID = 47843798739873434L;
//
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
//        log.info("[ExceptionHandlerEntryPoint] UnAuthorised call received: {}", extractRequestedHttpData(request));
//        response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
//    }
//
//    private String extractRequestedHttpData(final HttpServletRequest request) throws IOException {
//        StringBuilder sb = new StringBuilder();
//        sb.append("uri: ").append(request.getRequestURI());
//        sb.append(", headers: ").append(extractHeaders(request));
//        sb.append(", payload: ").append(extractPayload(request));
//        return sb.toString();
//    }
//
//    private Map<String, Object> extractHeaders(final HttpServletRequest request){
//        Map<String, Object> headers = new HashMap<>();
//        Enumeration<String> httpHeaders = request.getHeaderNames();
//        while(httpHeaders.hasMoreElements()){
//            String headerName = httpHeaders.nextElement();
//            headers.put(headerName, request.getHeader(headerName));
//        }
//        return headers;
//    }
//
//    private String extractPayload(final HttpServletRequest request) throws IOException {
//        StringBuilder requestBody = new StringBuilder();
//        BufferedReader reader = request.getReader();
//        String line;
//        while ((line = reader.readLine()) != null) {
//            requestBody.append(line);
//        }
//        return requestBody.toString();
//    }
//}
