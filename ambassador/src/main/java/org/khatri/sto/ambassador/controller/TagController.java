package org.khatri.sto.ambassador.controller;

import jakarta.validation.Valid;
import org.khatri.sto.ambassador.annotations.OpenEndPoint;
import org.khatri.sto.ambassador.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Ankit Khatri
 */

@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired private TagService tagService;

    @PostMapping("/mapping/{refId}")
    public ResponseEntity<?> createQuestionWithTags(@PathVariable String refId,
                                                    @RequestParam("refType") String refType,
                                                    @RequestBody List<String> tagNames) {
        return ResponseEntity.ok(this.tagService.mapTagsWithRefId(refId, refType, tagNames));
    }

    @OpenEndPoint
    @PostMapping("/search")
    public ResponseEntity<?> searchByTag(@Valid @RequestBody Object request){
        return ResponseEntity.ok(this.tagService.searchByTags(request));
    }

    @OpenEndPoint
    @GetMapping("/find/{refId}")
    public ResponseEntity<?> findTagWithRefId(@PathVariable String refId,
                                              @RequestParam("refType") String refType){
        return ResponseEntity.ok(this.tagService.findTagWithRefId(refId, refType));
    }
}
