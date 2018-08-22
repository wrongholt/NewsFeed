package com.example.android.newsfeed;

public class NewsArticle {
    private String heading;
    private String details;
    private String mUrl;
    private String mDate;
    private String mAuthor;


    public NewsArticle(String heading, String details, String date, String author, String url) {
        this.heading = heading;
        this.details = details;
        this.mUrl = url;
        this.mDate = date;
        this.mAuthor = author;
    }

    public NewsArticle(String heading, String details, String date, String url) {
        this.heading = heading;
        this.details = details;
        this.mUrl = url;
        this.mDate = date;
    }

    public String getmDate() {
        return mDate;
    }

    public String getHeading() {
        return heading;
    }

    public String getDetails() {
        return details;
    }

    public String getmUrl() {
        return mUrl;
    }

    public String getmAuthor() {
        return mAuthor;
    }
}
