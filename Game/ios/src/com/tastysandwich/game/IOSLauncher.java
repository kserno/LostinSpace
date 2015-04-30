package com.tastysandwich.game;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.gamekit.GKAchievementDescription;
import org.robovm.apple.uikit.UIApplication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.tastysandwich.game.MainClass;

public class IOSLauncher extends IOSApplication.Delegate implements AdsController {
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        return new IOSApplication(new MainClass(this), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }

    @Override
    public void showBannerAd() {

    }

    @Override
    public void hideBannerAd() {

    }

    @Override
    public boolean isInternetConnected() {
        return false;
    }

    @Override
    public void loadAd() {

    }
}