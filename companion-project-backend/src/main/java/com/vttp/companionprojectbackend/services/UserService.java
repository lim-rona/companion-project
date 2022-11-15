package com.vttp.companionprojectbackend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vttp.companionprojectbackend.models.User;
import com.vttp.companionprojectbackend.repositories.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepo;

    // For Signup
    public boolean signupNewUser(String name, String email, String username,String password){
        return userRepo.insertNewUser(name, email, username, password);
    }

    // For Login
    // For now, return Optional<String> can edit to Opt<User> if extra details required 
    public Optional<String> verifyUserAndGetName(String username, String password){

        if (1==userRepo.authenticateUser(username, password)){
            return userRepo.getNameFromUsername(username);
        } else{
            return Optional.empty();
        }
    }

    
}
