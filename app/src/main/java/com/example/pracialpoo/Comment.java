package com.example.pracialpoo;

import java.io.Serializable;

public class Comment implements Serializable {
    private String content;
    private String date;
    private float score;
    private int number;
    private String nickname;
    public Comment(){

    }

    public Comment(String content, String date, float score, int number, String nickname) {
        this.content = content;
        this.date = date;
        this.score = score;
        this.number = number;
        this.nickname = nickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
