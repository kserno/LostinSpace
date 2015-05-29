package com.tastysandwich.game;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.tastysandwich.helpers.AssetLoader;
import com.tastysandwich.screens.LoadingScreen;
import com.tastysandwich.screens.Menu;


public class MainClass extends Game{

    private AdsController adsController;
    private AssetManager manager;
    private PostHiScore p;
    private RequestHiScore r;

    public MainClass(AdsController adsController, PostHiScore p, RequestHiScore r ){
        this.adsController = adsController;
        this.p = p;
        this.r = r;
        UserScore[] userScores = r.getUserScores();
        //for (int i=0; i < userScores.length; i++) {
            //Gdx.app.log("Score", String.valueOf(userScores[i].getScore()));
           // Gdx.app.log("USer", userScores[i].getUser());
     //   }
    }

    @Override
    public void create() {
        setScreen(new LoadingScreen(this));
    }

    public void start(AssetManager manager) {
        this.manager = manager;
        int height = Gdx.graphics.getHeight();
        int width = Gdx.graphics.getWidth();
        AssetLoader.load(width, height);
        Gdx.input.setCatchBackKey(true);
        if(adsController.isInternetConnected()) adsController.loadAd();
        setScreen(new Menu(width, height, this, adsController, manager, p,r));

    }

    @Override
    public void dispose() {
        super.dispose();
        AssetLoader.dispose();
        manager.dispose();
    }
}
