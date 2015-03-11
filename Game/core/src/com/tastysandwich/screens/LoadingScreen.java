package com.tastysandwich.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tastysandwich.helpers.AssetLoader;

/**
 * Created by Filip on 24.2.2015.
 */
public class LoadingScreen implements Screen {

    private ShapeRenderer shapeRenderer;

    private int width, height;

    public LoadingScreen(int width, int height) {
        AssetLoader.load(width, height);
        this.width = width;
        this.height = height;

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(width/3, height/3, width/3 *2, height/3 *2);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLUE);
        //float r = width/3 *(AssetLoader.getLoadingPercent() / 100);
        //shapeRenderer.rect(width/3 , height/3, r, height/3 *2);



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
