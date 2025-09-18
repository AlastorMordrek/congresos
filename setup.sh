#!/bin/bash
echo "Loading environment variables..."
if [ -f .env ]; then
    export $(cat .env | grep -v '#' | xargs)
fi

echo "Building and starting containers..."
docker-compose up --build -d

echo "Waiting for services to start..."
sleep 15

echo "Checking database connection..."
docker exec ${APP_NAME}_db pg_isready -U ${DB_USR} -d ${DB_NAME}

echo "Setup complete! Application is running at http://localhost:${WEB_SERVER_PORT}"
