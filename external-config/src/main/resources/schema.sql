create table if not exists external_config
(
    id    varchar(128) primary key,
    key   varchar(128) not null,
    value varchar(128) not null,
    notes varchar(128)
);

create unique index if not exists unique_key on external_config (key);

INSERT INTO external_config (key, value)
SELECT 'db_initialized', 'sure'
WHERE NOT EXISTS (
    SELECT id FROM external_config WHERE id = 'db_initialized'
);
