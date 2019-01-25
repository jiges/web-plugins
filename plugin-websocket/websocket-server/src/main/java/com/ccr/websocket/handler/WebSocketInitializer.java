package com.ccr.websocket.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author ccr12312@163.com at 2019-1-25
 */
public class WebSocketInitializer extends ChannelInitializer {
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addFirst(new LoggingHandler());
        ch.pipeline().addLast(new HttpServerCodec());
        //maxContentLength 服务期望接收到的最大请求体，客户端在发送Post请求时，
        // 先询问服务端是否愿意接受长度为M的请求体（通过100-continue请求头），
        //服务端愿意接受，客户端再发送请求体，否则客户端将接收到417的响应
        ch.pipeline().addLast(new HttpObjectAggregator(2 * 1024 * 1024));
        //协议升级，Http协议升级为WebSocket协议
        ch.pipeline().addLast(new WebSocketServerProtocolHandler("/ws"));
    }
}
