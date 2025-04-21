package com.apptechnosoft.webSocket;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;
@Component
@Scope("singleton")

public class ContactUsHandler extends TextWebSocketHandler {

    private static Set<WebSocketSession> sessions = new HashSet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//  
//        broadcastNotification(message.getPayload());
//    }
//
//    public void broadcastNotification(String message) throws IOException {
//    	
//        for (WebSocketSession session : sessions) {
//            if (session.isOpen()) {
//                session.sendMessage(new TextMessage(message));
//            }
//        }
//    }
    
    public void broadcastMessage(String contactUsMessage, String name) throws IOException {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                String jsonMessage = String.format("{\"name\": \"%s\", \"message\": \"%s\"}",
                    name.replace("\"", "\\\""), contactUsMessage.replace("\"", "\\\""));
                session.sendMessage(new TextMessage(jsonMessage));
            }
        }
    }

}
