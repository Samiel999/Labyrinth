create table if not exists monster
(
    id                      serial primary key,
    health                  integer,
    position_x              integer,
    position_y              integer,
    type                    varchar(100)
);

create table if not exists monster_list
(
    id         serial primary key,
    monster_id serial references monster
);

create table if not exists monsters_to_spawn
(
    id         serial primary key,
    monster_id serial references monster
);

create table if not exists level
(
    id           serial primary key,
    level_number integer
);

create table if not exists building
(
    id         serial primary key,
    type       varchar(100),
    position_x integer,
    position_y integer,
    health     integer
);

create table if not exists logic_representation
(
    id                   serial primary key,
    building_id          serial references building,
    width                integer,
    height               integer,
    monster_list_id      serial references monster_list,
    monsters_to_spawn_id serial references monsters_to_spawn,
    level_id             serial references level
);

create table if not exists save
(
    id                      serial primary key,
    logic_representation_id serial references logic_representation
);

