package com.tastysandwich.game.android;

import com.badlogic.gdx.Gdx;
import com.google.android.gms.common.UserRecoverableException;
import com.tastysandwich.game.RequestHiScore;
import com.tastysandwich.game.UserScore;
import com.tastysandwich.game.android.requests.requestHiScore;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

/**
 * Created by Filip on 5.5.2015.
 */
public class reqHiScore implements RequestHiScore {

    private AndroidLauncher androidLauncher;
    public reqHiScore(AndroidLauncher androidLauncher) {
        this.androidLauncher = androidLauncher;
    }

    @Override
    public com.tastysandwich.game.UserScore[] getUserScores() {
        requestHiScore req = new requestHiScore();
        System.out.println("req");
        if(androidLauncher.isInternetConnected()) {
            try {
                System.out.println("in try");
                UserScore[] u = req.execute().get();
                Arrays.sort(u);
                for (int i = 0; i < u.length; i++) {
                    System.out.println(String.valueOf(u[i].getScore()));
                    System.out.println(u[i].getUser());
                }
                return u;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
