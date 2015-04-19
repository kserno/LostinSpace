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
    private Sprite scoreTable, highscoreTable;
    private Sprite[] energyBar;
    private Sprite pause, pauseScreen, startScreen;
    private Sprite shieldOff, shieldOn;

    private ImageButton ibTryAgain, ibMenu;

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
        highscoreTable = AssetLoader.highscorebg;
        scoreTable = AssetLoader.scorebg;
        ibTryAgain = myWorld.getIbTryAgain();
        ibMenu = myWorld.getIbMenu();
        energyBar = AssetLoader.energyBar;
        pause = AssetLoader.pause;
        pause.setPosition(width - width / 10, height/20);
        pauseScreen = AssetLoader.pauseScreen;
        startScreen = AssetLoader.startScreen;
        shieldOff = AssetLoader.shieldOff;
        shieldOn = AssetLoader.shieldOn;
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
        renderEnergyBar();
        AssetLoader.font.draw(batcher, "" + myWorld.getScore(), width / 2 - AssetLoader.font.getBounds(String.valueOf(myWorld.getScore())).width / 2, height / 16);
        pauseScreen.draw(batcher);
    }

    private void renderReady(float runTime) {
        drawBackground(true);
        batcher.enableBlending();
        batcher.draw(shipAnimation.getKeyFrame(runTime), ship.getX(), ship.getY(), ship.getWidth() / 2.0f, ship.getHeight() / 2.0f, ship.getWidth(), ship.getHeight(), 1, 1, ship.getRotation());
        renderEnergyBar();
        AssetLoader.font.draw(batcher, "" + myWorld.getScore(), width / 2 - AssetLoader.font.getBounds(String.valueOf(myWorld.getScore())).width / 2, height / 16);
        startScreen.draw(batcher);
    }

    private void renderEnergyBar() {
       energyBar[ship.getCurrentEnergy()].draw(batcher);
       if(ship.getShield()) { shieldOn.draw(batcher); }
        else { shieldOff.draw(batcher); }
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
        AssetLoader.font.draw(batcher, ""+ myWorld.getScore(), width / 2 - AssetLoader.font.getBounds(String.valueOf(myWorld.getScore())).width / 2, height/16);
        pause.draw(batcher);
    }


    private void renderDying(float runTime) {
        drawBackground(true);
        batcher.enableBlending();
        batcher.draw(shipAnimation.getKeyFrame(runTime), ship.getX(), ship.getY(), ship.getWidth() / 2.0f, ship.getHeight() / 2.0f, ship.getWidth(), ship.getHeight(), 1, 1, ship.getRotation());
        batcher.draw(explosionAnimation.getKeyFrame(myWorld.dyingTime), ship.getX()- ship.getWidth() / 4, ship.getY(), ship.getWidth() * 1.5f, ship.getHeight()*1.5f);
        AssetLoader.font.draw(batcher, "" + myWorld.getScore(), width / 2 - AssetLoader.font.getBounds(String.valueOf(myWorld.getScore())).width / 2, height / 16);

    }

    private void renderGameOver() {
        drawBackground(true);
        scoreTable.draw(batcher);
        ibTryAgain.draw(batcher, 50f);
        ibMenu.draw(batcher, 50f);
        String score = myWorld.getScore() + "";
        AssetLoader.font.draw(batcher, ""+ myWorld.getScore(),width / 2 - AssetLoader.font.getBounds(score).width / 2, height/2 + height / 4);
    }

    private void renderHiScore() {
        drawBackground(true);
        highscoreTable.draw(batcher);
        ibTryAgain.draw(batcher, 50f);
        ibMenu.draw(batcher, 50f);
        String score = myWorld.getScore() + "";
        AssetLoader.font.draw(batcher, ""+ myWorld.getScore(),width / 2 - AssetLoader.font.getBounds(score).width / 2, height/2 + height / 4);
    }

}