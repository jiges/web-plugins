package com.ccr.bio.server;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

    static class SocketWriter extends Thread {

        Socket socket;

        BufferedWriter writer;

        BufferedReader keyBoardReader;

        public SocketWriter(Socket socket) {
            this.socket = socket;
            try {
                this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.keyBoardReader = new BufferedReader(new InputStreamReader(System.in));
        }

        @Override
        public void run() {
            while (!interrupted()) {
                try {
                    System.out.println("please input some words to be sent with the 'enter' end...");
                    String words = keyBoardReader.readLine();
                    if(words != null && words.equals("stop")) {
                        socket.close();
                        break;
                    }
                    System.out.println("'" + words + "' will be sent to client.");
                    writer.write(words);
                    writer.newLine();
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("shutdown writing");
            System.out.println(socket.isClosed());
        }
    }

    static class SocketReader extends Thread {
        Socket socket;

        BufferedReader reader;

        public SocketReader(Socket socket) {
            this.socket = socket;
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (!interrupted()) {
                try {
                    System.out.println("waiting message....");
                    String line = reader.readLine();
                    if(line == null) {
                        break;
                    }
                    System.out.println("receive client message: " + line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("read end...");

            System.out.println(socket.isClosed());
        }
    }

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8080);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        try {
            System.out.println("listening client to connect....");
            Socket socket = serverSocket.accept();
            Thread writerThread = new SocketWriter(socket);
            Thread readerThread = new SocketReader(socket);
            writerThread.start();
            readerThread.start();

            readerThread.join();
            writerThread.join();

            Thread.sleep(5000);
            System.out.println("isClosed:" + socket.isClosed());
            System.out.println("isConnected:" + socket.isConnected());
            System.out.println("isInputShutdown:" + socket.isInputShutdown());
            System.out.println("isOutputShutdown:" + socket.isOutputShutdown());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
