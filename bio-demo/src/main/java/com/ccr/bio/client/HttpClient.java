package com.ccr.bio.client;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class HttpClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("192.168.2.127",8080));

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.write("POST  HTTP/1.1");
        writer.newLine();
        writer.write("Host: www.baidu.com");
        writer.newLine();
        writer.write("Cache-Control: no-cache");
        writer.newLine();
        writer.write("Postman-Token: a69fb080-837f-fbc9-3af6-7cd1e059f0f6");
        writer.newLine();
        writer.write("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
        writer.newLine();
        writer.newLine();

        writer.flush();

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String line;
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                System.out.println("closing socket");
                socket.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }


    }
}
