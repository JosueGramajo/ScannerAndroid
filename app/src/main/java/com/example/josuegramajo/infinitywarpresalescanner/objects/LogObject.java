package com.example.josuegramajo.infinitywarpresalescanner.objects;

/**
 * Created by josuegramajo on 3/23/18.
 */

public class LogObject {
    private String url;
    private String state;
    private String date;

    public LogObject(String url, String state, String date){
        this.url = url;
        this.state = state;
        this.date = date;
    }

    public LogObject(){}

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
