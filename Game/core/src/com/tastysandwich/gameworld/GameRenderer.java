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

    private float offset = 0,offset2 = 0,offset3 = 0;
    private Ship ship;
    private float width, height;
    private Energy energy;
    private Texture textAsteroids[];
    private TextureRegion scoreTable, highscoreTable;
    private Texture[] energyBar;

    private ImageButton ibTryAgain;

    private boolean updateEnergy = false;


    public GameRenderer(GameWorld world, float width, float height) {
        myWorld = world;
        this.width = width;
        this.height = height;
        cam = new OrthographicCamera();
        cam.setToOrtho(true,width,height);
        cam.position.set(width/2f, height/2f, 0);

        batcher = new SpriteBatch();
        // Attach batcher to camera
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
        energyAnimation = AssetLoader.energyAnimation;
        highscoreTable = AssetLoader.thighscorebg;
        scoreTable = AssetLoader.tscorebg;
        ibTryAgain = myWorld.getIbTryAgain();
        energyBar = AssetLoader.energyBar;
    }
    private void initGameObjects() {
        ship = myWorld.getShip();
    }

    public void render(float runTime) {

        drawBackground();
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
        }
        batcher.end();
    }

    private void renderReady(float runTime) {
        batcher.enableBlending();
        batcher.draw(shipAnimation.getKeyFrame(runTime), ship.getX(), ship.getY(), ship.getWidth() / 2.0f, ship.getHeight() / 2.0f, ship.getWidth(), ship.getHeight(), 1, 1, ship.getRotation());
        String score = myWorld.getScore() + "";
        renderEnergyBar();
        AssetLoader.font.draw(batcher, ""+ myWorld.getScore(), width/2 - (12 * score.length()), height/6);
    }

    private void renderEnergyBar() {
        batcher.draw(energyBar[ship.getCurrentEnergy()], (width / 4) * 3, (height / 18) * 16, width / 4, height/18);
    }

    private void drawBackground() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        offset -=  (width * Gdx.graphics.getDeltaTime()/(8/myWorld.getGameSpeed()));
        offset2 -=  (width * Gdx.graphics.getDeltaTime()/(4/myWorld.getGameSpeed()));
        offset3 -=  (width * Gdx.graphics.getDeltaTime()/(6/myWorld.getGameSpeed()));
        offset = offset % width;
        offset2 = offset2 % width;
        offset3 = offset3 % width;
        batcher.begin();
        background.setX(offset);
        background.draw(batcher);
        background.setX(offset + width);
        background.draw(batcher);

        stars2.setX(offset2);
        stars2.draw(batcher);
        stars2.setX(offset2 + width);
        stars2.draw(batcher);

        stars1.setX(offset3);
        stars1.draw(batcher);
        stars1.setX(offset3 + width);
        stars1.draw(batcher);


    }

    private void renderRunning(float runTime) {
        batcher.enableBlending();
        batcher.draw(shipAnimation.getKeyFrame(runTime), ship.getX(), ship.getY(), ship.getWidth() / 2.0f, ship.getHeight() / 2.0f, ship.getWidth(), ship.getHeight(), 1, 1, ship.getRotation());

        for (int i = 0; i <= myWorld.nAsteroids; i++) {
            batcher.draw(textAsteroids[myWorld.asteroids[i].getType()], myWorld.asteroids[i].getX(), myWorld.asteroids[i].getY(), myWorld.asteroids[i].getRadius() / 2.0f, myWorld.asteroids[i].getRadius() / 2.0f,  myWorld.asteroids[i].getRadius(), myWorld.asteroids[i].getRadius(), 1, 1,
                    myWorld.asteroids[i].getRotation(), 0, 0, 1000, 1000, false, false);
        }

        if (updateEnergy) {
            batcher.draw(energyAnimation.getKeyFrame(runTime), energy.getX(), energy.getY(), energy.getRadius()*2, energy.getRadius()*2);
        }
        String score = myWorld.getScore() + "";
        AssetLoader.font.draw(batcher, ""+ myWorld.getScore(), width/2 - (12 * score.length()), height/6);
        renderEnergyBar();
    }

    private void renderGameOver() {
        batcher.enableBlending();
        batcher.draw(scoreTable, width /8, height/8, width-(width/8+width/8), height-(height/8+height/8));
        ibTryAgain.draw(batcher, 50f);
        String score = myWorld.getScore() + "";
        AssetLoader.font.draw(batcher, ""+ myWorld.getScore(), width/2 - (12 * score.length()), height/2 + height / 4);
    }

    private void renderHiScore() {
        batcher.enableBlending();
        batcher.draw(highscoreTable, width /8, height/8, width -(width/8+width/8), height-(height/8+height/8));
        ibTryAgain.draw(batcher, 50f);
        String score = myWorld.getScore() + "";
        AssetLoader.font.draw(batcher, ""+ myWorld.getScore(), width/2 - (12 * score.length()), height/2 + height / 4);
    }

}
