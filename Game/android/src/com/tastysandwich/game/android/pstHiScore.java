package com.tastysandwich.game.android;

import com.tastysandwich.game.PostHiScore;
import com.tastysandwich.game.UserScore;
import com.tastysandwich.game.android.requests.postHiScore;

import java.util.concurrent.ExecutionException;

/**
 * Created by Filip on 5.5.2015.
 */
public class pstHiScore implements PostHiScore {

    private AndroidLauncher androidLauncher;
    public pstHiScore(AndroidLauncher androidLauncher) {
        this.androidLauncher = androidLauncher;
    }

    @Override
    public Boolean post(UserScore userScore) {
        postHiScore p = new postHiScore();

        if(androidLauncher.isInternetConnected()) {
            try {
                return p.execute(userScore).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
