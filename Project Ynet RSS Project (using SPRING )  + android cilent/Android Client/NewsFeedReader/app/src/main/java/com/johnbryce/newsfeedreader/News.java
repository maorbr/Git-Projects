package com.johnbryce.newsfeedreader;

import java.io.Serializable;

public class News implements Serializable {

    private long id;
    private String title;
    private String description;
    private String author;
    private String link;
    private String pubDate;

    public News(long id, String title, String description, String author, String link, String pubDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.link = link;
        this.pubDate = pubDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
}
