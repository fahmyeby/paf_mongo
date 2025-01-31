package com.example.paf_ws26.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.example.paf_ws26.model.Comment;

@Repository
public class CommentRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Comment findById(String id) {
        return mongoTemplate.findById(id, Comment.class, "comments");
    }
}
