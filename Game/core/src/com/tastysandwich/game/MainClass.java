package com.tastysandwich.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.tastysandwich.helpers.AssetLoader;
import com.tastysandwich.screens.Menu;

public class MainClass extends Game{

    @Override
    public void create() {
        int height = Gdx.graphics.getHeight();
        int width = Gdx.graphics.getWidth();
        AssetLoader.load(width, height);
        setScreen(new Menu(width, height, this));
    }

    @Override
    public void dispose() {
        super.dispose();
        AssetLoader.dispose();
    }
}
