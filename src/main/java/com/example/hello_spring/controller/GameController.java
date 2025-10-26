package com.example.hello_spring.controller;

import com.example.hello_spring.model.Game;
import com.example.hello_spring.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/games")
public class GameController {
    private final GameService service;
    
    public GameController(GameService service) {
        this.service = service;
    }

    @GetMapping
    public Iterable<Game> getAllGames() {
        return service.getAllGames();
    }

    @GetMapping("/recent")
    public Iterable<Game> getRecentGames(@RequestParam(defaultValue = "10") int limit) {
        return service.getRecentGames(limit);
    }

    @PostMapping
    public ResponseEntity<?> createGame(
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "Player 1") String player1,
            @RequestParam(defaultValue = "Player 2") String player2) {
        
        if (size < 3) {
            return ResponseEntity.badRequest().body("Размер поля должен быть не менее 3");
        }

        Game game = service.createGame(size, player1, player2);
        return ResponseEntity.ok(game);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGame(@PathVariable Long id) {
        Game game = service.getGameById(id);
        if (game != null) {
            return ResponseEntity.ok(game);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/move")
    public ResponseEntity<?> makeMove(
            @PathVariable Long id,
            @RequestParam int row,
            @RequestParam int col) {
        
        // Конвертируем в zero-based индексы
        int zeroBasedRow = row - 1;
        int zeroBasedCol = col - 1;

        Game game = service.makeMove(id, zeroBasedRow, zeroBasedCol);
        if (game != null) {
            return ResponseEntity.ok(game);
        }

        Map<String, String> response = new HashMap<>();
        response.put("error", "Недопустимый ход или игра не найдена");
        return ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGame(@PathVariable Long id) {
        if (service.getGameById(id) != null) {
            service.deleteGame(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}