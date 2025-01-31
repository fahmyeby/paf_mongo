package com.example.paf_ws26.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.paf_ws26.service.GameService;

@RestController
@RequestMapping("/api")
public class GameController {

    @Autowired
    private GameService gameService;

    // get all games
    @GetMapping("/games")
    public ResponseEntity<Map<String, Object>> getAllGames(
            @RequestParam(defaultValue = "25") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        Map<String, Object> response = gameService.getAllGames(limit, offset);
        return ResponseEntity.ok(response);
    }

    // get games sorted by rank ascending
    @GetMapping("/games/rank")
    public ResponseEntity<Map<String, Object>> getGamesByRank(
            @RequestParam(defaultValue = "25") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        Map<String, Object> response = gameService.getGamesByRank(limit, offset);
        return ResponseEntity.ok(response);
    }

    // get game details by ObjectId
    @GetMapping("/game/{id}")
    public ResponseEntity<Map<String, Object>> getGameDetails(@PathVariable String id) {
        Map<String, Object> gameDetails = gameService.getGameDetails(id);
        if (gameDetails != null) {
            return ResponseEntity.ok(gameDetails);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // get game with reviews
    @GetMapping("/game/{id}/reviews")
    public ResponseEntity<Map<String, Object>> getGameWithReviews(@PathVariable String id) {
        Map<String, Object> response = gameService.getGameWithReviews(id);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // get games with highest/lowest ranking
    @GetMapping("/game/highest")
    public ResponseEntity<Map<String, Object>> getGamesWithHighestRating() {
        Map<String, Object> response = gameService.getGamesWithHighestRating();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/game/lowest")
    public ResponseEntity<Map<String, Object>> getGamesWithLowestRating() {
        Map<String, Object> response = gameService.getGamesWithLowestRating();
        return ResponseEntity.ok(response);
    }
}