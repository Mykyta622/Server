package client;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientSocket {
    public static Scanner scanner = new Scanner(System.in);
    public static Socket socket;//для связи с сервером
    static {
        try {
            socket = new Socket("my_server", 5000);
            //if you start from idea ,than localhost!
        } catch (IOException e) {
            System.out.println("You are not connected with server\nMaybe \n1)Ip or Port is wrong or \n2)server doesn't run");
            System.exit(0);
        }
    }

    //для отправления сообщений
    public static PrintWriter out;
    static {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // для принятия сообщений
    public static BufferedReader in;
    static {
        try{
            in =new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //bekomt man vom server
    //слушаем сервер и записываем в эту переменную
    public static String response;

    public static void main(String[] args) throws Exception {
        //подключение к серверу со своего компьютера

        //для записи того что введено в консоль
        Scanner send = new Scanner(System.in);

        //получаемые сообщения
        Thread readThread = new Thread(() -> {
            try {
                while ((response = in.readLine()) != null) {
                    System.out.println(response);
                }
            } catch (Exception e) {
                System.out.println("Your are disconnected");
            }
        });
        readThread.start();

        //очищаем консоль
        System.out.print("\033[H\033[J");

        //записываем сообщение
        //переписать
        System.out.println("Do you wanna Log in(l) or Register(r)");
        boolean loopIsLaufen = true;
        while (loopIsLaufen) {
            String logOrRegister1 = scanner.nextLine();
            //отправляет 'l', 'r' на сервер и так уже запускает методы
            switch (logOrRegister1) {
                case "l":
                    //метод входа
                    out.println(logOrRegister1);
                    out.flush();
                    logIn1();
                    loopIsLaufen = false;
                    break;
                case "r":
                    //метод регистрации
                    out.println(logOrRegister1);
                    out.flush();
                    register1();
                    loopIsLaufen = false;
                    break;
                default:
                    System.out.println("Please enter (l) or (r)");
            }
        }

        //получатель
        //данный код не трогаю
        System.out.println("\nWho you'd like to chat with?\n Enter a name: ");
        String emfenger = send.nextLine();
        out.println(emfenger);//правильно, отправляем на сервер имя собеседника

        while (true) {
            System.out.println("Please enter your message");
            String words = send.nextLine();   //записываем в переменную то что ввел пользователь
            out.println(words);   //записываем переменную в поток//
            out.flush();
            if (words.equals("bye")){
                break;
            }
        }
        out.close();
        in.close();
        socket.close();
        System.exit(0);
    }

    public static void logIn1() throws InterruptedException {
        System.out.println("Your name:");
        String name2;
        while(true){
            name2 = scanner.nextLine();
            out.println(name2);
            out.flush();
            Thread.sleep(100); // небольшая задержка
            String nameMach1 = response;
            if("Your Password:".equals(nameMach1)){
                break;
            }
        }

        String password;
        while(true){
            password = scanner.nextLine();
            out.println(password);
            out.flush();
            Thread.sleep(100); // небольшая задержка
            String passwordMach = response;
            if("You have successfully logged in!".equals(passwordMach)){
                break;
            }
        }

        System.out.println("Hi " + name2);
    }

    public static void register1() throws InterruptedException {
        System.out.println("Create your name:");
        String name2;
        while(true){
            name2 = scanner.nextLine();
            out.println(name2);
            out.flush();
            Thread.sleep(100);
            //add to the
            String isoOcup = response;
            if("Create a password:".equals(isoOcup)){
                break;
            }
        }

        String password;
        while (true){
            password = scanner.nextLine();
            out.println(password);
            out.flush();
            Thread.sleep(100);
            break;
        }
        System.out.println(name2 + " successfully registered!");
    }
}
