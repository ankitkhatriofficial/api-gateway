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

/**
 * @author Ankit Khatri
 */

@Service
public class AnswerService {

    @Value("${qna.service.url}")
    private String qnaBaseUrl;

    @Autowired private RestService restService;

    private ExHttpRequest.ExHttpRequestBuilder getBasicExHttpRequest(final String path, final HttpMethod httpMethod, final Object payload){
        return ExHttpRequest.builder().system(ExternalSystem.QNA_SERVICE).httpMethod(httpMethod)
                .uri(this.qnaBaseUrl.concat(path)).payload(payload);
    }

    public Object writeAnswerToQuestion(String quesId, Object request) {
        ExHttpRequest exHttpRequest = this.getBasicExHttpRequest("/ans/write/".concat(quesId), HttpMethod.POST, request)
                .headers(new HashMap<>(){{put(Constants.USER_ID, MDC.get(Constants.USER_ID));}}).build();
        return this.restService.execute(exHttpRequest, Object.class);
    }

    public Object getAnswerOfQuestion(String page, String size, String sort, String direction, String quesId) {
        String url = "/ans/get/".concat(quesId).concat("?page=").concat(page).concat("&size=").concat(size).concat("&sort=").concat(sort).concat("&direction=").concat(direction);
        ExHttpRequest exHttpRequest = this.getBasicExHttpRequest(url, HttpMethod.GET, null)
                .headers(new HashMap<>(){{put(Constants.USER_ID, MDC.get(Constants.USER_ID));}}).build();
        return this.restService.execute(exHttpRequest, Object.class);
    }

    public Object modifyAnswer(String answerId, Object request) {
        ExHttpRequest exHttpRequest = this.getBasicExHttpRequest("/ans/modify/".concat(answerId), HttpMethod.PUT, request)
                .headers(new HashMap<>(){{put(Constants.USER_ID, MDC.get(Constants.USER_ID));}}).build();
        return this.restService.execute(exHttpRequest, Object.class);
    }

    public Object writeRepliesToAnswer(String answerId, Object request) {
        ExHttpRequest exHttpRequest = this.getBasicExHttpRequest("/ans/write/replies/".concat(answerId), HttpMethod.POST, request)
                .headers(new HashMap<>(){{put(Constants.USER_ID, MDC.get(Constants.USER_ID));}}).build();
        return this.restService.execute(exHttpRequest, Object.class);
    }

    public Object getRepliesOfAnswer(String page, String size, String sort, String direction, String answerId) {
        String url = "/ans/get-replies/".concat(answerId).concat("?page=").concat(page).concat("&size=").concat(size).concat("&sort=").concat(sort).concat("&direction=").concat(direction);
        ExHttpRequest exHttpRequest = this.getBasicExHttpRequest(url, HttpMethod.GET, null)
                .headers(new HashMap<>(){{put(Constants.USER_ID, MDC.get(Constants.USER_ID));}}).build();
        return this.restService.execute(exHttpRequest, Object.class);
    }

    public void suggestVoteOnAnswer(String answerId, String voteType) {
        ExHttpRequest exHttpRequest = this.getBasicExHttpRequest("/ans/suggest-vote/".concat(answerId).concat("?voteType=").concat(voteType), HttpMethod.POST, null)
                .headers(new HashMap<>(){{put(Constants.USER_ID, MDC.get(Constants.USER_ID));}}).build();
        this.restService.execute(exHttpRequest, Object.class);
    }
}
