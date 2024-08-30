package org.khatri.sto.ambassador.controller;

import org.khatri.sto.ambassador.annotations.OpenEndPoint;
import org.khatri.sto.ambassador.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Ankit Khatri
 */

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired private UserService userService;

    @OpenEndPoint
    @PostMapping("/register")
    public ResponseEntity<?> registerNewUser(@RequestBody Object request){
        return ResponseEntity.ok(this.userService.registerNewUser(request));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> fetchAllUsers(@PathVariable Long userId){
        return ResponseEntity.ok(this.userService.fetchUserById(userId));
    }

    @GetMapping("/fetch-all")
    public ResponseEntity<?> fetchAllUsers(){
        return ResponseEntity.ok(this.userService.fetchAllUsers());
    }
}


