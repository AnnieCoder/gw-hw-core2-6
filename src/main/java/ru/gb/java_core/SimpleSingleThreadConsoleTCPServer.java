package ru.gb.java_core;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleSingleThreadConsoleTCPServer {

    private static final int PORT = 8189;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Thread serverThread;

    public static void main(String[] args) throws IOException {
        new SimpleSingleThreadConsoleTCPServer().start();
    }

    private void start() throws IOException {
        try(ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("Server started");
            waitConnection(serverSocket);

            startConsoleInput();

            while (true) {
                String income = in.readUTF();
                if (income.startsWith("/end")) {
                    shutDown();
                    break;
                }
                System.out.println("Received: " + income);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            shutDown();
        }
    }

    private void startConsoleInput() {

        serverThread = new Thread(() -> {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
                System.out.println("Enter your message >>>> ");
                while (!Thread.currentThread().isInterrupted() && socket.isClosed()) {
                    if (br.ready()) {
                        String outcome = br.readLine();
                        out.writeUTF(outcome);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
    private void waitConnection(ServerSocket serverSocket) throws IOException {
        System.out.println("Waiting for connection...");
        Socket socket = serverSocket.accept();
        System.out.println("Client connected");

        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    private void shutDown() throws IOException {
        if (serverThread != null && serverThread.isAlive()) {
            serverThread.interrupt();
        }

        if (socket != null && !socket.isClosed()) {
            socket.close();
        }

        System.out.println("Server stopped");
    }
}
