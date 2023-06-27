package com.example.assignment;

public class GameRecord {
    private String playDate;
    private String playTime;
    private int moves;

    public GameRecord(String playDate, String playTime, int moves) {
        this.playDate = playDate;
        this.playTime = playTime;
        this.moves = moves;
    }

    public String getPlayDate() {
        return playDate;
    }

    public String getPlayTime() {
        return playTime;
    }

    public int getMoves() {
        return moves;
    }
}
