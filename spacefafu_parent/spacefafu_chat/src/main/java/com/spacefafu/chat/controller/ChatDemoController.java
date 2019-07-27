package com.spacefafu.chat.controller;

import com.spacefafu.chat.ChatApplication;
import com.spacefafu.chat.dao.ChatlistDao;
import com.spacefafu.chat.pojo.Chatinfo;
import com.spacefafu.chat.pojo.Chatlist;
import com.spacefafu.chat.service.ChatinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import util.IdWorker;

import java.util.Date;
import java.util.List;

@Controller
public class ChatDemoController {

    @Autowired
    ChatinfoService chatinfoService;
    @Autowired
    ChatlistDao chatlistDao;
//    @MessageMapping("/send")
//    @SendTo("/topic/send")//会把方法的返回值广播到指定主题（“主题”这个词并不合适）
    //  @SendToUser("/message")//把返回值发到指定队列（“队列”实际不是队列，而是跟上面“主题”类似的东西，只是spring在SendTo的基础上加了用户的内容而已）
//    WebSocketConfig 中配置setApplicationDestinationPrefixes()的消息会被转发到WebSocketController 中 @MessageMapping 相应方法进行处理。
// @SendTo("/topic/hello") 会把方法的返回值序列化为json串，然后发送到指定的主题，不用此注解，使用 simpMessagingTemplate.convertAndSend 效果相同；
// 若为 @SendToUser("/message") 则为发送到指定的用户队列（实际队列名字为/user/用户名/原队列名），不用此注解，
// 使用 simpMessagingTemplate.convertAndSendToUser() 效果相同；
//
//  后台主动往前端推送消息，直接调用 simpMessagingTemplate.convertAndSendToUser() 跟 simpMessagingTemplate.convertAndSend() 即可将消息发往队列或者主题。
//    public SocketMessage send(SocketMessage message) throws Exception {
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        message.setDate(df.format(new Date()));
//        return message;
//    }


    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private IdWorker idWorker;
    @MessageMapping("/send")
    public void send(Chatinfo sendmessageinfo) throws Exception {

        sendmessageinfo.setId(idWorker.nextId()+"");
        sendmessageinfo.setCreatetime(new Date());
        String tousersession = ChatApplication.sessionMap.get(sendmessageinfo.getTouserid());
        System.out.println(sendmessageinfo.toString());
        //若对方在线
        if(tousersession!=null){
            String sessionId=tousersession;
            System.out.println("sessionId:"+sessionId);
            messagingTemplate.convertAndSendToUser(sessionId,
                    "/topic/send",sendmessageinfo,createHeaders(sessionId));
        }
        //存入数据库
        //chatinfo
        chatinfoService.add(sendmessageinfo);
        //改变未读消息列表    ----- 对方列表中
        List<Chatlist> chatlists = chatlistDao.getunreadup(sendmessageinfo.getTouserid(), sendmessageinfo.getUserid());
        if(chatlists.size()>0){
            Chatlist chatlist = chatlists.get(0);
            chatlist.setLasttime(sendmessageinfo.getCreatetime());
            chatlist.setMessage(sendmessageinfo.getMessage());
            chatlist.setUnread(chatlist.getUnread()+1);
            chatlistDao.save(chatlist);
        }else{
            Chatlist chatlist = new Chatlist();
            chatlist.setUserid(sendmessageinfo.getTouserid());
            chatlist.setFromuserid(sendmessageinfo.getUserid());
            chatlist.setId(idWorker.nextId()+"");
            chatlist.setLasttime(sendmessageinfo.getCreatetime());
            chatlist.setMessage(sendmessageinfo.getMessage());
            chatlist.setUnread(1L);
            chatlistDao.save(chatlist);
        }

        //改变未读消息列表    ----- 我的列表
        List<Chatlist> chatlistsmy = chatlistDao.getunreadup( sendmessageinfo.getUserid(),sendmessageinfo.getTouserid());
        if(chatlistsmy.size()>0){

            Chatlist chatlist = chatlistsmy.get(0);
            chatlist.setLasttime(sendmessageinfo.getCreatetime());
            chatlist.setMessage(sendmessageinfo.getMessage());
            chatlist.setUnread(0L);
            chatlistDao.save(chatlist);
        }else{
            Chatlist chatlist = new Chatlist();
            chatlist.setUserid(sendmessageinfo.getUserid());
            chatlist.setFromuserid(sendmessageinfo.getTouserid());
            chatlist.setId(idWorker.nextId()+"");
            chatlist.setLasttime(sendmessageinfo.getCreatetime());
            chatlist.setMessage(sendmessageinfo.getMessage());
            chatlist.setUnread(0L);
            chatlistDao.save(chatlist);
        }
    }
//    @MessageMapping("/send")
//    public void send(SocketMessage message) throws Exception {
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        message.setDate(df.format(new Date()));
//        String sessionId= ChatApplication.sessionMap.get(message.getToUser());
//        System.out.println("sessionId:"+sessionId);        messagingTemplate.convertAndSendToUser(sessionId,"/topic/send",message,createHeaders(sessionId));
//    }
    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }
}