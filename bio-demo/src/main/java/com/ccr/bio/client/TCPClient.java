package com.ccr.bio.client;


import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TCPClient {

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
                    System.out.println("'" + words + "' will be sent to server.");
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
                    System.out.println("receive server response: " + line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("read end....");
            System.out.println(socket.isClosed());
        }
    }

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("192.168.1.106",8080));


        Thread writerThread = new SocketWriter(socket);
        Thread readerThread = new SocketReader(socket);
        writerThread.start();
        readerThread.start();
        System.out.println("connected to server.");
        try {
            writerThread.join();
            readerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
