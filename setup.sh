#!/bin/bash
echo "Cargando variables de configuracion..."
if [ -f .env ]; then
    export $(cat .env | grep -v '#' | xargs)
fi

# Comprobar si debemos correr en modo desarrollo o produccion.
if [ "$1" = "--dev" ]; then
    echo "Modo desarrollo: montando volumenes para desarrollo rapido..."
    docker-compose -f docker-compose.yml -f docker-compose.override.yml up --build -d
else
    echo "Compilando e iniciando contenedores (modo produccion)..."
    docker-compose up --build -d
fi

echo "Esperando a que inicien los servicios..."
sleep 20

echo "Comprobando conexion a la base de datos..."
docker-compose exec postgres pg_isready -U ${DB_USR} -d ${DB_NAME}

echo "Comprobando estatus de la aplicacion..."
docker-compose ps

echo "Listo!, la aplicacion esta corriendo en http://localhost:${WEB_SERVER_PORT}"
