package com.ccr.websocket.handler;

import com.ccr.websocket.annotation.WebSocketHandler;
import io.netty.channel.ChannelHandler;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author ccr12312@163.com at 2019-1-28
 */
public class HandlerMappings {

    private static ServiceLoader<ChannelHandler> loader;

    static {
        load();
    }

    /**
     * SPI机制加载HandlerMapping
     */
    public static void load() {
        loader = ServiceLoader.load(ChannelHandler.class);
    }

    /**
     * 这个方法是在检查更新程序中使用
     */
    public static synchronized void reload(){
        loader.reload();
    }

    public static synchronized ChannelHandler lookup(String uri) {
        Iterator<ChannelHandler> iter = loader.iterator();
        try {
            while(iter.hasNext()) {
                ChannelHandler handler = iter.next();
                if(handler.getClass().isAnnotationPresent(WebSocketHandler.class)) {
                    WebSocketHandler handlerMapping = handler.getClass().getAnnotation(WebSocketHandler.class);
                    if (handlerMapping.uri().equals(uri)) {
                        return handler;
                    }
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

}
