package com.vttp.companionprojectbackend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vttp.companionprojectbackend.models.Post;
import com.vttp.companionprojectbackend.repositories.PostRepository;
import com.vttp.companionprojectbackend.repositories.UserRepository;

@Service
public class PostService {
    
    @Autowired
    private PostRepository postRepo;

    @Autowired
    private UserRepository userRepo;    

    public boolean insertPost(Post post){

        int post_id=0;

        Optional<Integer> opt_user_id = userRepo.getUserIdWithUsername(post.getUsername());
        
        if(opt_user_id.isEmpty()){
            return false;
        }
        post.setUser_id(opt_user_id.get());
        
        if(postRepo.insertPost(post)){
            post_id = postRepo.lastInsertId();
        }

        if(post.getHashtags()!=null){
            return postRepo.insertHashtags(post.getHashtags(),post_id);
        } else{
            return true;
        }
    }

    public List<String> getPostTitles(String date, String username){
        // Some handling could be done here
        Optional<Integer> opt_user_id = userRepo.getUserIdWithUsername(username);
        int user_id = opt_user_id.get();

        return postRepo.getPostTitles(date, user_id);
    }

    public List<Post> getPosts(String date, String username){
        // Some handling could be done here
        Optional<Integer> opt_user_id = userRepo.getUserIdWithUsername(username);
        int user_id = opt_user_id.get();

        return postRepo.getPosts(date, user_id);
    }

    public Post getIndividualPost(Integer post_id,String username){
        Optional<Integer> opt_user_id = userRepo.getUserIdWithUsername(username);
        int user_id = opt_user_id.get();

        return postRepo.getIndividualPost(post_id,user_id);
    } 

    public List<Post> getPostsForUser(String username){
        Optional<Integer> opt_user_id = userRepo.getUserIdWithUsername(username);
        int user_id = opt_user_id.get();

        return postRepo.getPostsForUser(user_id);
    }

    public boolean deletePost(Integer post_id){
        return postRepo.deletePost(post_id);
    }
}
