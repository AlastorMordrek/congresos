#!/bin/bash

# Cargar variables de ambiente.
if [ -f .env ]; then
    export $(cat .env | grep -v '#' | xargs)
fi

case "$1" in
    stop)
        echo "Deteniendo solo la aplicacion..."
        docker-compose stop app
        ;;
    restart)
        echo "Reiniciando la aplicacion (sin recompilar)..."
        docker-compose restart app
        ;;
    rebuild)
        echo "Recompilando y reiniciando la aplicacion (sin recompilar las dependencias)..."
        docker-compose stop app
        docker-compose up -d --build app
        ;;
    status)
        echo "Estado de los contenedores:"
        docker-compose ps
        ;;
    *)
        echo "Uso: $0 {stop|restart|rebuild|status}"
        exit 1
        ;;
esac
