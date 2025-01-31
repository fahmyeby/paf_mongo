package com.example.paf_ws26.repository;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.DeleteResult;

@Repository
public class ReviewRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String reviews = "reviews";

    // find review by ObjectId
    // MongoDB Query: db.reviews.findOne({ _id: ObjectId("<id>") })
    public Document findReviewById(String id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        return mongoTemplate.findOne(query, Document.class, reviews);
    }

    // find all reviews for a specific game
    // MongoDB Query: db.reviews.find({ gameId: <gameId> }).toArray()
    public List<Document> findReviewsByGameId(int gameId) {
        Query query = Query.query(Criteria.where("gameId").is(gameId));
        return mongoTemplate.find(query, Document.class, reviews);
    }

    // save new review
    // MongoDB Query: db.reviews.insertOne({ gameId: <gameId>, rating: <rating>,
    // comment: "<comment>" })
    public Document saveReview(Document review) {
        return mongoTemplate.save(review, reviews);
    }

    // update review
    // MongoDB Query: db.reviews.findOneAndReplace({ _id: ObjectId("<id>") }, {
    // gameId: <gameId>, rating: <newRating>, comment: "<newComment>" })
    public Document updateReview(String id, Document updatedReview) {
        Query query = Query.query(Criteria.where("_id").is(id));
        return mongoTemplate.findAndReplace(query, updatedReview, reviews);
    }

    // delete review
    // MongoDB Query: db.reviews.deleteOne({ _id: ObjectId("<id>") })
    public boolean deleteReviewById(String id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        DeleteResult result = mongoTemplate.remove(query, reviews);
        return result.getDeletedCount() > 0;
    }

    // find review with highest/lowest rating
    public List<Document> findReviewsWithHighestRating() {
        SortOperation sortByRating = Aggregation.sort(Sort.by(Sort.Direction.DESC, "rating"));
        LimitOperation limit = Aggregation.limit(1);
        Aggregation pipeline = Aggregation.newAggregation(sortByRating, limit);
        AggregationResults<Document> results = mongoTemplate.aggregate(pipeline, "reviews", Document.class);
        return results.getMappedResults();
    }

    public List<Document> findReviewsWithLowestRating() {
        SortOperation sortByRating = Aggregation.sort(Sort.by(Sort.Direction.ASC, "rating"));
        LimitOperation limit = Aggregation.limit(1);
        Aggregation pipeline = Aggregation.newAggregation(sortByRating, limit);
        AggregationResults<Document> results = mongoTemplate.aggregate(pipeline, "reviews", Document.class);
        return results.getMappedResults();
    }
}
