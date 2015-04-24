package com.tastysandwich.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tastysandwich.gameobjects.Ship;
import com.tastysandwich.gameobjects.Energy;
import com.tastysandwich.helpers.AssetLoader;

import java.util.Random;

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
    private Sprite scoreTable;
    private Sprite[] energyBar;
    private Sprite pause, pauseScreen, startScreen, hit;
    private Sprite shieldOff, shieldOn;

    private boolean updateEnergy = false;


    //Rumble
    private float time;
    private Random random;
    private float rotate;
    private float current_time;
    private float power;
    private float current_power;
    private float rotation;

    public GameRenderer(GameWorld world, float width, float height) {
        myWorld = world;
        this.width = width;
        this.height = height;
        bgSpeed = width / 10;
        bgSpeed2 = width / 8;
        bgSpeed3 = width / 6;

        time = 0.3f;
        current_time = 0;
        power = 2;
        current_power = 0;
        random = new Random();

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
        scoreTable = AssetLoader.scorebg;
        energyBar = AssetLoader.energyBar;
        pause = AssetLoader.pause;
        pause.setPosition(width - width / 10, height/20);
        pauseScreen = AssetLoader.pauseScreen;
        startScreen = AssetLoader.startScreen;
        hit = AssetLoader.hit;
        shieldOff = AssetLoader.shieldOff;
        shieldOn = AssetLoader.shieldOn;
    }
    private void initGameObjects() {
        ship = myWorld.getShip();
    }

    public void render(float runTime, float delta) {
        cam.update();
        batcher.setProjectionMatrix(cam.combined);
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
                renderRunning(runTime, delta);
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
                renderDying(runTime, delta);
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
        hit.draw(batcher);
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
            if(background.getX()<0) background.setX(width);
            if(stars1.getX()<0) stars1.setX(width);
            if(stars2.getX()<0) stars2.setX(width);

            offset =  bgSpeed * Gdx.graphics.getDeltaTime() * myWorld.getGameSpeed();
            offset2 = bgSpeed2 * myWorld.getGameSpeed() * Gdx.graphics.getDeltaTime();
            offset3 = bgSpeed3 * myWorld.getGameSpeed() * Gdx.graphics.getDeltaTime();
        }else {
            offset = 0;
            offset2 = 0;
            offset3 = 0;
        }

        batcher.begin();
        background.translateX(-width - offset);
        background.draw(batcher);
        background.translateX(width);
        background.draw(batcher);

        stars1.translateX(-width - offset2);
        stars1.draw(batcher);
        stars1.translateX(width);
        stars1.draw(batcher);

        stars2.translateX(-width - offset3);
        stars2.draw(batcher);
        stars2.translateX(width);
        stars2.draw(batcher);
    }

    private void renderRunning(float runTime, float delta) {
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
        hit.setAlpha(myWorld.cracksAlpha);
        hit.draw(batcher);
        renderEnergyBar();
        AssetLoader.font.draw(batcher, ""+ myWorld.getScore(), width / 2 - AssetLoader.font.getBounds(String.valueOf(myWorld.getScore())).width / 2, height/16);
        pause.draw(batcher);
        if(myWorld.shake){ tick(delta); }
    }


    private void renderDying(float runTime, float delta) {
        drawBackground(true);
        batcher.enableBlending();
        batcher.draw(shipAnimation.getKeyFrame(runTime), ship.getX(), ship.getY(), ship.getWidth() / 2.0f, ship.getHeight() / 2.0f, ship.getWidth(), ship.getHeight(), 1, 1, ship.getRotation());
        batcher.draw(explosionAnimation.getKeyFrame(myWorld.dyingTime), ship.getX()- ship.getWidth() / 4, ship.getY(), ship.getWidth() * 1.5f, ship.getHeight()*1.5f);
        hit.setAlpha(myWorld.cracksAlpha);
        hit.draw(batcher);
        AssetLoader.font.draw(batcher, "" + myWorld.getScore(), width / 2 - AssetLoader.font.getBounds(String.valueOf(myWorld.getScore())).width / 2, height / 16);
        if(myWorld.shake){ tick(delta); }
    }

    private void renderGameOver() {
        drawBackground(true);
        scoreTable.draw(batcher);
        String score = myWorld.getScore() + "";
        AssetLoader.font.draw(batcher, ""+ myWorld.getScore(),width / 2 - AssetLoader.font.getBounds(score).width / 2, height/2 + height / 40 * 9);
    }

    private void renderHiScore() {
        drawBackground(true);
        scoreTable.draw(batcher);
        String score = myWorld.getScore() + "";
        AssetLoader.font.draw(batcher, ""+ myWorld.getScore(),width / 2 - AssetLoader.font.getBounds(score).width / 2, height/2 + height / 40 * 9);
    }
    private void tick(float delta){
        if(current_time <= time) {
            current_power = power * ((time - current_time) / time);
            if(rotate>=0) {
                rotate = (random.nextFloat() - 1f) * 2 * current_power;
            }else{
                rotate = random.nextFloat() * 2 * current_power;
            }
            rotation += rotate;
            cam.rotate(-rotate);
            current_time += delta;
        } else {
            cam.rotate(rotation);
            rotation = 0;
            myWorld.shake = false;
            current_power = 0;
            current_time = 0;
        }
    }

}