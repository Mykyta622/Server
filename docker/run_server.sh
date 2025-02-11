#!/bin/bash

# Создаём сеть, если её нет
docker network ls | grep -q "my_network" || docker network create my_network

# Проверяем, существует ли контейнер с именем my_server
if docker ps -a --format '{{.Names}}' | grep -q "^my_server$"; then
    # Если контейнер существует, просто запускаем его
    echo "Контейнер my_server уже существует, запускаем его..."
    docker start my_server
else
    # Если контейнер не существует, запускаем новый
    echo "Запускаем новый сервер..."
    docker run --rm -d --network my_network --name my_server server
fi
