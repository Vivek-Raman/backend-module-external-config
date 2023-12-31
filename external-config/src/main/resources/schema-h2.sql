create table if not exists external_config
(
    id uuid not null default random_uuid(),
    config_key character varying not null,
    config_value character varying not null,
    notes character varying null,
    constraint external_config_pkey primary key (id)
);

create unique index if not exists unique_config_key on external_config (config_key);

INSERT INTO external_config (config_key, config_value)
SELECT 'db_initialized', 'sure'
WHERE NOT EXISTS (SELECT config_key
                  FROM external_config
                  WHERE config_key = 'db_initialized');
