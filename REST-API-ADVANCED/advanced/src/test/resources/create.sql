insert into tags (name) values ('testTag1')
insert into tags (name) values ('testTag2')
insert into tags (name) values ('testTag3')

insert into certificates (name,description,price,duration,create_date,last_update_date) values ('testCertificate1','first description',210.5,10,'2022-12-24 12:51:55','2022-12-24 12:51:55')
insert into certificates (name,description,price,duration,create_date,last_update_date) values ('testCertificate2','second description',145.3,5,'2022-12-24 23:51:55','2022-12-24 23:51:55')


insert into certificates_has_tags values (1,1)
insert into certificates_has_tags values (1,2)
insert into certificates_has_tags values (2,2)
insert into certificates_has_tags values (2,3)

insert into users (email) values ('testUser1@mail.com')
insert into users (email) values ('testUser2@mail.com')

insert into customer_order (price,purchase,user_id) values (355.8,'2022-12-24 15:51:55',1)
insert into customer_order (price,purchase,user_id) values (145.3,'2022-12-24 17:54:35',2)

insert into order_has_certificate (order_id,certificate_id) values (1,1)
insert into order_has_certificate (order_id,certificate_id) values (1,2)
insert into order_has_certificate (order_id,certificate_id) values (2,2)