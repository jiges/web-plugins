package com.ccr.websocket.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * WebSocket握手完成
 * 握手一旦完成需要根据URI加载不同的Handler以实现不同的功能
 * @author ccr12312@163.com at 2019-1-25
 */
public class HandshakeCompleteHandler extends SimpleChannelInboundHandler<WebSocketServerProtocolHandler.HandshakeComplete> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketServerProtocolHandler.HandshakeComplete msg) throws Exception {

    }
}
