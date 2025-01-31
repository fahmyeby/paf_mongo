package com.example.paf_ws26.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.paf_ws26.service.ReviewService;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // get review by ObjectId
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getReviewById(@PathVariable String id) {
        Map<String, Object> review = reviewService.getReviewById(id);
        if (review != null) {
            return ResponseEntity.ok(review);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // add new review
    @PostMapping
    public ResponseEntity<Map<String, Object>> addReview(
            @RequestParam String name,
            @RequestParam int rating,
            @RequestParam(required = false) String comment,
            @RequestParam int gameId) {
        Map<String, Object> response = reviewService.addReview(name, rating, comment, gameId);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // update review
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateReview(
            @PathVariable String id,
            @RequestParam int rating,
            @RequestParam(required = false) String comment) {
        Map<String, Object> response = reviewService.updateReview(id, rating, comment);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // get review history
    @GetMapping("/{id}/history")
    public ResponseEntity<Map<String, Object>> getReviewHistory(@PathVariable String id) {
        Map<String, Object> response = reviewService.getReviewHistory(id);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}