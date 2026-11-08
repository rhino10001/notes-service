create table if not exists notes (
    id              bigserial                   primary key,
    title           varchar(255)                not null,
    content         text,
    created_at      timestamp with time zone    not null
);

create table if not exists note_tags (
    note_id     bigint          not null,
    tag         varchar(255)    not null,

    constraint pk_note_tags primary key (note_id, tag),
    constraint fk_note_tags_note
        foreign key (note_id) references notes (id)
            on delete cascade
);
