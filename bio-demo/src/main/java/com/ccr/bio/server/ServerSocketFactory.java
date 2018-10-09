package com.ccr.bio.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author ccr12312@163.com at 2018-10-9
 */
public interface ServerSocketFactory {
    ServerSocket createSocket() throws IOException;
    Socket acceptSocket(ServerSocket serverSocket) throws IOException;
}
