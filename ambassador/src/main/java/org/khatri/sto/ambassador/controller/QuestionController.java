package org.khatri.sto.ambassador.controller;

import jakarta.validation.Valid;
import org.khatri.sto.ambassador.annotations.OpenEndPoint;
import org.khatri.sto.ambassador.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Ankit Khatri
 */

@RestController
@RequestMapping("/ques")
public class QuestionController {

    @Autowired private QuestionService questionService;

    @PostMapping("/create")
    public ResponseEntity<?> createNewQuestion(@Valid @RequestBody Object request){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.questionService.createNewQuestion(request));
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllQuestionsOfUser(@RequestParam(defaultValue = "0") String page,
                                                   @RequestParam(defaultValue = "5") String size,
                                                   @RequestParam(defaultValue = "createdAt") String sort,
                                                   @RequestParam(defaultValue = "desc") String direction){
        return ResponseEntity.ok(this.questionService.getAllUserQuestions(page, size, sort, direction));
    }

    @OpenEndPoint
    @GetMapping("/get/{quesId}")
    public ResponseEntity<?> getQuestionById(@PathVariable String quesId){
        return ResponseEntity.ok(this.questionService.findQuestionById(quesId));
    }

    @PutMapping("/modify/{quesId}")
    public ResponseEntity<?> updateExistingQuestion(@PathVariable String quesId,
                                                    @Valid @RequestBody Object request){
        return ResponseEntity.ok(this.questionService.modifyExistingQuestion(quesId, request));
    }

    @DeleteMapping("/delete/{quesId}")
    public ResponseEntity<?> deleteQuestionId(@PathVariable String quesId){
        this.questionService.deleteQuestion(quesId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @OpenEndPoint
    @GetMapping("/extract-all")
    public ResponseEntity<?> getAllQuestions(){
        return ResponseEntity.ok(this.questionService.findAllQuestions());
    }

    @PostMapping("/suggest-vote/{quesId}")
    public ResponseEntity<?> suggestVote(@PathVariable String quesId, @RequestParam("voteType") String voteType){
        this.questionService.suggestVoteOnQuestion(quesId, voteType);
        return ResponseEntity.ok().build();
    }

    @OpenEndPoint
    @GetMapping("/search")
    public ResponseEntity<?> searchQuestion(@RequestParam("query") String query,
                                            @RequestParam(defaultValue = "0") String page,
                                            @RequestParam(defaultValue = "5") String size,
                                            @RequestParam(defaultValue = "createdAt") String sort,
                                            @RequestParam(defaultValue = "desc") String direction){
        return ResponseEntity.ok(this.questionService.searchQuestion(page, size, sort, direction, query));
    }

    @OpenEndPoint
    @PostMapping("/search-by-tags")
    public ResponseEntity<?> searchQuestionByTags(@RequestBody Object request){
        return ResponseEntity.ok(this.questionService.searchQuestionByTags(request));
    }
}
