drop schema if exists companionProject;

create schema companionProject;

use companionProject;

create table user(
    user_id int not null auto_increment,
    name varchar(256),
    username varchar(256),
    password varchar(256),
    
    unique(username),

    primary key(user_id)
);