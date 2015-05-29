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
        if(androidLauncher.isInternetConnected()) {
            try {
                UserScore[] u = req.execute().get();
                Arrays.sort(u);
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
