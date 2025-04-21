package com.apptechnosoft.webSocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;


@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	@Autowired
	private NotificationHandler notificationHandler;
	@Autowired
	private ContactUsHandler contactUsHandler;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
	    registry.addHandler(notificationHandler, "/ws/notifications").setAllowedOrigins("*");
        registry.addHandler(contactUsHandler, "/ws/contact-us").setAllowedOrigins("*");

	}

}
