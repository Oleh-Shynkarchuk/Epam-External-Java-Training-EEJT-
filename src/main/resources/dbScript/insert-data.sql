insert into tags values (1,'testTag1')
insert into tags values (2,'testTag2')
insert into tags values (3,'testTag3')

insert into certificates values (1,'testCertificate1','first description',210.24,10,'2022-12-24 12:51:55',null)
insert into certificates values (2,'testCertificate2','second description',145.33,5,'2022-12-24 23:51:55',null)


insert into certificates_has_tags values (1,1)
insert into certificates_has_tags values (1,2)
insert into certificates_has_tags values (2,2)
insert into certificates_has_tags values (2,3)
