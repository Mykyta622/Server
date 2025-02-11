# Определяем имя для нового клиента
client_name="client"
count=1

while docker ps --format '{{.Names}}' | grep -q "^$client_name$"; do
    client_name="client$count"
    ((count++))
done

# Запускаем клиента с уникальным именем
echo "Запускаем клиента с именем $client_name..."
docker run --rm -it --network my_network --name "$client_name" client

