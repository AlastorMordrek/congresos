#!/bin/bash

# Cargar variables de ambiente.
if [ -f .env ]; then
    export $(cat .env | grep -v '#' | xargs)
fi

case "$1" in
    stop)
        echo "Deteniendo todos los servicios (app + db)..."
        docker-compose stop
        ;;
    stop-app)
        echo "Deteniendo solo la aplicacion (la base de datos sigue activa)..."
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

    stop-destroy)
        echo "Deteniendo todos los servicios y eliminando contenedores, redes y volumenes..."
        docker-compose down -v
        ;;
    destroy-rebuild)
        echo "Eliminando volumenes, recompilando y reiniciando la aplicacion..."
        docker-compose down -v
        docker-compose up -d --build app
        ;;

    status)
        echo "Estado de los contenedores:"
        docker-compose ps
        ;;

    logs)
        echo "Mostrando logs..."
        docker logs -t ${APP_NAME}_app
        ;;
    live-logs)
        echo "Mostrando logs en vivo..."
        docker logs -tf ${APP_NAME}_app
        ;;

    *)
        echo "Uso: $0 {stop|stop-app|restart|rebuild|stop-destroy|destroy-rebuild|status|logs|live-logs}"
        exit 1
        ;;
esac
