package org.khatri.sto.ambassador.controller;

import org.khatri.sto.ambassador.annotations.OpenEndPoint;
import org.khatri.sto.ambassador.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Ankit Khatri
 */

@RestController
@RequestMapping("/ans")
public class AnswerController {

    @Autowired private AnswerService answerService;

    @PostMapping("/write/{quesId}")
    public ResponseEntity<?> answerToQuestion(@PathVariable String quesId, @RequestBody Object request){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.answerService.writeAnswerToQuestion(quesId, request));
    }

    @OpenEndPoint
    @GetMapping("/get/{quesId}")
    public ResponseEntity<?> getAnswerOfQuestion(@PathVariable String quesId,
                                                 @RequestParam(defaultValue = "0") String page,
                                                 @RequestParam(defaultValue = "5") String size,
                                                 @RequestParam(defaultValue = "createdAt") String sort,
                                                 @RequestParam(defaultValue = "desc") String direction){
        return ResponseEntity.ok(this.answerService.getAnswerOfQuestion(page, size, sort, direction, quesId));
    }

    @PutMapping("/modify/{answerId}")
    public ResponseEntity<?> modifyAnswer(@PathVariable String answerId, @RequestBody Object request){
        return ResponseEntity.ok(this.answerService.modifyAnswer(answerId, request));
    }

    @PostMapping("/write/replies/{answerId}")
    public ResponseEntity<?> writeRepliesToAnswer(@PathVariable String answerId,
                                                  @RequestBody Object request) {
        return ResponseEntity.ok(this.answerService.writeRepliesToAnswer(answerId, request));
    }

    @OpenEndPoint
    @GetMapping("/get-replies/{answerId}")
    public ResponseEntity<?> getRepliesOfAnswer(@PathVariable String answerId,
                                                @RequestParam(defaultValue = "0") String page,
                                                @RequestParam(defaultValue = "5") String size,
                                                @RequestParam(defaultValue = "createdAt") String sort,
                                                @RequestParam(defaultValue = "desc") String direction){
        return ResponseEntity.ok(this.answerService.getRepliesOfAnswer(page, size, sort, direction, answerId));
    }

    @PostMapping("/suggest-vote/{answerId}")
    public ResponseEntity<?> suggestVote(@PathVariable String answerId, @RequestParam("voteType") String voteType){
        this.answerService.suggestVoteOnAnswer(answerId, voteType);
        return ResponseEntity.ok().build();
    }

}
