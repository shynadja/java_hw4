package com.example.hello_spring.repository;

import com.example.hello_spring.model.Game;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class GameRepository {
    private final ConcurrentHashMap<Long, Game> storage = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);
    
    public Game save(Game game) {
        if (game.getId() == null) {
            game.setId(idCounter.incrementAndGet());
        }
        storage.put(game.getId(), game);
        return game;
    }
    
    public Game findById(Long id) {
        return storage.get(id);
    }
    
    public Iterable<Game> findAll() {
        return storage.values();
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
