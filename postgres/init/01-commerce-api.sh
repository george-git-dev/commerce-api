#!/bin/sh
# Roda uma única vez, quando o volume do Postgres é criado.
# Usuário próprio da commerce-api com acesso só ao schema "commerce_api" (A7).
# As tabelas quem cria é o Flyway (src/main/resources/db/migration).
set -eu
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<EOSQL
  CREATE ROLE commerce_api LOGIN PASSWORD '${COMMERCE_API_DB_PASSWORD}';
  CREATE SCHEMA commerce_api AUTHORIZATION commerce_api;
  ALTER ROLE commerce_api SET search_path = commerce_api;
  REVOKE ALL ON SCHEMA public FROM PUBLIC;
  REVOKE ALL ON DATABASE ${POSTGRES_DB} FROM PUBLIC;
  GRANT CONNECT ON DATABASE ${POSTGRES_DB} TO commerce_api;
EOSQL
