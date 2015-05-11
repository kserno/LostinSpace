package com.tastysandwich.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.tastysandwich.game.AdsController;
import com.tastysandwich.game.MainClass;
import com.tastysandwich.game.PostHiScore;
import com.tastysandwich.game.RequestHiScore;
import com.tastysandwich.gameworld.GameRenderer;
import com.tastysandwich.gameworld.GameWorld;
import com.tastysandwich.helpers.InputHandler;

/**
 * Created by solit_000 on 5.2.2015.
 */
public class GameScreen implements Screen {

    private GameWorld world;
    private GameRenderer renderer;
    private float runTime = 0;

    public GameScreen(float width, float height, AdsController adsController, MainClass game, AssetManager manager, Music music, boolean clicking, PostHiScore p, RequestHiScore r) {
        world = new GameWorld(width, height, adsController, manager, music, clicking, p, r);
        renderer = new GameRenderer(world, width, height);
        Gdx.input.setInputProcessor(new InputHandler(world.getShip(), world, game, manager, clicking, p, r));
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (world.getCurrentState()!= GameWorld.GameState.PAUSE) runTime += delta;
        world.update(delta, runTime);
        renderer.render(runTime, delta);
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
}
