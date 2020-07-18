insert into client(client_id,city,client_category,client_name,country,status) values (1,'Mumbai','CLINIC','Dr. Mehta','India','ACTIVE');

insert into client_operation (operation_id,day,from_time,no_of_tokens,to_time,client_client_id) values ( 1,1,'10:00',50,'11:00',1);
insert into client_operation (operation_id,day,from_time,no_of_tokens,to_time,client_client_id) values ( 2,2,'10:00',50,'11:00',1);
insert into client_operation (operation_id,day,from_time,no_of_tokens,to_time,client_client_id) values ( 3,3,'10:00',50,'11:00',1);
insert into client_operation (operation_id,day,from_time,no_of_tokens,to_time,client_client_id) values ( 4,4,'10:00',50,'11:00',1);
insert into client_operation (operation_id,day,from_time,no_of_tokens,to_time,client_client_id) values ( 5,5,'10:00',50,'11:00',1);
insert into client_operation (operation_id,day,from_time,no_of_tokens,to_time,client_client_id) values ( 6,6,'10:00',50,'11:00',1);
insert into client_operation (operation_id,day,from_time,no_of_tokens,to_time,client_client_id) values ( 7,0,'10:00',50,'11:00',1);

insert into user_details(user_id,full_name,login_id,password,phone_number,status) values(1,'Kaplesh Jethwa','kalpeshjethwa92@gmail.com','$2a$10$tZIXnqfNOX5hdtwLUfHm9u4CjbryhUNV1oKgWAv59FglMjMG/75/q',9594267520,'ACTIVE');
insert into user_role(id,role,user_id) values ( 1,'ROLE_USER',1);
