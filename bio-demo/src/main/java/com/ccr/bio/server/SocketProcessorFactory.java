package com.ccr.bio.server;

import org.apache.http.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.params.BasicHttpParams;

import java.io.IOException;
import java.net.Socket;

/**
 * @author ccr12312@163.com at 2018-10-9
 */
public class SocketProcessorFactory {

    static class SocketProcessor implements Runnable {

        private Socket socket;

        public SocketProcessor(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                BasicHttpParams params = new BasicHttpParams();
                DefaultHttpServerConnection conn = new DefaultHttpServerConnection();
                HttpRequest request = null;
                conn.bind(socket, params);
                request = conn.receiveRequestHeader();
                if (request instanceof HttpEntityEnclosingRequest) {
                    conn.receiveRequestEntity((HttpEntityEnclosingRequest)
                            request);
                    HttpEntity entity = ((HttpEntityEnclosingRequest) request)
                            .getEntity();
                    if (entity != null) {

                    }
                }
                System.out.println(params.getParameter("aa"));
                System.out.println(request.getRequestLine().getMethod() + " " + request.getRequestLine().getUri() + " " + request.getRequestLine().getProtocolVersion().getProtocol());
                for (int i = 0; i < request.getAllHeaders().length; i++) {
                    System.out.println(request.getAllHeaders()[i].getName() +": " + request.getAllHeaders()[i].getValue());
                }
                HttpResponse response = new
                        BasicHttpResponse(HttpVersion.HTTP_1_1,
                        200, "OK");
                response.setEntity(new StringEntity("Got it"));
                conn.sendResponseHeader(response);
                conn.sendResponseEntity(response);
            } catch (HttpException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Runnable createSocketProcessor(Socket socket){
        return new SocketProcessor(socket);
    }

}
