use companionProject;

create table Hashtag(
	hashtag_id int not null auto_increment,
    hashtag varchar(256),
    
    unique(hashtag),
    
    primary key(hashtag_id)
    
    );