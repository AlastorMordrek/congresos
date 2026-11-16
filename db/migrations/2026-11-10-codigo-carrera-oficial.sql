-- -----------------------------------------------------------------------------
-- Migracion: codigo de carrera oficial del TecNM.
--
-- El campo USUARIO.codigo_carrera paso de varchar(3) a varchar(13) para
-- almacenar la clave oficial completa, por ejemplo "ISIC-2010-224".
--
-- Esta migracion NO se ejecuta automaticamente. La aplicacion usa
-- spring.jpa.hibernate.ddl-auto=update y no cuenta con Flyway ni Liquibase,
-- por lo que el script se aplica manualmente en cada entorno.
--
-- Fecha: 2026-11-10
-- -----------------------------------------------------------------------------

BEGIN;

-- 1. Ampliar la columna. El aumento de longitud en un varchar no reescribe la
--    tabla en PostgreSQL, solo actualiza el catalogo.
ALTER TABLE usuario
  ALTER COLUMN codigo_carrera TYPE varchar(13);

-- 2. Normalizar los valores historicos capturados a mano. Las claves oficiales
--    ya migradas se conservan; los valores ambiguos o sin correspondencia
--    segura se limpian a NULL.
UPDATE usuario
SET codigo_carrera = CASE
  WHEN trim(codigo_carrera) ~ '^[A-Z]{4}-[0-9]{4}-[0-9]{3}$' THEN trim(codigo_carrera)

  WHEN lower(trim(codigo_carrera)) IN ('isc', '224') THEN 'ISIC-2010-224'
  WHEN lower(trim(codigo_carrera)) = 'ind'           THEN 'IIND-2010-227'
  WHEN lower(trim(codigo_carrera)) IN ('inf', '220', 'if', 'if4', 'ifb', 'ifc', 'iff')
                                                     THEN 'IINF-2010-220'
  WHEN lower(trim(codigo_carrera)) = 'tic'           THEN 'ITIC-2010-225'
  WHEN lower(trim(codigo_carrera)) IN ('ia', '248')  THEN 'IIAR-2024-248'
  WHEN lower(trim(codigo_carrera)) = '234'           THEN 'LADM-2010-234'
  WHEN lower(trim(codigo_carrera)) = '210'           THEN 'IEME-2010-210'
  WHEN lower(trim(codigo_carrera)) = '213'           THEN 'IADM-2010-213'
  WHEN lower(trim(codigo_carrera)) IN ('icb', 'ics') THEN 'ICBS-2024-249'
  WHEN lower(trim(codigo_carrera)) = 'ie'            THEN 'IELC-2010-211'
  WHEN lower(trim(codigo_carrera)) = 'bio'           THEN 'IBQA-2010-207'

  ELSE NULL
END
WHERE codigo_carrera IS NOT NULL;

COMMIT;

-- Verificacion posterior:
--   SELECT character_maximum_length
--   FROM information_schema.columns
--   WHERE table_name = 'usuario' AND column_name = 'codigo_carrera';
--
--   SELECT codigo_carrera, count(*)
--   FROM usuario
--   GROUP BY codigo_carrera
--   ORDER BY 2 DESC;
