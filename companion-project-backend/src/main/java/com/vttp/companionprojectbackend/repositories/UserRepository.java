package com.vttp.companionprojectbackend.repositories;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.vttp.companionprojectbackend.models.User;

@Repository
public class UserRepository {
    
    @Autowired
    private JdbcTemplate template;

    private static final String SQL_SELECT_AND_COUNT_USERS_BY_USERNAME_AND_PASSWORD="select count(*) as user_count from user where username=? and password=sha1(?)";

    private static final String SQL_GET_NAME_BY_USERNAME="select name from user where username=?";

    private static final String SQL_GET_USERID_BY_USERNAME="select user_id from user where username=?";

    private static final String SQL_INSERT_USER="insert into user(name, email, username, password) values(?,?,?,sha1(?))";


    public boolean insertNewUser(String name, String email, String username, String password){
        int count = 0;
        try{
            count = template.update(SQL_INSERT_USER, name, email, username, password);
        } catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return 1==count; 
    }

    public int authenticateUser(String username, String password){
        SqlRowSet rs = template.queryForRowSet(SQL_SELECT_AND_COUNT_USERS_BY_USERNAME_AND_PASSWORD,username,password);
        if(!rs.next())
            return 0;
        return rs.getInt("user_count");
    }

    public Optional<String> getNameFromUsername(String username){
        SqlRowSet rs = template.queryForRowSet(SQL_GET_NAME_BY_USERNAME, username);
        if(!rs.next())
            return Optional.empty();
        return Optional.of(rs.getString("name"));
    }

    public Optional<Integer> getUserIdWithUsername(String username){
        SqlRowSet rs = template.queryForRowSet(SQL_GET_USERID_BY_USERNAME, username);
        if(!rs.next())
            return Optional.empty();
        return Optional.of(rs.getInt("user_id"));
    }
}
