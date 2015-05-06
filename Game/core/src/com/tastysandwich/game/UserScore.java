package com.tastysandwich.game;

/**
 * Created by Filip on 3.5.2015.
 */
public class UserScore implements Comparable<UserScore> {

    private int score;
    private String user;

    public UserScore(String user, int score) {
        this.score = score;
        this.user = user;

    }

    public String getUser() {
        return user;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(UserScore o) {
        return o.getScore()- this.getScore() ;
    }
}
