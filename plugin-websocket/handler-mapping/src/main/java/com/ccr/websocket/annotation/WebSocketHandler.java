package com.ccr.websocket.annotation;

import java.lang.annotation.*;

/**
 * handler注解，NettyServer将根据URI分配Handler
 * Created by ccr at 2018/2/1.
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebSocketHandler {
    String uri() ;
}
