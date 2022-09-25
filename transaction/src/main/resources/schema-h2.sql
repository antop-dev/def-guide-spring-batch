create table account_summary
(
    id              int            not null auto_increment,
    account_number  varchar(10)    not null,
    current_balance decimal(10, 2) not null,
    primary key (id)
);

create table transaction
(
    id                 int           not null auto_increment,
    timestamp          timestamp     not null,
    amount             decimal(8, 2) not null,
    account_summary_id int           not null,
    primary key (id)
);

alter table transaction
    add foreign key (account_summary_id)
        references account_summary (id);
