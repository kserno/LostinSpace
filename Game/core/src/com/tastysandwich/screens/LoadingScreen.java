package com.tastysandwich.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tastysandwich.game.MainClass;

/**
 * Created by solit_000 on 24.4.2015.
 */
public class LoadingScreen implements Screen {
    private MainClass game;
    private Texture bg;
    private SpriteBatch batch;
    private AssetManager manager;
    public LoadingScreen(MainClass game) {
        manager = new AssetManager();
        loadSounds();
        this.game = game;

        batch = new SpriteBatch();
        bg = new Texture(Gdx.files.internal("data/loading.jpg"));
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        manager.update();
        batch.begin();
        batch.draw(bg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        if(manager.update()) {
            game.start(manager);
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
    private void loadSounds() {
        manager.load("data/audio/explosion.mp3", Sound.class);
        manager.load("data/audio/background_music.mp3", Music.class);
    }
}
