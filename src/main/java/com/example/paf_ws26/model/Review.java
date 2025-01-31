package com.example.paf_ws26.model;

import java.util.Date;
import java.util.List;

public class Review {
    private String id;
    private String user;
    private int rating;
    private String comment;
    private int gameId;
    private Date posted;
    private String name;
    private List<Edit> edited;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public Date getPosted() {
        return posted;
    }

    public void setPosted(Date posted) {
        this.posted = posted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Edit> getEdited() {
        return edited;
    }

    public void setEdited(List<Edit> edited) {
        this.edited = edited;
    }
}
