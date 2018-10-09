package com.ccr.bio.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Http服务
 * @author ccr12312@163.com at 20181009
 */
public class HttpServer {

    Logger logger = LoggerFactory.getLogger(HttpServer.class);

    class ServerSocketFactoryImpl implements ServerSocketFactory{

        private int port;

        public ServerSocketFactoryImpl(int port) {
            this.port = port;
        }

        @Override
        public ServerSocket createSocket() throws IOException {
            return new ServerSocket(port);
        }

        @Override
        public Socket acceptSocket(ServerSocket serverSocket) throws IOException {
            return serverSocket.accept();
        }
    }

    class Acceptor implements Runnable {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                Socket socket = null;

                try {
                    socket = serverSocketFactory.acceptSocket(serverSocket);
                } catch (IOException e) {
                }

                workerExecutor.execute(SocketProcessorFactory.createSocketProcessor(socket));
            }
        }
    }

    Executor workerExecutor;

    Executor bossExecutor;

    ServerSocketFactory serverSocketFactory;

    protected ServerSocket serverSocket = null;
    private void init() {
        //1、初始化两个线程池
        workerExecutor = Executors.newFixedThreadPool(8);
        bossExecutor = Executors.newSingleThreadExecutor();

        //2、创建ServerSocketFactory并配置相关属性
        serverSocketFactory = new ServerSocketFactoryImpl(8080);
    }

    public void start(){
        try {
            this.serverSocket = serverSocketFactory.createSocket();
        } catch (IOException e) {
            logger.error("start server failed.",e);
            System.exit(1);
        }
        bossExecutor.execute(new Acceptor());
    }

    public static void main(String args[]) {
        HttpServer httpServer = new HttpServer();
        httpServer.init();
        httpServer.start();
    }
}
