package com.vttp.companionprojectbackend.controllers;

import java.util.Optional;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vttp.companionprojectbackend.services.UserService;

import jakarta.json.Json;
import jakarta.json.JsonObject;


@RestController
@RequestMapping
public class LoginRestController {

    @Autowired
    private UserService userSvc;

    // for signups
    @GetMapping(path="/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> newSignup(@RequestParam String name, @RequestParam String email,
        @RequestParam String username, @RequestParam String password){

            boolean result = userSvc.signupNewUser(name,email,username,password);
            
            if(result==false){
                JsonObject message = Json.createObjectBuilder()
                .add("status", false)
                .add("message","Sign up failed, please try again :(")
                .build();
            return ResponseEntity.status(404).body(message.toString());
            }

            JsonObject message = Json.createObjectBuilder()
                .add("status", true)
                .add("message","Sign up success!")
                .build();
                
            return ResponseEntity.ok(message.toString());
    }
    
    // verify whether user exists w username and password and return name of user
    @GetMapping(path="/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> verifyLogin(@RequestParam String username, @RequestParam String password){
        
        Optional<String> optName = userSvc.verifyUserAndGetName(username, password);

        if(optName.isEmpty()){
            JsonObject result = Json.createObjectBuilder()
                .add("status",false)
                .build();
            return ResponseEntity.status(404).body(result.toString());
        }

        String name = optName.get();

        JsonObject result = Json.createObjectBuilder()
                .add("status",true)
                .add("name", name)
                .add("message","Login success!")
                .build();

        return ResponseEntity.ok(result.toString());
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("hello");
    }
}
