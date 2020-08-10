create user t2tierp with encrypted password 't2t1Erp*';
create database t2tierp with owner t2tierp encoding 'UTF-8';
alter schema public owner to t2tierp;