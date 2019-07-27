package com.spacefafu.chat.listener;

import com.spacefafu.chat.ChatApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.util.HashMap;
import java.util.Map;

@Component
public class STOMPConnectEventListener implements ApplicationListener<SessionConnectEvent> {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    //当前用户列表

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        String userId = sha.getNativeHeader("login").get(0);
        String sessionId = sha.getSessionId();
        ChatApplication.sessionMap.put(userId,sessionId);
        System.out.println("STOMPConnectEventListener........"+userId+"-"+sessionId);

        System.out.println(ChatApplication.sessionMap.toString());
    }
}