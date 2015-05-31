package com.tastysandwich.game.android;

import com.tastysandwich.game.RequestUsername;
import com.tastysandwich.game.android.requests.requestUsername;

import java.util.concurrent.ExecutionException;

/**
 * Created by Filip on 30.5.2015.
 */
public class reqUsername implements RequestUsername {

    private AndroidLauncher androidLauncher;

    public reqUsername(AndroidLauncher androidLauncher) {
        this.androidLauncher = androidLauncher;
    }

    @Override
    public Boolean requestUsername(String username) {
        requestUsername r = new requestUsername();
        if (androidLauncher.isInternetConnected()) {
            try {
                Boolean b = r.execute(username).get();
                return b;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public Boolean requestUsername(String username, String lusername) {
        requestUsername r = new requestUsername();
        if (androidLauncher.isInternetConnected()) {
            try {
                Boolean b = r.execute(username, lusername).get();
                return b;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


}
