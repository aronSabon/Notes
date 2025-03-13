package com.apptechnosoft.controller.backend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
public class AutoNotificationController {

    // Thread-safe list to hold all active emitters
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @GetMapping(value = "/notifications/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public SseEmitter streamNotifications() {
        SseEmitter emitter = new SseEmitter();
        emitters.add(emitter);

        try {
            // Send a comment to keep the connection alive
            emitter.send(SseEmitter.event().comment("Connected"));
            // Alternatively, send a data event
            // emitter.send("Connection established");
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError((e) -> emitters.remove(emitter));

        return emitter;
    }

    // Send the notification to all connected clients
    public void sendNotification(String message) {
        System.out.println("Sending notification: " + message);
        List<SseEmitter> deadEmitters = new ArrayList<>();
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(message);
            } catch (Exception e) {
                // If sending fails, mark the emitter as dead
                deadEmitters.add(emitter);
            }
        }
        // Clean up dead emitters
        emitters.removeAll(deadEmitters);
    }
}