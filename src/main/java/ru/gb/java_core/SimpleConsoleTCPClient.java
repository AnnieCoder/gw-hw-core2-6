package ru.gb.java_core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class SimpleConsoleTCPClient {

    private static final int PORT = 8189;
    private static final String HOST = "127.0.0.1";

    public static void main(String[] args) {

        try(Socket socket = new Socket(HOST, PORT);
            Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to server");

            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            while (true) {
                String outcome = scanner.nextLine();
                out.writeUTF(outcome);
                Thread.sleep(500);
                String income = in.readUTF();
                System.out.println("Got the answer: " + income);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
