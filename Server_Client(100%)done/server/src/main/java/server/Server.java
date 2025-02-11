package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    public static List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) throws Exception{
        //ожидает подключение по порту 8081 (создаём сервер)
        ServerSocket serverSocket = new ServerSocket(5000);


        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Neuer Client.Client verbunden");

            // Create a new client handler and add it to the list
            ClientHandler clientHandler = new ClientHandler(clientSocket, clients);
            clients.add(clientHandler);
            clientHandler.start();

        }



    }
}
