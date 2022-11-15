package com.vttp.companionprojectbackend.models;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Post {
    private MultipartFile image;
    private byte[] picture;
    private String title;
    private String date;
    private String text;
    private String lat;
    private String lng;
    private String username;
    private List<String> hashtags;
    private String imageBase64;


    private Integer post_id;

    private Integer user_id;

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public Integer getPost_id() {
        return post_id;
    }

    public void setPost_id(Integer post_id) {
        this.post_id = post_id;
    }

    public List<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    
    public JsonObject toJson(){
        System.out.println("This is hashtag To String: " + hashtags.toString());
        return Json.createObjectBuilder()
            .add("imageBase64",imageBase64)
            .add("title",title)
            .add("date",date)
            .add("text",text)
            .add("lat",lat)
            .add("lng",lng)
            .add("post_id",post_id)
            // .add("username",username)
            .add("hashtags",hashtags.toString()) // Not sure if this works
            .build();
    }

}
