package server;

import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.util.List;
//in = входящее сообщение
//out = отправляемое сообщение
public class ClientHandler extends Thread {
    private Socket socket;
    private List<ClientHandler> clients;
    private PrintWriter send;
    private String name;
    private String password;
    private String receiverName;
    private JSONObject existList;

    public ClientHandler(Socket socket, List<ClientHandler> clients){
        this.socket = socket;
        this.clients = clients;
    }

    public String getUserName() {
        return name;
    }

    public void run() {
        try {
            //рабочий код


            //создаем объекты для получения входящего потока (in)
            //и для того что бы отправлять (out)
            BufferedReader receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            send = new PrintWriter(socket.getOutputStream(), true);


            //принимаем 'l' or 'r'
            try {
                String logOrReg1 = receive.readLine();
                switch (logOrReg1){
                    case "l":
                        logIn2(receive, send);
                        break;
                    case "r":
                        register(receive, send);
                        System.out.println("regest ok");
                        break;
                    default:
                        System.out.println("it was not 'r' or 'l'");
                }

                receiverName = receive.readLine();
                if(name != null && receiverName != null){
                    System.out.println( "'"+ name + "' chats with '" + receiverName + "'");
                }
            }catch (NullPointerException e){
                System.out.println("Didn't chosen 'l' or 'r'");
            }




            String inputLine;
            while ((inputLine = receive.readLine()) != null) {
                Thread.sleep(100);
                System.out.println("Client '"+ name + "' wright to '" + receiverName + "': " + inputLine);

                broadcastMessage(name + " spreibt: " + inputLine);

                if ("bye".equalsIgnoreCase(inputLine)) {
                    break;
                }
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Connection with client lost: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            clients.remove(this);
            if(name == null){
                System.out.println("Client unknown is disconnected!");
            }else {
                System.out.println("Client " + name + " is disconnected!");
            }
        }
    }
    //become json file
    public void json2() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("users.json");

        if (inputStream == null) {
            throw new FileNotFoundException("users.json not found in data/");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder string = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            string.append(line);
        }
        existList = new JSONObject(string.toString());

    }


    //login
    public void logIn2(BufferedReader receive , PrintWriter send) throws IOException {

        json2();
        // имя
        while (true){
            name = receive.readLine();
            if (name == null) return;
            if(existList.has(name)){
                send.println("Your Password:");
                send.flush();
                break;
            }else{
                send.println("Wrong name, try again:");
                send.flush();
            }
        }

        //пароль
        while (true){
            password = receive.readLine();
            if (password == null) return;
            if(existList.getString(name).equals(password)){
                send.println("You have successfully logged in!");
                send.flush();
                break;
            }else {
                send.println("Wrong password, try again:");
                send.flush();
            }
        }
        System.out.println("'" + name + "' is logged in");

    }

    //registration
    public void register(BufferedReader receive, PrintWriter send) throws IOException {
        json2();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("users.json");

        while (true) {
            name = receive.readLine();
            if (name == null) return;
            if (existList.has(name)) {
                send.println("This user name is occupied\nEnter another name:");
            } else {
                send.println("Create a password:");
                break;
            }
        }

        while (true) {
            password = receive.readLine();
            if (password == null) return;
            break;
        }

        existList.put(name, password);

        // Записываем обновленный JSON обратно в файл
        try (FileWriter writer = new FileWriter(String.valueOf(inputStream))) {
            System.out.println("vorher");
            writer.write(existList.toString(4));
            System.out.println("danach");
        }
    }



    private void broadcastMessage(String message) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client.getUserName() != null && client.getUserName().equals(receiverName)) {
                    client.send.println(message);
                }else if(receiverName.equals("all")){
                    client.send.println(message);
                }
            }
        }
    }

}
//сделать build протестировать у себя на комптютере и звгрузить на виртуальную нашину