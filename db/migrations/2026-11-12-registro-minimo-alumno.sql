-- -----------------------------------------------------------------------------
-- Migracion: registro minimo de ALUMNO con solo numero de control.
--
-- Permite que un ALUMNO creado por el personal autorizado exista sin email,
-- telefono ni fecha de nacimiento. El nombre se crea con un valor temporal y
-- el password se genera en el backend.
--
-- Esta migracion NO se ejecuta automaticamente. La aplicacion usa
-- spring.jpa.hibernate.ddl-auto=update y no cuenta con Flyway ni Liquibase,
-- por lo que el script se aplica manualmente en cada entorno.
--
-- Fecha: 2026-11-12
-- -----------------------------------------------------------------------------

BEGIN;

ALTER TABLE usuario ALTER COLUMN email DROP NOT NULL;
ALTER TABLE usuario ALTER COLUMN fecha_nacimiento DROP NOT NULL;
ALTER TABLE usuario ALTER COLUMN tel_pref DROP NOT NULL;
ALTER TABLE usuario ALTER COLUMN tel_suf DROP NOT NULL;

COMMIT;

-- Verificacion posterior:
--   SELECT column_name, is_nullable
--   FROM information_schema.columns
--   WHERE table_name = 'usuario'
--     AND column_name IN
--       ('email', 'fecha_nacimiento', 'tel_pref', 'tel_suf');
--
-- Las cuatro columnas deben mostrar is_nullable = 'YES'.
