create table account (id bigint not null auto_increment, current_cash float, name varchar(255), original_cash float, user_id bigint, primary key (id)) engine=InnoDB
create table application_user (id bigint not null auto_increment, email_address varchar(255), first_name varchar(255), handle varchar(255), last_name varchar(255), password varchar(255), social_platform_id varchar(255), primary key (id)) engine=InnoDB
create table issuing_company (id bigint not null auto_increment, full_name varchar(255), primary key (id)) engine=InnoDB
create table ticker (id bigint not null auto_increment, exchange varchar(255), symbol varchar(255), issuing_company_id bigint, primary key (id)) engine=InnoDB
create table transaction (id bigint not null auto_increment, share_price float, share_quantity float, transaction_date_time datetime, transaction_type varchar(255), account_id bigint, ticker_id bigint, primary key (id)) engine=InnoDB
alter table account add constraint FKp0j41j7l4h0kv6q8dwxudg2l4 foreign key (user_id) references application_user (id)
alter table ticker add constraint FK8b5lyj4yyb91ah3wrrdobhce6 foreign key (issuing_company_id) references issuing_company (id)
alter table transaction add constraint FK6g20fcr3bhr6bihgy24rq1r1b foreign key (account_id) references account (id)
alter table transaction add constraint FKk9hakev39wbqt0by3lo4o5gjw foreign key (ticker_id) references ticker (id)
