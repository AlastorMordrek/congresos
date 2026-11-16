-- -----------------------------------------------------------------------------
-- Migracion consolidada: acreditaciones, plantilla de constancia,
-- restriccion por carrera del CONGRESO, firma de la constancia y evidencia
-- de pago del BOLETO.
--
-- Reemplaza a 2026-11-12-acreditaciones.sql y a la correccion intermedia.
-- Produce el estado final en un solo paso, tanto en una base limpia como en
-- una base donde ya se aplico la version vieja con defaults.
--
-- La plantilla de constancia es nullable. El frontend provee los valores
-- sugeridos y el organizador decide si los conserva o los elimina. El backend
-- solo conserva DEFAULT 1 para los creditos, porque es un valor numerico.
--
-- El pie de la constancia usa 10 slots de logos, una franja inferior y una
-- imagen de anio. Las columnas viejas pie_logos, pie_patron y constancia_evento
-- se migran a la imagen de anio y se eliminan.
--
-- No se migran boletos acreditados existentes: el ORGANIZADOR puede
-- desacreditar y volver a acreditar para generar su constancia.
--
-- El nombre del ALUMNO en la constancia se toma del USUARIO al acreditar y se
-- copia al BOLETO. Su limite es varchar(120), igual que boleto.alumno_nombre.
--
-- Fecha: 2026-11-13
-- -----------------------------------------------------------------------------

BEGIN;

-- 1. Plantilla de constancia por CONGRESO.
ALTER TABLE congreso
  ADD COLUMN IF NOT EXISTS constancia_creditos integer NOT NULL DEFAULT 1,
  ADD COLUMN IF NOT EXISTS constancia_instituto varchar(150),
  ADD COLUMN IF NOT EXISTS constancia_subtitulo varchar(150),
  ADD COLUMN IF NOT EXISTS constancia_ciudad varchar(80),
  ADD COLUMN IF NOT EXISTS constancia_estado varchar(80),
  ADD COLUMN IF NOT EXISTS constancia_pais varchar(80),
  ADD COLUMN IF NOT EXISTS constancia_jefa_nombre varchar(100),
  ADD COLUMN IF NOT EXISTS constancia_jefa_cargo varchar(150),
  ADD COLUMN IF NOT EXISTS constancia_jefe_nombre varchar(100),
  ADD COLUMN IF NOT EXISTS constancia_departamento varchar(150),
  ADD COLUMN IF NOT EXISTS constancia_lema1 varchar(150),
  ADD COLUMN IF NOT EXISTS constancia_lema2 varchar(150),
  ADD COLUMN IF NOT EXISTS constancia_pie_texto varchar(500),
  ADD COLUMN IF NOT EXISTS constancia_nota_ccp varchar(100),
  ADD COLUMN IF NOT EXISTS constancia_iniciales varchar(50),
  ADD COLUMN IF NOT EXISTS constancia_actividad varchar(200),
  ADD COLUMN IF NOT EXISTS constancia_nivel_desempeno varchar(100),
  ADD COLUMN IF NOT EXISTS constancia_pie_img_1_mime_type varchar(100),
  ADD COLUMN IF NOT EXISTS constancia_pie_img_1_data bytea,
  ADD COLUMN IF NOT EXISTS constancia_pie_img_2_mime_type varchar(100),
  ADD COLUMN IF NOT EXISTS constancia_pie_img_2_data bytea,
  ADD COLUMN IF NOT EXISTS constancia_pie_img_3_mime_type varchar(100),
  ADD COLUMN IF NOT EXISTS constancia_pie_img_3_data bytea,
  ADD COLUMN IF NOT EXISTS constancia_pie_img_4_mime_type varchar(100),
  ADD COLUMN IF NOT EXISTS constancia_pie_img_4_data bytea,
  ADD COLUMN IF NOT EXISTS constancia_pie_img_5_mime_type varchar(100),
  ADD COLUMN IF NOT EXISTS constancia_pie_img_5_data bytea,
  ADD COLUMN IF NOT EXISTS constancia_pie_img_6_mime_type varchar(100),
  ADD COLUMN IF NOT EXISTS constancia_pie_img_6_data bytea,
  ADD COLUMN IF NOT EXISTS constancia_pie_img_7_mime_type varchar(100),
  ADD COLUMN IF NOT EXISTS constancia_pie_img_7_data bytea,
  ADD COLUMN IF NOT EXISTS constancia_pie_img_8_mime_type varchar(100),
  ADD COLUMN IF NOT EXISTS constancia_pie_img_8_data bytea,
  ADD COLUMN IF NOT EXISTS constancia_pie_img_9_mime_type varchar(100),
  ADD COLUMN IF NOT EXISTS constancia_pie_img_9_data bytea,
  ADD COLUMN IF NOT EXISTS constancia_pie_img_10_mime_type varchar(100),
  ADD COLUMN IF NOT EXISTS constancia_pie_img_10_data bytea,
  ADD COLUMN IF NOT EXISTS constancia_pie_franja_mime_type varchar(100),
  ADD COLUMN IF NOT EXISTS constancia_pie_franja_data bytea,
  ADD COLUMN IF NOT EXISTS constancia_anio_mime_type varchar(100),
  ADD COLUMN IF NOT EXISTS constancia_anio_data bytea;

-- 1.b Restriccion por carrera del CONGRESO.
--     Clave oficial del catalogo de carreras del TecNM. NULL = sin restriccion.
--     La restriccion solo aplica al inscribir ALUMNOS; los BOLETOS existentes
--     no se revalidan.
ALTER TABLE congreso
  ADD COLUMN IF NOT EXISTS codigo_carrera varchar(13),
  ADD COLUMN IF NOT EXISTS constancia_firma_mime_type varchar(100),
  ADD COLUMN IF NOT EXISTS constancia_firma_data bytea;

-- 2. Quitar restricciones de los textos, por si la base ya tenia la version
--    vieja con NOT NULL y DEFAULT.
ALTER TABLE congreso
  ALTER COLUMN constancia_instituto DROP NOT NULL,
  ALTER COLUMN constancia_instituto DROP DEFAULT,
  ALTER COLUMN constancia_subtitulo DROP NOT NULL,
  ALTER COLUMN constancia_subtitulo DROP DEFAULT,
  ALTER COLUMN constancia_ciudad DROP NOT NULL,
  ALTER COLUMN constancia_ciudad DROP DEFAULT,
  ALTER COLUMN constancia_estado DROP NOT NULL,
  ALTER COLUMN constancia_estado DROP DEFAULT,
  ALTER COLUMN constancia_pais DROP NOT NULL,
  ALTER COLUMN constancia_pais DROP DEFAULT,
  ALTER COLUMN constancia_jefa_nombre DROP NOT NULL,
  ALTER COLUMN constancia_jefa_nombre DROP DEFAULT,
  ALTER COLUMN constancia_jefa_cargo DROP NOT NULL,
  ALTER COLUMN constancia_jefa_cargo DROP DEFAULT,
  ALTER COLUMN constancia_jefe_nombre DROP NOT NULL,
  ALTER COLUMN constancia_jefe_nombre DROP DEFAULT,
  ALTER COLUMN constancia_departamento DROP NOT NULL,
  ALTER COLUMN constancia_departamento DROP DEFAULT,
  ALTER COLUMN constancia_lema1 DROP NOT NULL,
  ALTER COLUMN constancia_lema1 DROP DEFAULT,
  ALTER COLUMN constancia_lema2 DROP NOT NULL,
  ALTER COLUMN constancia_lema2 DROP DEFAULT,
  ALTER COLUMN constancia_pie_texto DROP NOT NULL,
  ALTER COLUMN constancia_pie_texto DROP DEFAULT,
  ALTER COLUMN constancia_nota_ccp DROP NOT NULL,
  ALTER COLUMN constancia_nota_ccp DROP DEFAULT,
  ALTER COLUMN constancia_iniciales DROP NOT NULL,
  ALTER COLUMN constancia_iniciales DROP DEFAULT;

-- 3. Limpiar valores que en realidad eran defaults de la version vieja.
UPDATE congreso SET constancia_instituto = NULL
  WHERE constancia_instituto IN (
    'INSTITUTO TECNOLÓGICO DE TIJUANA',
    ''
  );

UPDATE congreso SET constancia_subtitulo = NULL
  WHERE constancia_subtitulo IN (
    'CONSTANCIA DE ACREDITACIÓN DE ACTIVIDAD COMPLEMENTARIA',
    'Constancia de Acreditación de Actividad Complementaria',
    ''
  );

UPDATE congreso SET constancia_ciudad = NULL
  WHERE constancia_ciudad IN ('Tijuana', '');
UPDATE congreso SET constancia_estado = NULL
  WHERE constancia_estado IN ('Baja California', '');
UPDATE congreso SET constancia_pais = NULL
  WHERE constancia_pais IN ('México', '');

UPDATE congreso SET constancia_jefa_nombre = NULL
  WHERE constancia_jefa_nombre IN (
    'MARÍA MAGDALENA SERRANO ORTEGA',
    ''
  );
UPDATE congreso SET constancia_jefa_cargo = NULL
  WHERE constancia_jefa_cargo IN (
    'JEFATURA DEL DEPARTAMENTO DE SERVICIOS ESCOLARES',
    ''
  );
UPDATE congreso SET constancia_jefe_nombre = NULL
  WHERE constancia_jefe_nombre IN ('CLAUDIA SELENE LORETO MEDINA', '');
UPDATE congreso SET constancia_departamento = NULL
  WHERE constancia_departamento IN ('SISTEMAS Y COMPUTACIÓN', '');

UPDATE congreso SET constancia_lema1 = NULL
  WHERE constancia_lema1 IN ('Excelencia en Educación Tecnológica®', '');
UPDATE congreso SET constancia_lema2 = NULL
  WHERE constancia_lema2 IN (
    'Por una Juventud Integrada al Desarrollo de México®',
    ''
  );

UPDATE congreso SET constancia_pie_texto = NULL
  WHERE constancia_pie_texto = ''
    OR constancia_pie_texto IN (
      'Calzada del Tecnológico 12950 esquina Castillo de Chapultepec y ' ||
        'calle Cuauhtemotzin, Fracc. Tomás Aquino C.P. 22414 Tijuana, Baja ' ||
        'California. Tel. 01 (664) 6078400 | dir_tijuana@tecnm.mx | ' ||
        'tecnm.mx | tijuana.tecnm.mx'
    );

UPDATE congreso SET constancia_nota_ccp = NULL
  WHERE constancia_nota_ccp = '';
UPDATE congreso SET constancia_iniciales = NULL
  WHERE constancia_iniciales = '';
UPDATE congreso SET constancia_actividad = NULL
  WHERE constancia_actividad = '';
UPDATE congreso SET constancia_nivel_desempeno = NULL
  WHERE constancia_nivel_desempeno = '';

-- 4. Migrar el antiguo logo de evento a la imagen de anio y eliminar la
--    columna vieja.
DO $$
BEGIN
  IF EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_name = 'congreso'
      AND column_name = 'constancia_evento_img_data'
  ) THEN
    UPDATE congreso
      SET constancia_anio_mime_type = constancia_evento_mime_type
      WHERE constancia_anio_mime_type IS NULL
        AND constancia_evento_mime_type IS NOT NULL;

    UPDATE congreso
      SET constancia_anio_data = constancia_evento_img_data
      WHERE constancia_anio_data IS NULL
        AND constancia_evento_img_data IS NOT NULL;

    ALTER TABLE congreso
      DROP COLUMN IF EXISTS constancia_evento_mime_type,
      DROP COLUMN IF EXISTS constancia_evento_img_data;
  END IF;
END $$;

-- 5. Secuencia de ACREDITACION.
CREATE SEQUENCE IF NOT EXISTS acreditacion_sequence START 1;

-- 6. Tabla ACREDITACION.
CREATE TABLE IF NOT EXISTS acreditacion (
  id                          bigint PRIMARY KEY,
  fecha_creacion              timestamp NOT NULL,
  creador_id                  bigint,

  boleto_id                   bigint NOT NULL,
  boleto_folio                varchar(6),
  boleto_folio_largo          varchar(20),

  alumno_id                   bigint NOT NULL,
  alumno_no_control           varchar(8),
  alumno_nombre               varchar(120),
  carrera_codigo              varchar(13),
  carrera_nombre              varchar(150),

  congreso_id                 bigint NOT NULL,
  congreso_nombre             varchar(100),
  congreso_fecha_inicio       timestamp,
  congreso_fecha_fin          timestamp,

  fecha_emision               date NOT NULL,
  periodo                     varchar(30),
  anio                        integer,
  creditos                    integer,

  instituto                   varchar(150),
  subtitulo                   varchar(150),
  ciudad                      varchar(80),
  estado                      varchar(80),
  pais                        varchar(80),

  jefa_nombre                 varchar(100),
  jefa_cargo                  varchar(150),
  jefe_nombre                 varchar(100),
  departamento                varchar(150),

  lema1                       varchar(150),
  lema2                       varchar(150),
  pie_texto                   varchar(500),
  nota_ccp                    varchar(100),
  iniciales                   varchar(50),

  actividad                   varchar(200),
  nivel_desempeno             varchar(100),

  encabezado_mime_type        varchar(100),
  encabezado_img_data         bytea,

  pie_img_1_mime_type         varchar(100),
  pie_img_1_data              bytea,
  pie_img_2_mime_type         varchar(100),
  pie_img_2_data              bytea,
  pie_img_3_mime_type         varchar(100),
  pie_img_3_data              bytea,
  pie_img_4_mime_type         varchar(100),
  pie_img_4_data              bytea,
  pie_img_5_mime_type         varchar(100),
  pie_img_5_data              bytea,
  pie_img_6_mime_type         varchar(100),
  pie_img_6_data              bytea,
  pie_img_7_mime_type         varchar(100),
  pie_img_7_data              bytea,
  pie_img_8_mime_type         varchar(100),
  pie_img_8_data              bytea,
  pie_img_9_mime_type         varchar(100),
  pie_img_9_data              bytea,
  pie_img_10_mime_type        varchar(100),
  pie_img_10_data             bytea,

  pie_franja_mime_type        varchar(100),
  pie_franja_data             bytea,

  pie_anio_mime_type          varchar(100),
  pie_anio_data               bytea,

  firma_mime_type             varchar(100),
  firma_data                  bytea
);

-- 6.b Limite del nombre del ALUMNO.
--     Coincide con boleto.alumno_nombre (varchar(120)). El nombre completo
--     del USUARIO puede llegar a 122 caracteres; el backend lo recorta antes
--     de guardarlo.
UPDATE acreditacion
SET alumno_nombre = left(alumno_nombre, 120)
WHERE length(alumno_nombre) > 120;

ALTER TABLE acreditacion
  ALTER COLUMN alumno_nombre TYPE varchar(120);

-- 7. Asegurar las columnas nuevas si la tabla ya existia.
ALTER TABLE acreditacion
  ADD COLUMN IF NOT EXISTS pie_img_1_mime_type varchar(100),
  ADD COLUMN IF NOT EXISTS pie_img_1_data bytea,
  ADD COLUMN IF NOT EXISTS pie_img_2_mime_type varchar(100),
  ADD COLUMN IF NOT EXISTS pie_img_2_data bytea,
  ADD COLUMN IF NOT EXISTS pie_img_3_mime_type varchar(100),
  ADD COLUMN IF NOT EXISTS pie_img_3_data bytea,
  ADD COLUMN IF NOT EXISTS pie_img_4_mime_type varchar(100),
  ADD COLUMN IF NOT EXISTS pie_img_4_data bytea,
  ADD COLUMN IF NOT EXISTS pie_img_5_mime_type varchar(100),
  ADD COLUMN IF NOT EXISTS pie_img_5_data bytea,
  ADD COLUMN IF NOT EXISTS pie_img_6_mime_type varchar(100),
  ADD COLUMN IF NOT EXISTS pie_img_6_data bytea,
  ADD COLUMN IF NOT EXISTS pie_img_7_mime_type varchar(100),
  ADD COLUMN IF NOT EXISTS pie_img_7_data bytea,
  ADD COLUMN IF NOT EXISTS pie_img_8_mime_type varchar(100),
  ADD COLUMN IF NOT EXISTS pie_img_8_data bytea,
  ADD COLUMN IF NOT EXISTS pie_img_9_mime_type varchar(100),
  ADD COLUMN IF NOT EXISTS pie_img_9_data bytea,
  ADD COLUMN IF NOT EXISTS pie_img_10_mime_type varchar(100),
  ADD COLUMN IF NOT EXISTS pie_img_10_data bytea,
  ADD COLUMN IF NOT EXISTS pie_franja_mime_type varchar(100),
  ADD COLUMN IF NOT EXISTS pie_franja_data bytea,
  ADD COLUMN IF NOT EXISTS pie_anio_mime_type varchar(100),
  ADD COLUMN IF NOT EXISTS pie_anio_data bytea,
  ADD COLUMN IF NOT EXISTS firma_mime_type varchar(100),
  ADD COLUMN IF NOT EXISTS firma_data bytea;

-- 8. Migrar imagenes viejas del snapshot y eliminar columnas obsoletas.
DO $$
BEGIN
  IF EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_name = 'acreditacion'
      AND column_name = 'evento_img_data'
  ) THEN
    UPDATE acreditacion
      SET pie_anio_mime_type = evento_mime_type
      WHERE pie_anio_mime_type IS NULL
        AND evento_mime_type IS NOT NULL;

    UPDATE acreditacion
      SET pie_anio_data = evento_img_data
      WHERE pie_anio_data IS NULL
        AND evento_img_data IS NOT NULL;

    ALTER TABLE acreditacion
      DROP COLUMN IF EXISTS evento_mime_type,
      DROP COLUMN IF EXISTS evento_img_data;
  END IF;

  IF EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_name = 'acreditacion'
      AND column_name = 'pie_logos_img_data'
  ) THEN
    ALTER TABLE acreditacion
      DROP COLUMN IF EXISTS pie_logos_mime_type,
      DROP COLUMN IF EXISTS pie_logos_img_data;
  END IF;

  IF EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_name = 'acreditacion'
      AND column_name = 'pie_patron_img_data'
  ) THEN
    ALTER TABLE acreditacion
      DROP COLUMN IF EXISTS pie_patron_mime_type,
      DROP COLUMN IF EXISTS pie_patron_img_data;
  END IF;
END $$;

-- 8b. Eliminar columnas duplicadas generadas por nombres camelCase.
ALTER TABLE congreso
  DROP COLUMN IF EXISTS constancia_pie_img1data,
  DROP COLUMN IF EXISTS constancia_pie_img1mime_type,
  DROP COLUMN IF EXISTS constancia_pie_img2data,
  DROP COLUMN IF EXISTS constancia_pie_img2mime_type,
  DROP COLUMN IF EXISTS constancia_pie_img3data,
  DROP COLUMN IF EXISTS constancia_pie_img3mime_type,
  DROP COLUMN IF EXISTS constancia_pie_img4data,
  DROP COLUMN IF EXISTS constancia_pie_img4mime_type,
  DROP COLUMN IF EXISTS constancia_pie_img5data,
  DROP COLUMN IF EXISTS constancia_pie_img5mime_type,
  DROP COLUMN IF EXISTS constancia_pie_img6data,
  DROP COLUMN IF EXISTS constancia_pie_img6mime_type,
  DROP COLUMN IF EXISTS constancia_pie_img7data,
  DROP COLUMN IF EXISTS constancia_pie_img7mime_type,
  DROP COLUMN IF EXISTS constancia_pie_img8data,
  DROP COLUMN IF EXISTS constancia_pie_img8mime_type,
  DROP COLUMN IF EXISTS constancia_pie_img9data,
  DROP COLUMN IF EXISTS constancia_pie_img9mime_type,
  DROP COLUMN IF EXISTS constancia_pie_img10data,
  DROP COLUMN IF EXISTS constancia_pie_img10mime_type;

ALTER TABLE acreditacion
  DROP COLUMN IF EXISTS pie_img1data,
  DROP COLUMN IF EXISTS pie_img1mime_type,
  DROP COLUMN IF EXISTS pie_img2data,
  DROP COLUMN IF EXISTS pie_img2mime_type,
  DROP COLUMN IF EXISTS pie_img3data,
  DROP COLUMN IF EXISTS pie_img3mime_type,
  DROP COLUMN IF EXISTS pie_img4data,
  DROP COLUMN IF EXISTS pie_img4mime_type,
  DROP COLUMN IF EXISTS pie_img5data,
  DROP COLUMN IF EXISTS pie_img5mime_type,
  DROP COLUMN IF EXISTS pie_img6data,
  DROP COLUMN IF EXISTS pie_img6mime_type,
  DROP COLUMN IF EXISTS pie_img7data,
  DROP COLUMN IF EXISTS pie_img7mime_type,
  DROP COLUMN IF EXISTS pie_img8data,
  DROP COLUMN IF EXISTS pie_img8mime_type,
  DROP COLUMN IF EXISTS pie_img9data,
  DROP COLUMN IF EXISTS pie_img9mime_type,
  DROP COLUMN IF EXISTS pie_img10data,
  DROP COLUMN IF EXISTS pie_img10mime_type;

-- 9. Indice unico por boleto.
CREATE UNIQUE INDEX IF NOT EXISTS uk_acreditacion_boleto
  ON acreditacion (boleto_id);

-- 10. Evidencia de pago del BOLETO.
-- Dos slots de imagen opcionales, disponibles en cualquier boleto sin
-- importar si el CONGRESO es gratuito. Mismo limite que el resto: 2 MB.
ALTER TABLE boleto
  ADD COLUMN IF NOT EXISTS media_pago1mime_type varchar(100),
  ADD COLUMN IF NOT EXISTS media_pago1img_data bytea,
  ADD COLUMN IF NOT EXISTS media_pago2mime_type varchar(100),
  ADD COLUMN IF NOT EXISTS media_pago2img_data bytea;

-- 11. Evidencia de pago presentada por el ALUMNO.
-- Dos slots opcionales que el ALUMNO sube por su cuenta. La administracion
-- decide si los acepta copiandolos a media_pago1/2.
ALTER TABLE boleto
  ADD COLUMN IF NOT EXISTS media_alumno1mime_type varchar(100),
  ADD COLUMN IF NOT EXISTS media_alumno1img_data bytea,
  ADD COLUMN IF NOT EXISTS media_alumno2mime_type varchar(100),
  ADD COLUMN IF NOT EXISTS media_alumno2img_data bytea;

COMMIT;

-- Verificacion posterior:
--   \d acreditacion
--   SELECT column_name, is_nullable, column_default
--   FROM information_schema.columns
--   WHERE table_name = 'congreso'
--     AND (column_name LIKE 'constancia%' OR column_name = 'codigo_carrera');
--   SELECT column_name, data_type FROM information_schema.columns
--   WHERE table_name = 'acreditacion' AND column_name LIKE 'firma%';
--   SELECT column_name, data_type FROM information_schema.columns
--   WHERE table_name = 'boleto' AND column_name LIKE 'media_pago%';
