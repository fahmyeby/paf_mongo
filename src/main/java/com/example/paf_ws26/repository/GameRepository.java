package com.example.paf_ws26.repository;

import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class GameRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String games = "games";
    private static final String reviews = "reviews";

    // find all games with limit and offset
    // MongoDB Query: db.games.find().skip(<offset>).limit(<limit>)
    public List<Document> findAllGames(Integer limit, Integer offset) {
    Query query = new Query().skip(offset).limit(limit);
    return mongoTemplate.find(query, Document.class, games);
    }

    // find games sorted by rank
    // MongoDB Query: db.games.find().sort({ ranking: 1 }).skip(<offset>).limit(<limit>)
    public List<Document> findAllGamesByRank(Integer limit, Integer offset) {
        Query query = new Query().skip(offset).limit(limit)
        .with(Sort.by(Sort.Direction.ASC, "ranking"));
        return mongoTemplate.find(query, Document.class, games);
    }

    // find game by ObjectId
    // MongoDB Query: db.games.findOne({ _id: ObjectId("<id>") })
    public Document findGameById(String id){
        Query query = Query.query(Criteria.where("_id").is(id));
        return mongoTemplate.findOne(query, Document.class, games);
    }

    // count total number of games
    // MongoDB Query: db.games.countDocuments()
    public Long countAllGames(){
        return mongoTemplate.count(new Query(), games);
    }

    // find game with reviews
    public Document findGameWithReviews(String gameId) {
    MatchOperation matchGame = Aggregation.match(Criteria.where("_id").is(new ObjectId(gameId)));
    LookupOperation lookupReviews = LookupOperation.newLookup()
            .from("reviews")
            .localField("gid")
            .foreignField("gameId")
            .as("reviews");

    Aggregation pipeline = Aggregation.newAggregation(matchGame, lookupReviews);
    AggregationResults<Document> results = mongoTemplate.aggregate(pipeline, "games", Document.class);

    return results.getUniqueMappedResult();
}
}
