package com.ccr.bio.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class HttpServer {

    public static void main(String[] args) throws UnknownHostException {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);

            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> {
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        StringBuilder header = new StringBuilder();
                        StringBuilder body = new StringBuilder();
                        int contentLength = 0;
                        String line;
                        while (!(line = reader.readLine()).equals("")) {
                            header.append(line + "\r\n");
                            if(line.startsWith("Content-Length:")){
                                contentLength = Integer.valueOf(line.substring(16));
                            }
                        }
                        for (int i = 0; i < contentLength; ) {
                            String bodyLine = reader.readLine();
                            body.append(bodyLine);
                            i += bodyLine.length();
                        }
                        System.out.println(header);
                        System.out.println(body);
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        writer.write("HTTP/1.1 200 OK");
                        writer.newLine();
                        writer.write("Content-Length: 2");
                        writer.newLine();
                        writer.write("Content-Type: text/plain");
                        writer.newLine();
                        writer.newLine();
                        writer.write("ok");
                        writer.newLine();
                        writer.newLine();
                        writer.flush();
                        while (true) {
                            System.out.println(reader.readLine());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        try {
                            System.out.println("close socket...");
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
