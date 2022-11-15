package com.vttp.companionprojectbackend.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.apache.commons.lang3.ArrayUtils;


import com.mysql.cj.protocol.Resultset;
import com.vttp.companionprojectbackend.models.Post;

@Repository
public class PostRepository {
    
    @Autowired
    private JdbcTemplate template;

    private static final String SQL_INSERT_POST="insert into Post(picture,title,date,text,lat,lng,user_id) values(?,?,?,?,?,?,?)";

    private static final String SQL_SELECT_LAST_INSERT_ID="select LAST_INSERT_ID()";

    private static final String SQL_FIND_EXISTING_HASHTAG="select hashtag_id from Hashtag where hashtag=?";

    private static final String SQL_INSERT_HASHTAG="insert into Hashtag(hashtag) values(?)";

    private static final String SQL_POST_HASHTAG_UPDATE="insert into post_hashtag(post_id,hashtag_id) values(?,?)";

    private static final String SQL_GET_POST_TITLE="select title from Post where date=? and user_id=?";

    private static final String SQL_GET_POSTS="select * from Post where date=? and user_id=?";

    private static final String SQL_GET_COUNT_OF_HASHTAG="select count(*) from post_hashtag where post_id=?";

    private static final String SQL_GET_HASHTAGID_FROM_POSTID="select hashtag_id from post_hashtag where post_id=?";

    private static final String SQL_GET_HASHTAG_FROM_HASHTAGID="select hashtag from Hashtag where hashtag_id=?";

    private static final String SQL_GET_POST_FROM_POSTID="select * from Post where post_id=? and user_id=?";

    private static final String SQL_GET_POSTS_FROM_USERID="select * from Post where user_id=?";


    public boolean insertPost(Post post){
        int updated=0;

        // Inserting post and returning post_id
        try{
            updated=template.update(SQL_INSERT_POST,post.getImage().getInputStream(), post.getTitle(),post.getDate(),post.getText(),post.getLat(),post.getLng(),post.getUser_id());
        }catch(Exception ex){
            ex.printStackTrace();
        }

        return 1==updated;
    }

    public int lastInsertId(){
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_LAST_INSERT_ID);
        if(!rs.next())
            return 0;
        return rs.getInt("LAST_INSERT_ID()");
    }

    public boolean insertHashtags(List<String> hashtags, Integer post_id){

        for (String hashtag : hashtags) {
            System.out.println("Hashtag is: " + hashtag);
            // Check if hashtag exists and return hashtag_id
            SqlRowSet rs = template.queryForRowSet(SQL_FIND_EXISTING_HASHTAG,hashtag);
            if(!rs.next()){ // means not existing,
                System.out.println("Not existing for: " + hashtag);
                template.update(SQL_INSERT_HASHTAG,hashtag);
                // Retrieve the hashtag_id of what you just inserted
                SqlRowSet rs2 = template.queryForRowSet(SQL_SELECT_LAST_INSERT_ID);
                if(rs2.next()){
                    int hashtag_id = rs2.getInt("LAST_INSERT_ID()");
                    // Join the hashtag_id and post_id
                    template.update(SQL_POST_HASHTAG_UPDATE,post_id,hashtag_id);
                }
                
            }else{
                // if existing
                System.out.println("Existing for: " + hashtag);

                int hashtag_id = rs.getInt("hashtag_id");
                template.update(SQL_POST_HASHTAG_UPDATE,post_id,hashtag_id);
            }
            
        }

        return true;
    }

    public List<String> getPostTitles(String date, Integer user_id){
        List<String> postTitles = new ArrayList<>();

        SqlRowSet rs = template.queryForRowSet(SQL_GET_POST_TITLE,date,user_id);
        while(rs.next()){
            String title = rs.getString("title");
            postTitles.add(title);
        }

        return postTitles;
    }

    public List<Post> getPosts(String date, Integer user_id){
        List<Post> posts = new ArrayList<>();

        return template.query(SQL_GET_POSTS,
            (ResultSet rs) -> {
                
                while(rs.next()){
                    Post post = new Post();
                    int post_id = rs.getInt("post_id");
                    post.setPost_id(post_id);
                    post.setPicture(rs.getBytes("picture"));
                    post.setTitle(rs.getString("title"));
                    post.setDate(rs.getDate("date").toString());
                    System.out.println("Date from PostRepo getPosts is: "+rs.getDate("date").toString());
                    post.setText(rs.getString("text"));
                    post.setLat(rs.getString("lat"));
                    post.setLng(rs.getString("lng"));
                    post.setUser_id(rs.getInt("user_id"));

                    // Now to retrieve hashtags
                    post.setHashtags(getHashtags(post_id, user_id));

                    // Now to convert picture into base64
                    if(ArrayUtils.isNotEmpty(post.getPicture())) {
                        String imageBase64 = Base64.getEncoder().encodeToString(post.getPicture());
                        post.setImageBase64(imageBase64);
                    }

                    posts.add(post);
                }
                
                return posts;
            },date,user_id);
    }

    public List<String> getHashtags(Integer post_id, Integer user_id){
        List<String> hashtags = new ArrayList<>();
        int count=0;
        
        SqlRowSet rs = template.queryForRowSet(SQL_GET_COUNT_OF_HASHTAG,post_id);
        if(rs.next()){
            count = rs.getInt("count(*)");
            System.out.println("Hashtag Counts is: "+count);
        }
        
        if(count>0){ // Meaning there are hashtags
            List<Integer> hashtag_ids=new ArrayList<>();
            // Retrieve list of hashtag_ids related to the post 
            SqlRowSet rs2 = template.queryForRowSet(SQL_GET_HASHTAGID_FROM_POSTID,post_id);
            while(rs2.next()){
                System.out.println("Hashtag_ids are: " + rs2.getInt("hashtag_id"));
                hashtag_ids.add(rs2.getInt("hashtag_id"));
            }
            // Find the hashtag related to hashtag id and put into list
            for(Integer hashtag_id:hashtag_ids){
                SqlRowSet rs3=template.queryForRowSet(SQL_GET_HASHTAG_FROM_HASHTAGID,hashtag_id);
                while(rs3.next()){
                    hashtags.add(rs3.getString("hashtag"));
                }
            }
        }
        
        return hashtags;
    }

    public Post getIndividualPost(Integer post_id,Integer user_id){
        Post post = new Post();
        
        return template.query(SQL_GET_POST_FROM_POSTID,
            (ResultSet rs) -> {
                
                while(rs.next()){
                    post.setPost_id(post_id);
                    post.setPicture(rs.getBytes("picture"));
                    post.setTitle(rs.getString("title"));
                    post.setDate(rs.getDate("date").toString());
                    System.out.println("Date from PostRepo getPosts is: "+rs.getDate("date").toString());
                    post.setText(rs.getString("text"));
                    post.setLat(rs.getString("lat"));
                    post.setLng(rs.getString("lng"));
                    post.setUser_id(rs.getInt("user_id"));

                    // Now to retrieve hashtags
                    post.setHashtags(getHashtags(post_id, user_id));

                    // Now to convert picture into base64
                    if(ArrayUtils.isNotEmpty(post.getPicture())) {
                        String imageBase64 = Base64.getEncoder().encodeToString(post.getPicture());
                        post.setImageBase64(imageBase64);
                    }

                }
                
                return post;
            },post_id,user_id);
    }

    public List<Post> getPostsForUser(Integer user_id){
        List<Post> posts = new ArrayList<>();

        return template.query(SQL_GET_POSTS_FROM_USERID,
            (ResultSet rs) -> {
                
                while(rs.next()){
                    Post post = new Post();
                    int post_id = rs.getInt("post_id");
                    post.setPost_id(post_id);
                    post.setPicture(rs.getBytes("picture"));
                    post.setTitle(rs.getString("title"));
                    post.setDate(rs.getDate("date").toString());
                    System.out.println("Date from PostRepo getPosts is: "+rs.getDate("date").toString());
                    post.setText(rs.getString("text"));
                    post.setLat(rs.getString("lat"));
                    post.setLng(rs.getString("lng"));
                    post.setUser_id(rs.getInt("user_id"));

                    // Now to retrieve hashtags
                    post.setHashtags(getHashtags(post_id, user_id));

                    // Now to convert picture into base64
                    if(ArrayUtils.isNotEmpty(post.getPicture())) {
                        String imageBase64 = Base64.getEncoder().encodeToString(post.getPicture());
                        post.setImageBase64(imageBase64);
                    }

                    posts.add(post);
                }
                
                return posts;
            },user_id);

        

    }
    
}
