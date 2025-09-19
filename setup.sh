#!/bin/bash
echo "Cargando variables de configuracion..."
if [ -f .env ]; then
    export $(cat .env | grep -v '#' | xargs)
fi

echo "Compilando e iniciando contenedores..."
docker-compose up --build -d

echo "Esperando a que inicien los servicios..."
sleep 20

echo "Comprobando conexion a la base de datos..."
#docker exec ${APP_NAME}_db pg_isready -U ${DB_USR} -d ${DB_NAME}
docker-compose exec postgres pg_isready -U ${DB_USR} -d ${DB_NAME}

echo "Comprobando estatus de la aplicacion..."
docker-compose ps

echo "Listo!, la aplicacion esta corriendo en http://localhost:${WEB_SERVER_PORT}"
