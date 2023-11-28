create table if not exists external_config
(
    id uuid not null default random_uuid(),
    config_key character varying not null,
    config_value character varying not null,
    notes character varying null,
    constraint external_config_pkey primary key (id)
);
