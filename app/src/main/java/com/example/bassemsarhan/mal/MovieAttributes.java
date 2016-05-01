package com.example.bassemsarhan.mal;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Bassem Sarhan on 4/15/2016.
 */
public class MovieAttributes {String poster;
    String title;
    String date;
    String rate;
    String discraption;
    String id;
    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
    public void setDiscraption(String discraption) {
        this.discraption = discraption;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoster() {
        return poster;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getRate() {
        return rate;
    }

    public String getDiscraption() {
        return discraption;
    }

    public String getId() {
        return id;
    }

}
