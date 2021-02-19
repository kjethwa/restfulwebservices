insert into client_configuration_setting (id,no_of_tokens_per_quarter) values ( '64f2ab89-141d-4cd5-8bd7-f1fac3b18c35',3);

insert into client(client_id,city,client_category,client_name,country,status,client_config_id) values ('64f2ab89-141d-4cd5-8bd7-f1fac3b18c00','Mumbai','CLINIC','Dr. Mehta','India','ACTIVE','64f2ab89-141d-4cd5-8bd7-f1fac3b18c35');

insert into client_operation (operation_id,day,from_time,no_of_tokens,to_time,client_id) values ( '64f2ab89-141d-4cd5-8bd7-f1fac3b18c01',1,'09:00',2,'13:00','64f2ab89-141d-4cd5-8bd7-f1fac3b18c00');
insert into client_operation (operation_id,day,from_time,no_of_tokens,to_time,client_id) values ( '64f2ab89-141d-4cd5-8bd7-f1fac3b18c02',2,'09:00',2,'13:00','64f2ab89-141d-4cd5-8bd7-f1fac3b18c00');
insert into client_operation (operation_id,day,from_time,no_of_tokens,to_time,client_id) values ( '64f2ab89-141d-4cd5-8bd7-f1fac3b18c03',3,'09:00',2,'13:00','64f2ab89-141d-4cd5-8bd7-f1fac3b18c00');
insert into client_operation (operation_id,day,from_time,no_of_tokens,to_time,client_id) values ( '64f2ab89-141d-4cd5-8bd7-f1fac3b18c04',4,'09:00',2,'13:00','64f2ab89-141d-4cd5-8bd7-f1fac3b18c00');
insert into client_operation (operation_id,day,from_time,no_of_tokens,to_time,client_id) values ( '64f2ab89-141d-4cd5-8bd7-f1fac3b18c05',5,'09:00',2,'13:00','64f2ab89-141d-4cd5-8bd7-f1fac3b18c00');
insert into client_operation (operation_id,day,from_time,no_of_tokens,to_time,client_id) values ( '64f2ab89-141d-4cd5-8bd7-f1fac3b18c06',6,'09:00',2,'13:00','64f2ab89-141d-4cd5-8bd7-f1fac3b18c00');
insert into client_operation (operation_id,day,from_time,no_of_tokens,to_time,client_id) values ( '64f2ab89-141d-4cd5-8bd7-f1fac3b18c07',0,'09:00',2,'13:00','64f2ab89-141d-4cd5-8bd7-f1fac3b18c00');

insert into client_operation (operation_id,day,from_time,no_of_tokens,to_time,client_id) values ( '64f2ab89-141d-4cd5-8bd7-f1fac3b18c08',1,'18:00',1,'21:30','64f2ab89-141d-4cd5-8bd7-f1fac3b18c00');
insert into client_operation (operation_id,day,from_time,no_of_tokens,to_time,client_id) values ( '64f2ab89-141d-4cd5-8bd7-f1fac3b18c09',2,'18:00',1,'21:30','64f2ab89-141d-4cd5-8bd7-f1fac3b18c00');
insert into client_operation (operation_id,day,from_time,no_of_tokens,to_time,client_id) values ( '64f2ab89-141d-4cd5-8bd7-f1fac3b18c10',3,'18:00',1,'21:30','64f2ab89-141d-4cd5-8bd7-f1fac3b18c00');
insert into client_operation (operation_id,day,from_time,no_of_tokens,to_time,client_id) values ( '64f2ab89-141d-4cd5-8bd7-f1fac3b18c11',4,'18:00',1,'21:30','64f2ab89-141d-4cd5-8bd7-f1fac3b18c00');
insert into client_operation (operation_id,day,from_time,no_of_tokens,to_time,client_id) values ( '64f2ab89-141d-4cd5-8bd7-f1fac3b18c12',5,'18:00',1,'21:30','64f2ab89-141d-4cd5-8bd7-f1fac3b18c00');
insert into client_operation (operation_id,day,from_time,no_of_tokens,to_time,client_id) values ( '64f2ab89-141d-4cd5-8bd7-f1fac3b18c13',6,'18:00',1,'21:30','64f2ab89-141d-4cd5-8bd7-f1fac3b18c00');
insert into client_operation (operation_id,day,from_time,no_of_tokens,to_time,client_id) values ( '64f2ab89-141d-4cd5-8bd7-f1fac3b18c14',0,'18:00',1,'21:30','64f2ab89-141d-4cd5-8bd7-f1fac3b18c00');

/*insert into user_details(user_id,full_name,login_id,password,phone_number,status,client_id) values('64f2ab89-141d-4cd5-8bd7-f1fac3b18c29','Kalpesh Jethwa','kalpeshjethwa92@gmail.com','$2a$10$tZIXnqfNOX5hdtwLUfHm9u4CjbryhUNV1oKgWAv59FglMjMG/75/q',9594267520,'ACTIVE','64f2ab89-141d-4cd5-8bd7-f1fac3b18c00');
insert into user_role(id,role,user_id) values ( '64f2ab89-141d-4cd5-8bd7-f1fac3b18c30','ROLE_ADMIN','64f2ab89-141d-4cd5-8bd7-f1fac3b18c29');
*/
-- insert into user_details(user_id,full_name,login_id,client_id) values('64f2ab89-141d-4cd5-8bd7-f1fac3b18c31','Test User1','714d3bbf-00b4-4c7e-9592-a34326852dd6','64f2ab89-141d-4cd5-8bd7-f1fac3b18c00');

