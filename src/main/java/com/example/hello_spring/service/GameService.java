package com.example.hello_spring.service;

import com.example.hello_spring.model.*;
import com.example.hello_spring.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class GameService {
    private final GameRepository repository;
    
    public GameService(GameRepository repository) {
        this.repository = repository;
        initializeSampleData();
    }
    
    private void initializeSampleData() {
        // Создаем демо-игру
        Board board = new Board(3);
        Player player1 = new Player("Player 1", 'X');
        Player player2 = new Player("Player 2", 'O');
        
        Game game = new Game(board, player1, player2);
        repository.save(game);
    }
       
    public Iterable<Game> getAllGames() {
        return repository.findAll();
    }

    public Game createGame(int boardSize, String player1Name, String player2Name) {
        if (boardSize < 3) {
            throw new IllegalArgumentException("Размер поля должен быть не менее 3");
        }
        
        Board board = new Board(boardSize);
        Player player1 = new Player(player1Name, 'X');
        Player player2 = new Player(player2Name, 'O');
        
        Game game = new Game(board, player1, player2);
        return repository.save(game);
    }

    public Game getGameById(Long id) {
        return repository.findById(id);
    }

    public Game makeMove(Long gameId, int row, int col) {
        Game game = repository.findById(gameId);
        if (game == null || game.getStatus() != GameStatus.IN_PROGRESS) {
            return null;
        }

        Board board = game.getBoard();
        Player currentPlayer = game.getCurrentPlayer();

        if (board.isValidMove(row, col)) {
            board.placeMark(row, col, currentPlayer.getMark());
            
            // Проверка условий окончания игры
            if (board.checkWin()) {
                game.setStatus(currentPlayer == game.getPlayer1() ? 
                    GameStatus.PLAYER1_WON : GameStatus.PLAYER2_WON);
            } else if (board.isFull()) {
                game.setStatus(GameStatus.DRAW);
            } else {
                game.switchPlayer();
            }
            
            return repository.save(game);
        }
        
        return null; // Недопустимый ход
    }

    public List<Game> getRecentGames(int limit) {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .sorted((g1, g2) -> g2.getUpdatedAt().compareTo(g1.getUpdatedAt()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public void deleteGame(Long id) {
        repository.deleteById(id);
    }

    // Новый метод для проверки существования игры
    public boolean gameExists(Long id) {
        return repository.existsById(id);
    }
}