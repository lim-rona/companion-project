use companionProject;

create table Post(
	post_id int not null auto_increment,
    picture mediumblob,
    title varchar(256),
    date date,
    text varchar(256),
    lat varchar(256),
    lng varchar(256),
    
    user_id int,
    
    primary key(post_id),
    
     constraint fk_post_id
		foreign key(user_id)
        references user(user_id)
);