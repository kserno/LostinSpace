package com.tastysandwich.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tastysandwich.gameobjects.Asteroid;
import com.tastysandwich.gameobjects.Ship;
import com.tastysandwich.gameobjects.Energy;
import com.tastysandwich.helpers.AssetLoader;
import com.tastysandwich.helpers.InputHandler;

import javax.swing.plaf.TextUI;

/**
 * Created by solit_000 on 5.2.2015.
 */
public class GameRenderer {

    private GameWorld myWorld;
    private OrthographicCamera cam;

    private SpriteBatch batcher;
    private Sprite background,stars1,stars2;
    private Animation shipAnimation;
    private Animation energyAnimation;
    private Animation explosionAnimation;

    private float offset = 0,offset2 = 0,offset3 = 0;
    private float width, height;
    private float bgSpeed, bgSpeed2, bgSpeed3;

    private Ship ship;
    private Energy energy;
    private Texture textAsteroids[];
    private TextureRegion scoreTable, highscoreTable;
    private Texture[] energyBar;
    private Sprite pause;

    private ImageButton ibTryAgain;

    private boolean updateEnergy = false;

    public GameRenderer(GameWorld world, float width, float height) {
        myWorld = world;
        this.width = width;
        this.height = height;
        bgSpeed = width / 10;
        bgSpeed2 = width / 8;
        bgSpeed3 = width / 6;
        cam = new OrthographicCamera();
        cam.setToOrtho(true,width,height);
        cam.position.set(width/2f, height/2f, 0);

        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);

        initAssets();
        initGameObjects();
    }

    private void initAssets() {
        background = AssetLoader.background;
        stars1 = AssetLoader.stars1;
        stars2 = AssetLoader.stars2;
        shipAnimation = AssetLoader.shipAnimation;
        textAsteroids = AssetLoader.asteroids;
        explosionAnimation = AssetLoader.explosionAnimation;
        energyAnimation = AssetLoader.energyAnimation;
        highscoreTable = AssetLoader.thighscorebg;
        scoreTable = AssetLoader.tscorebg;
        ibTryAgain = myWorld.getIbTryAgain();
        energyBar = AssetLoader.energyBar;
        pause = AssetLoader.pause;
        pause.setPosition(width - width / 10, height/20);
    }
    private void initGameObjects() {
        ship = myWorld.getShip();
    }

    public void render(float runTime) {
        if (!myWorld.geteIsActive()) {
            energy = null;
            updateEnergy = false;
        }
        if (!updateEnergy && myWorld.geteIsActive()){
            energy = myWorld.getEnergy();
            updateEnergy = true;
        }


        switch (myWorld.getCurrentState()) {
            case RUNNING:
                renderRunning(runTime);
                break;
            case GAMEOVER:
                renderGameOver();
                break;
            case HISCORE:
                renderHiScore();
                break;
            case READY:
                renderReady(runTime);
                break;
            case PAUSE:
                renderPause(runTime);
                break;
            case DYING:
                renderDying(runTime);
        }
        batcher.end();
    }

    private void renderPause(float runTime) {
        drawBackground(false);
        batcher.enableBlending();
        batcher.draw(shipAnimation.getKeyFrame(runTime), ship.getX(), ship.getY(), ship.getWidth() / 2.0f, ship.getHeight() / 2.0f, ship.getWidth(), ship.getHeight(), 1, 1, ship.getRotation());
        for (int i = 0; i <= myWorld.nAsteroids; i++) {
            batcher.draw(textAsteroids[myWorld.asteroids[i].getType()], myWorld.asteroids[i].getX(), myWorld.asteroids[i].getY(), myWorld.asteroids[i].getRadius() / 2.0f, myWorld.asteroids[i].getRadius() / 2.0f,  myWorld.asteroids[i].getRadius(), myWorld.asteroids[i].getRadius(), 1, 1,
                    myWorld.asteroids[i].getRotation(), 0, 0, 1000, 1000, false, false);
        }

        if (updateEnergy) {
            batcher.draw(energyAnimation.getKeyFrame(runTime), energy.getX(), energy.getY(), energy.getRadius()*2, energy.getRadius()*2);
        }
        AssetLoader.font.draw(batcher, "" + myWorld.getScore(), width / 2 - AssetLoader.font.getBounds(String.valueOf(myWorld.getScore())).width / 2, height / 6);
        AssetLoader.font.draw(batcher, "Touch to resume", width/3, height/2);
        renderEnergyBar();
    }

    private void renderReady(float runTime) {
        drawBackground(true);
        batcher.enableBlending();
        batcher.draw(shipAnimation.getKeyFrame(runTime), ship.getX(), ship.getY(), ship.getWidth() / 2.0f, ship.getHeight() / 2.0f, ship.getWidth(), ship.getHeight(), 1, 1, ship.getRotation());
        renderEnergyBar();
        AssetLoader.font.draw(batcher, "" + myWorld.getScore(), width / 2 - AssetLoader.font.getBounds(String.valueOf(myWorld.getScore())).width / 2, height / 6);
        AssetLoader.font.draw(batcher, "Touch to start", width/3, height/2);
    }

    private void renderEnergyBar() {
        batcher.draw(energyBar[ship.getCurrentEnergy()], (width / 4) * 3, (height / 18) * 16, width / 4, height/18);
    }

    private void drawBackground(boolean moving) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(moving) {
            if(offset < -width) offset = 0;
            if(offset2 < -width) offset2 = 0;
            if(offset3 < -width) offset3 = 0;
            offset = offset - bgSpeed * Gdx.graphics.getDeltaTime() * myWorld.getGameSpeed();
            offset2 = offset2 - bgSpeed2 * myWorld.getGameSpeed() * Gdx.graphics.getDeltaTime();
            offset3 = offset3 - bgSpeed3 * myWorld.getGameSpeed() * Gdx.graphics.getDeltaTime();
        }

        batcher.begin();
        background.setX(offset);
        background.draw(batcher);
        background.setX(offset + width);
        background.draw(batcher);

        stars1.setX(offset2);
        stars1.draw(batcher);
        stars1.setX(offset2 + width);
        stars1.draw(batcher);

        stars2.setX(offset3);
        stars2.draw(batcher);
        stars2.setX(offset3 + width);
        stars2.draw(batcher);
    }

    private void renderRunning(float runTime) {
        drawBackground(true);
        batcher.enableBlending();
        batcher.draw(shipAnimation.getKeyFrame(runTime), ship.getX(), ship.getY(), ship.getWidth() / 2.0f, ship.getHeight() / 2.0f, ship.getWidth(), ship.getHeight(), 1, 1, ship.getRotation());
        for (int i = 0; i <= myWorld.nAsteroids; i++) {
            batcher.draw(textAsteroids[myWorld.asteroids[i].getType()], myWorld.asteroids[i].getX(), myWorld.asteroids[i].getY(), myWorld.asteroids[i].getRadius() / 2.0f, myWorld.asteroids[i].getRadius() / 2.0f,  myWorld.asteroids[i].getRadius(), myWorld.asteroids[i].getRadius(), 1, 1,
                    myWorld.asteroids[i].getRotation(), 0, 0, 1000, 1000, false, false);
        }
        if (updateEnergy) {
            batcher.draw(energyAnimation.getKeyFrame(runTime), energy.getX(), energy.getY(), energy.getRadius()*2, energy.getRadius()*2);
        }
        renderEnergyBar();
        AssetLoader.font.draw(batcher, ""+ myWorld.getScore(), width / 2 - AssetLoader.font.getBounds(String.valueOf(myWorld.getScore())).width / 2, height/6);
        pause.draw(batcher);
    }


    private void renderDying(float runTime) {
        drawBackground(true);
        batcher.enableBlending();
        batcher.draw(shipAnimation.getKeyFrame(runTime), ship.getX(), ship.getY(), ship.getWidth() / 2.0f, ship.getHeight() / 2.0f, ship.getWidth(), ship.getHeight(), 1, 1, ship.getRotation());
        batcher.draw(explosionAnimation.getKeyFrame(myWorld.dyingTime), ship.getX(), ship.getY(), ship.getWidth() * 1.5f, ship.getHeight()*1.5f);
        AssetLoader.font.draw(batcher, "" + myWorld.getScore(), width / 2 - AssetLoader.font.getBounds(String.valueOf(myWorld.getScore())).width / 2, height / 6);

    }

    private void renderGameOver() {
        drawBackground(true);
        batcher.enableBlending();
        batcher.draw(scoreTable, width /8, height/8, width-(width/8+width/8), height-(height/8+height/8));
        ibTryAgain.draw(batcher, 50f);
        String score = myWorld.getScore() + "";
        AssetLoader.font.draw(batcher, ""+ myWorld.getScore(),width / 2 - AssetLoader.font.getBounds(score).width / 2, height/2 + height / 4);
    }

    private void renderHiScore() {
        drawBackground(true);
        batcher.enableBlending();
        batcher.draw(highscoreTable, width /8, height/8, width -(width/8+width/8), height-(height/8+height/8));
        ibTryAgain.draw(batcher, 50f);
        String score = myWorld.getScore() + "";
        AssetLoader.font.draw(batcher, ""+ myWorld.getScore(),width / 2 - AssetLoader.font.getBounds(score).width / 2, height/2 + height / 4);
    }

}