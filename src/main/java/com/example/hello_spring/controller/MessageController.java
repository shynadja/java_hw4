package com.example.hello_spring.controller;

import com.example.hello_spring.model.Message;
import com.example.hello_spring.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class MessageController {
    private final MessageService service;
    
    public MessageController(MessageService service) {
        this.service = service;
    }

    @GetMapping("/messages")
    public Iterable<Message> getAllMessages() {
        return service.getAllMessages();
    }

    @GetMapping("/messages/recent")
    public List<Message> getRecentMessages() {
        return service.getRecentMessages();
    }

    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(
            @RequestParam String author,
            @RequestParam String content) {
        
        if (content == null || content.trim().isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Сообщение не может быть пустым");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            Message message = service.createMessage(author, content);
            return ResponseEntity.ok(message);
        } catch (IllegalArgumentException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/messages/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long id) {
        if (service.messageExists(id)) {
            service.deleteMessage(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/broadcast")
    public ResponseEntity<Message> broadcastSystemMessage(@RequestParam String content) {
        Message message = service.createMessage("System", content);
        return ResponseEntity.ok(message);
    }
}
