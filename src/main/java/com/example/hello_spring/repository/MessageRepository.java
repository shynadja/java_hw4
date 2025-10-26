package com.example.hello_spring.repository;

import com.example.hello_spring.model.Message;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MessageRepository {
    private final ConcurrentHashMap<Long, Message> storage = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);
    
    public Message save(Message message) {
        if (message.getId() == null) {
            message.setId(idCounter.incrementAndGet());
        }
        storage.put(message.getId(), message);
        return message;
    }
    
    public Message findById(Long id) {
        return storage.get(id);
    }
    
    public Iterable<Message> findAll() {
        return storage.values();
    }
    
    public List<Message> findRecentMessages(int limit) {
        return storage.values().stream()
                .sorted(Comparator.comparing(Message::getTimestamp).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    public void deleteById(Long id) {
        storage.remove(id);
    }
    
    public void clear() {
        storage.clear();
        idCounter.set(0);
    }

    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }
}
