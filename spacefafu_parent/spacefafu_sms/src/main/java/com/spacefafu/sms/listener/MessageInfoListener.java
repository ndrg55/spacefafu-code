package com.spacefafu.sms.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spacefafu.sms.client.UserClient;
import com.spacefafu.sms.pojo.Messageinfo;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
@RabbitListener(queues = "messageinfo")
public class MessageInfoListener {
    @Autowired
    UserClient userClient;
    @RabbitHandler
    public void messageinfo(Map<String,String> message){
        ObjectMapper mapper = new ObjectMapper();
        try {
            String s = mapper.writeValueAsString(message);
            Messageinfo messageinfo = new Messageinfo();
            messageinfo.setContentid(message.get("contentid"));
            messageinfo.setFromuserid(message.get("fromuserid"));
            messageinfo.setId(message.get("id"));
            messageinfo.setMessage(message.get("message"));
            messageinfo.setTime(new Date());
            messageinfo.setTitle(message.get("title"));
            messageinfo.setType(message.get("type"));
            messageinfo.setUserid(message.get("userid"));
            userClient.add(messageinfo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
}
