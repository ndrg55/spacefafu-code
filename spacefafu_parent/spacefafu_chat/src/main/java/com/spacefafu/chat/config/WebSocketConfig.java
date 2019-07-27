package com.spacefafu.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    //这里配置了以“/app”开头的websocket请求url。和名为“my-websocket”的endpoint(端点)
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 订阅Broker名称//这里设置的simple broker是指可以订阅的地址，也就是服务器可以发送的地址
        config.enableSimpleBroker("/topic");
        // 全局使用的消息前缀（客户端订阅路径上会体现出来）
        config.setApplicationDestinationPrefixes("/app");
        //点对点使用的订阅前缀（客户端订阅路径上会体现出来），不设置的话，默认也是/user/
        //config.setUserDestinationPrefix("/user");

    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/my-websocket").setAllowedOrigins("*").withSockJS();
    }
}