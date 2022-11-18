package com.vttp.companionprojectbackend.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vttp.companionprojectbackend.models.Post;
import com.vttp.companionprojectbackend.services.PostService;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

@RestController
@RequestMapping(path="/post")
public class PostRestController {

    @Autowired
    private PostService postSvc;
    
    @PostMapping(path="/addPost",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadPost(@RequestPart(required=false) MultipartFile image, @RequestPart String title,
        @RequestPart String date, @RequestPart String text, @RequestPart(required=false) String lat, @RequestPart(required=false) String lng, 
        @RequestPart(required=false) String hashtags, @RequestPart String username){
            Post post = new Post();
            post.setImage(image);
            post.setTitle(title);
            post.setDate(date);
            post.setText(text);
            post.setLat(lat);
            post.setLng(lng);
            post.setUsername(username);
            post.setHashtags(Arrays.asList(hashtags.split(",")));

            if(postSvc.insertPost(post)){
                JsonObject message = Json.createObjectBuilder()
                .add("status",true)
                .add("message", "Post successfully added!")
                .build();
                return ResponseEntity.ok(message.toString());
            }else{
                JsonObject result = Json.createObjectBuilder()
                .add("status",false)
                .add("message", "There was an error adding post, please try again.")
                .build();
            return ResponseEntity.status(503).body(result.toString());
            }
    }


    @PostMapping(path="/test",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> test(@RequestPart MultipartFile myFile){
        System.out.println("something good happened");
        return null;
    }

    // For now let's just return the title
    @GetMapping(path="/getPostTitles")
    public ResponseEntity<String> getPostTitle(@RequestParam String date, @RequestParam String username){
        List<String> postTitles = new ArrayList<>();
        postTitles = postSvc.getPostTitles(date, username);

        // JsonObject data = Json.createObjectBuilder()
        //     .add("postTitles",postTitles)
        //     .build();
        System.out.println("List was retrieved");

        JsonArrayBuilder bobTheBuilder = Json.createArrayBuilder();
        for(String title:postTitles){
            bobTheBuilder.add(title);
        }

        return ResponseEntity.ok(bobTheBuilder.build().toString());
    }

    // Return post with date and username 
    @GetMapping(path="/getPosts")
    public ResponseEntity<String> getPosts(@RequestParam String date, @RequestParam String username){
        System.out.println("getPosts API was called");
        List<Post> posts = new ArrayList<>();
        posts = postSvc.getPosts(date, username);

        JsonArrayBuilder bobTheBuilder = Json.createArrayBuilder();
        for(Post post:posts){
            bobTheBuilder.add(post.toJson());
        }
        
        return ResponseEntity.ok(bobTheBuilder.build().toString());
    }

    @GetMapping(path="/getIndividualPost")
    public ResponseEntity<String> getIndividualPost(@RequestParam Integer post_id,@RequestParam String username){
        System.out.println("getIndividualPost API was called");
        Post post = new Post();
        post=postSvc.getIndividualPost(post_id,username);

        return ResponseEntity.ok(post.toJson().toString());
    }

    // Get all post for a particular user 
    @GetMapping(path="/getPostsForUser")
    public ResponseEntity<String> getPostsForUser(@RequestParam String username){
        System.out.println("getPostsForUser API was called");
        List<Post> posts = new ArrayList<>();
        posts = postSvc.getPostsForUser(username);

        JsonArrayBuilder bobTheBuilder = Json.createArrayBuilder();
        for(Post post:posts){
            bobTheBuilder.add(post.toJson());
        }
        
        return ResponseEntity.ok(bobTheBuilder.build().toString());
    }

    @DeleteMapping(path="/deletePost")
    public ResponseEntity<String> deletePost(@RequestParam Integer post_id){
        System.out.println("deletePost API was called");

        
        if(postSvc.deletePost(post_id)){
            JsonObject message = Json.createObjectBuilder()
            .add("status",true)
            .add("message", "Post successfully deleted!")
            .build();
            return ResponseEntity.ok(message.toString());
        }else{
            JsonObject result = Json.createObjectBuilder()
            .add("status",false)
            .add("message", "There was an error deleting post, please try again.")
            .build();
        return ResponseEntity.status(503).body(result.toString());
        }
        
    }
}
