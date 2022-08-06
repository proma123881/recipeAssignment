create sequence HIBERNATE_SEQUENCE;

DROP TABLE IF EXISTS RECIPE;

create table RECIPE (
    ID bigint auto_increment not null primary key,
    RECIPE_NAME varchar(128) not null,
    IS_VEGETARIAN  bool not null,
    NO_OF_SERVINGS int not null,
    INSTRUCTION varchar(255)
);


create table RECIPE_INGREDIENTS (
    ID bigint not null references RECIPE (ID),
    INGREDIENTS varchar(128) not null
);
