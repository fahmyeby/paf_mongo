package com.example.paf_ws26.service;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.paf_ws26.repository.ReviewRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    // get review details by ObjectId
    public Map<String, Object> getReviewById(String id) {
        Document review = reviewRepository.findReviewById(id);
        Map<String, Object> reviewDetails = new LinkedHashMap<>();

        if (review != null) {
            reviewDetails.put("user", review.getString("user"));
            reviewDetails.put("rating", review.getInteger("rating"));
            reviewDetails.put("comment", review.getString("comment"));
            reviewDetails.put("id", review.getObjectId("_id").toString());
            reviewDetails.put("posted", review.getDate("posted").toInstant().toString());
            reviewDetails.put("name", review.getString("name"));
            reviewDetails.put("edited", review.getList("edited", Document.class));
            reviewDetails.put("timestamp", new Date());
        }

        return reviewDetails;
    }
    

    // add new review
    public Map<String, Object> addReview(String name, int rating, String comment, int gameId) {
        Document review = new Document();
        review.put("user", name);
        review.put("rating", rating);
        review.put("comment", comment);
        review.put("gameId", gameId);
        review.put("posted", Instant.now());
        review.put("name", name);
        review.put("edited", new ArrayList<>());

        Document savedReview = reviewRepository.saveReview(review);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("user", savedReview.getString("user"));
        response.put("rating", savedReview.getInteger("rating"));
        response.put("comment", savedReview.getString("comment"));
        response.put("id", savedReview.getObjectId("_id").toString());
        response.put("posted", savedReview.getDate("posted").toInstant().toString());
        response.put("name", savedReview.getString("name"));
        response.put("edited", savedReview.getList("edited", Document.class));
        response.put("timestamp", new Date());

        return response;
    }

    // update review
    public Map<String, Object> updateReview(String id, int rating, String comment) {
        Document review = reviewRepository.findReviewById(id);

        if (review != null) {
            // null handling
            if (review.get("edited") == null) {
                review.put("edited", new ArrayList<>());
            }

            // new edit entry
            Document edit = new Document();
            edit.put("comment", review.getString("comment"));
            edit.put("rating", review.getInteger("rating"));
            edit.put("posted", review.getDate("posted"));

            // update review
            review.put("rating", rating);
            review.put("comment", comment);
            review.put("posted", Instant.now());

            // add edit to edited list
            List<Document> edited = review.getList("edited", Document.class);
            edited.add(edit);
            review.put("edited", edited);

            // save updated review
            Document updatedReview = reviewRepository.updateReview(id, review);

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("user", updatedReview.getString("user"));
            response.put("rating", updatedReview.getInteger("rating"));
            response.put("comment", updatedReview.getString("comment"));
            response.put("id", updatedReview.getObjectId("_id").toString());
            response.put("posted", updatedReview.getDate("posted").toInstant().toString());
            response.put("name", updatedReview.getString("name"));
            response.put("edited", updatedReview.getList("edited", Document.class));
            response.put("timestamp", new Date());

            return response;
        }

        return null;
    }

    // get review history
    public Map<String, Object> getReviewHistory(String id) {
        Document review = reviewRepository.findReviewById(id);
        Map<String, Object> reviewHistory = new LinkedHashMap<>();

        if (review != null) {
            reviewHistory.put("user", review.getString("user"));
            reviewHistory.put("rating", review.getInteger("rating"));
            reviewHistory.put("comment", review.getString("comment"));
            reviewHistory.put("id", review.getObjectId("_id").toString());
            reviewHistory.put("posted", review.getDate("posted").toInstant().toString());
            reviewHistory.put("name", review.getString("name"));
            reviewHistory.put("edited", review.getList("edited", Document.class));
            reviewHistory.put("timestamp", new Date());
        }

        return reviewHistory;
    }
}
