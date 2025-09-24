#!/bin/bash
echo "Cargando variables de configuracion..."
if [ -f .env ]; then
    export $(cat .env | grep -v '#' | xargs)
fi

# Comprobar si debemos correr en modo desarrollo o produccion.
if [ "$1" = "--dev" ]; then
    if [ ! -f "docker-compose.override.yml" ]; then
        if [ -f "docker-compose.override.yml.example" ]; then
            echo "Creando archivo de override personalizado desde la plantilla..."
            cp docker-compose.override.yml.example docker-compose.override.yml
            echo "Por favor, revisa docker-compose.override.yml y ajusta segun tu entorno de desarrollo."
        else
            echo "Error: No se encontro el archivo docker-compose.override.yml o un archivo docker-compose.override.yml.example dese el cual derivarlo."
            exit 1
        fi
    fi
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
