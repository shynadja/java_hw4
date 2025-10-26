package com.example.hello_spring.service;

import com.example.hello_spring.model.Message;
import com.example.hello_spring.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    private final MessageRepository repository;
    private static final int MAX_RECENT_MESSAGES = 30;
    
    public MessageService(MessageRepository repository) {
        this.repository = repository;
        initializeSampleData();
    }
    
    private void initializeSampleData() {
        repository.save(new Message("System", "Добро пожаловать в игру Крестики-Нолики!"));
        repository.save(new Message("System", "Создайте новую игру или присоединитесь к существующей"));
    }
       
    public Iterable<Message> getAllMessages() {
        return repository.findAll();
    }

    public List<Message> getRecentMessages() {
        return repository.findRecentMessages(MAX_RECENT_MESSAGES);
    }

    public Message createMessage(String author, String content) {
        if (author == null || author.trim().isEmpty()) {
            author = "Аноним";
        }
        
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Сообщение не может быть пустым");
        }
        
        Message message = new Message(author.trim(), content.trim());
        return repository.save(message);
    }

    public void deleteMessage(Long id) {
        repository.deleteById(id);
    }

    public boolean messageExists(Long id) {
        return repository.existsById(id);
    }
}
