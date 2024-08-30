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
public class QuestionService {

    @Value("${qna.service.url}")
    private String qnaBaseUrl;

    @Autowired private RestService restService;

    private ExHttpRequest.ExHttpRequestBuilder getBasicExHttpRequest(final String path, final HttpMethod httpMethod, final Object payload){
        return ExHttpRequest.builder().system(ExternalSystem.QNA_SERVICE).httpMethod(httpMethod)
                .uri(this.qnaBaseUrl.concat(path)).payload(payload);
    }

    public Object createNewQuestion(Object request) {
        ExHttpRequest exHttpRequest = this.getBasicExHttpRequest("/ques/create", HttpMethod.POST, request)
                .headers(new HashMap<>(){{put(Constants.USER_ID, MDC.get(Constants.USER_ID));}}).build();
        return this.restService.execute(exHttpRequest, Object.class);
    }

    public Object getAllUserQuestions(String page, String size, String sort, String direction) {
        String url = "/ques/get-all".concat("?page=").concat(page).concat("&size=").concat(size).concat("&sort=").concat(sort).concat("&direction=").concat(direction);
        ExHttpRequest exHttpRequest = this.getBasicExHttpRequest(url, HttpMethod.GET, null)
                .headers(new HashMap<>(){{put(Constants.USER_ID, MDC.get(Constants.USER_ID));}}).build();
        return this.restService.execute(exHttpRequest, Object.class);
    }

    public Object findQuestionById(String quesId) {
        ExHttpRequest exHttpRequest = this.getBasicExHttpRequest("/ques/get/".concat(quesId), HttpMethod.GET, null)
                .headers(new HashMap<>(){{put(Constants.USER_ID, MDC.get(Constants.USER_ID));}}).build();
        return this.restService.execute(exHttpRequest, Object.class);
    }

    public Object modifyExistingQuestion(String quesId, Object request) {
        ExHttpRequest exHttpRequest = this.getBasicExHttpRequest("/ques/modify/".concat(quesId), HttpMethod.PUT, request)
                .headers(new HashMap<>(){{put(Constants.USER_ID, MDC.get(Constants.USER_ID));}}).build();
        return this.restService.execute(exHttpRequest, Object.class);
    }

    public void deleteQuestion(String quesId) {
        ExHttpRequest exHttpRequest = this.getBasicExHttpRequest("/ques/delete/".concat(quesId), HttpMethod.DELETE, null)
                .headers(new HashMap<>(){{put(Constants.USER_ID, MDC.get(Constants.USER_ID));}}).build();
        this.restService.execute(exHttpRequest, Void.class);
    }

    public Object findAllQuestions() {
        ExHttpRequest exHttpRequest = this.getBasicExHttpRequest("/ques/extract-all", HttpMethod.GET, null)
                .headers(new HashMap<>(){{put(Constants.USER_ID, MDC.get(Constants.USER_ID));}}).build();
        return this.restService.execute(exHttpRequest, Object.class);
    }

    public void suggestVoteOnQuestion(String quesId, String voteType) {
        String url = "/ques/suggest-vote/".concat(quesId).concat("?voteType=").concat(voteType);
        ExHttpRequest exHttpRequest = this.getBasicExHttpRequest(url, HttpMethod.POST, null)
                .headers(new HashMap<>(){{put(Constants.USER_ID, MDC.get(Constants.USER_ID));}}).build();
        this.restService.execute(exHttpRequest, Object.class);
    }

    public Object searchQuestion(String page, String size, String sort, String direction, String query) {
        String url = "/ques/search".concat("?page=").concat(page).concat("&size=").concat(size).concat("&sort=").concat(sort).concat("&direction=").concat(direction)
                .concat("&query=").concat(query);
        ExHttpRequest exHttpRequest = this.getBasicExHttpRequest(url, HttpMethod.GET, null)
                .headers(new HashMap<>(){{put(Constants.USER_ID, MDC.get(Constants.USER_ID));}}).build();
        return this.restService.execute(exHttpRequest, Object.class);
    }

    public Object searchQuestionByTags(Object request) {
        String url = "/ques/search-by-tags";
        ExHttpRequest exHttpRequest = this.getBasicExHttpRequest(url, HttpMethod.POST, request)
                .headers(new HashMap<>(){{put(Constants.USER_ID, MDC.get(Constants.USER_ID));}}).build();
        return this.restService.execute(exHttpRequest, Object.class);
    }
}
