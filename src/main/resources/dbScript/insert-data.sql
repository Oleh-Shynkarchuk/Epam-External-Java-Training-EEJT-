insert into tags values (default , 'testTag1')
insert into tags values (default , 'testTag2')
insert into tags values (default , 'testTag3')

insert into certificates values (default , 'testCertificate1','first description',210.24,10,'2022-12-24 12:51:55','2022-12-24 12:51:55')
insert into certificates values (default , 'testCertificate2','second description',145.33,5,'2022-12-24 23:51:55','2022-12-24 23:51:55')


insert into certificates_has_tags values (1,1)
insert into certificates_has_tags values (1,2)
insert into certificates_has_tags values (2,2)
insert into certificates_has_tags values (2,3)
