#!/bin/bash

# Проверяем, запущен ли сервер
if docker ps --format '{{.Names}}' | grep -q "^my_server$"; then
    echo "Останавливаем сервер..."
    docker stop my_server
else
    echo "Сервер уже остановлен или не Запущен"
fi
