package com.kingmonkey.jiovany.popularmovies2.model;

/**
 * Created by jiovany on 17/04/17.
 */

public class Review {
    private String id;
    private String author;
    private String content;
    private String url;

    public Review(String id, String author, String content, String url) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }
}
