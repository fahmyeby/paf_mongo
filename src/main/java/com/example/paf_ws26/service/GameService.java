package com.example.paf_ws26.service;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.example.paf_ws26.repository.GameRepository;
import com.example.paf_ws26.repository.ReviewRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    // get all games with limit and offset
    public Map<String, Object> getAllGames(Integer limit, Integer offset) {
        List<Document> games = gameRepository.findAllGames(limit, offset);
        List<Map<String, Object>> gameList = new ArrayList<>();

        for (Document game : games) {
            Map<String, Object> gameMap = new LinkedHashMap<>(); // use linkedhashmap to maintain order of keys
            gameMap.put("game_id", game.getObjectId("_id").toString());
            gameMap.put("name", game.getString("name"));
            gameList.add(gameMap);
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("games", gameList);
        response.put("offset", offset);
        response.put("limit", limit);
        response.put("total", gameRepository.countAllGames());
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    // get games sorted by rank (ascending)
    public Map<String, Object> getGamesByRank(Integer limit, Integer offset) {
        List<Document> games = gameRepository.findAllGamesByRank(limit, offset);
        List<Map<String, Object>> gameList = new ArrayList<>();

        for (Document game : games) {
            Map<String, Object> gameMap = new LinkedHashMap<>();
            gameMap.put("game_id", game.getObjectId("_id").toString());
            gameMap.put("name", game.getString("name"));
            gameMap.put("ranking", game.getInteger("ranking"));
            gameList.add(gameMap);
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("games", gameList);
        response.put("offset", offset);
        response.put("limit", limit);
        response.put("total", gameRepository.countAllGames());
        response.put("timestamp", System.currentTimeMillis());

        return response;
    }

    // get game details by ObjectId(_id)
    public Map<String, Object> getGameDetails(String id) {
        Document game = gameRepository.findGameById(id);
        Map<String, Object> gameDetails = new LinkedHashMap<>();

        if (game != null) {
            gameDetails.put("game_id", game.getObjectId("_id").toString());
            gameDetails.put("name", game.getString("name"));
            gameDetails.put("year", game.getInteger("year"));
            gameDetails.put("ranking", game.getInteger("ranking"));
            // gameDetails.put("average", calculateAverageRating(game));
            gameDetails.put("users_rated", game.getInteger("users_rated"));
            gameDetails.put("url", game.getString("url"));
            gameDetails.put("thumbnail", game.getString("image"));
            gameDetails.put("timestamp", System.currentTimeMillis());
        }

        return gameDetails;
    }

    public Map<String, Object> getGameWithReviews(String gameId) {
        Document game = gameRepository.findGameById(gameId);
        Map<String, Object> response = new LinkedHashMap<>();

        if (game != null) {
            response.put("game_id", game.getObjectId("_id").toString());
            response.put("name", game.getString("name"));
            response.put("year", game.getInteger("year"));
            response.put("rank", game.getInteger("ranking"));
            // response.put("average", calculateAverageRating(game)); // Implement this
            // method
            response.put("users_rated", game.getInteger("users_rated"));
            response.put("url", game.getString("url"));
            response.put("thumbnail", game.getString("image"));

            // Get reviews for the game
            List<Document> reviews = reviewRepository.findReviewsByGameId(game.getInteger("gid"));
            List<String> reviewLinks = new ArrayList<>();
            for (Document review : reviews) {
                reviewLinks.add("/review/" + review.getObjectId("_id").toString());
            }
            response.put("reviews", reviewLinks);

            response.put("timestamp", Instant.now().toString());
        }

        return response;
    }

    public Map<String, Object> getGamesWithHighestRating() {
        List<Document> reviews = reviewRepository.findReviewsWithHighestRating();
        return buildRatingResponse("highest", reviews);
    }

    public Map<String, Object> getGamesWithLowestRating() {
        List<Document> reviews = reviewRepository.findReviewsWithLowestRating();
        return buildRatingResponse("lowest", reviews);
    }

    private Map<String, Object> buildRatingResponse(String ratingType, List<Document> reviews) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("rating", ratingType);
        response.put("games", new ArrayList<>());

        for (Document review : reviews) {
            Map<String, Object> gameDetails = new LinkedHashMap<>();
            gameDetails.put("_id", review.getInteger("gameId"));
            gameDetails.put("name", getGameNameById(review.getInteger("gameId"))); // Implement this method
            gameDetails.put("rating", review.getInteger("rating"));
            gameDetails.put("user", review.getString("user"));
            gameDetails.put("comment", review.getString("comment"));
            gameDetails.put("review_id", review.getObjectId("_id").toString());
            ((List<Map<String, Object>>) response.get("games")).add(gameDetails);
        }

        response.put("timestamp", Instant.now().toString());
        return response;
    }

    private String getGameNameById(int gameId) {
        Query query = Query.query(Criteria.where("gid").is(gameId));
        Document game = mongoTemplate.findOne(query, Document.class, "games");
        return game != null ? game.getString("name") : "Unknown Game";
    }
}
