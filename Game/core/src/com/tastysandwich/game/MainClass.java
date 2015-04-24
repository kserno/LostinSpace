package com.tastysandwich.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.tastysandwich.helpers.AssetLoader;
import com.tastysandwich.screens.LoadingScreen;
import com.tastysandwich.screens.Menu;

public class MainClass extends Game{

    private AdsController adsController;
    private AssetManager manager;

    public MainClass(AdsController adsController){
        this.adsController = adsController;
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
        setScreen(new Menu(width, height, this, adsController, manager));
    }

    @Override
    public void dispose() {
        super.dispose();
        AssetLoader.dispose();
        manager.dispose();
    }
}
