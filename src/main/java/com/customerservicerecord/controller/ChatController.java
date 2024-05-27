package com.customerservicerecord.controller;

import com.customerservicerecord.model.ChatMessage;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/chat/{userName}")
public class ChatController {

    private static Map<String, Session> sessionsMap = new ConcurrentHashMap<>();
    private static int visitorCounter = 0;
    private static Gson gson = new Gson();

    @OnOpen
    public void onOpen(@PathParam("userName") String userName, Session userSession) throws IOException {
        if (userName.equals("visitor")) {
            userName = "visitor" + (++visitorCounter);
        }
        sessionsMap.put(userName, userSession);
        broadcastUserStatus(userName, "online");
    }

    @OnMessage
    public void onMessage(Session userSession, String message) {
        ChatMessage chatMessage = gson.fromJson(message, ChatMessage.class);
        chatMessage.setTimestamp(LocalDateTime.now()); // 设置时间戳

        // 发送消息给特定的接收者或广播给所有人
        sendMessageToUser(chatMessage.getReceiver(), chatMessage);
    }

    @OnError
    public void onError(Session userSession, Throwable e) {
        e.printStackTrace();
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        String userNameClose = null;
        for (Map.Entry<String, Session> entry : sessionsMap.entrySet()) {
            if (entry.getValue().equals(userSession)) {
                userNameClose = entry.getKey();
                sessionsMap.remove(userNameClose);
                break;
            }
        }
        if (userNameClose != null) {
            broadcastUserStatus(userNameClose, "offline");
        }
    }

    private void sendMessageToUser(String receiver, ChatMessage message) {
        if (receiver.equals("host")) {
            sessionsMap.values().forEach(session -> {
                if (session.isOpen() && getUserNameBySession(session).equals("host")) {
                    session.getAsyncRemote().sendText(gson.toJson(message));
                }
            });
        } else {
            Session receiverSession = sessionsMap.get(receiver);
            if (receiverSession != null && receiverSession.isOpen()) {
                receiverSession.getAsyncRemote().sendText(gson.toJson(message));
            }
        }
    }

    private String getUserNameBySession(Session session) {
        for (Map.Entry<String, Session> entry : sessionsMap.entrySet()) {
            if (entry.getValue().equals(session)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void broadcastUserStatus(String userName, String status) {
        ChatMessage message = new ChatMessage();
        message.setType("status");
        message.setSender(userName);
        message.setMessage(status);
        message.setTimestamp(LocalDateTime.now());
        sessionsMap.values().forEach(session -> {
            if (session.isOpen()) {
                session.getAsyncRemote().sendText(gson.toJson(message));
            }
        });
    }
}