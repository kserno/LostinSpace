package com.tastysandwich.gameworld;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.tastysandwich.game.AdsController;
import com.tastysandwich.game.PostHiScore;
import com.tastysandwich.game.RequestHiScore;
import com.tastysandwich.game.UserScore;
import com.tastysandwich.gameobjects.Asteroid;
import com.tastysandwich.gameobjects.Ship;
import com.tastysandwich.gameobjects.Energy;
import com.tastysandwich.helpers.AssetLoader;

import java.util.Random;

/**
 * Created by solit_000 on 5.2.2015.
 */
public class GameWorld {
    private Ship ship;
    public float width, height;
    private Random random;
    public Energy energy;

    public boolean eIsActive = false;

    public float score;
    public float dyingTime = 0;

    public int nAsteroids = -1;
    public Asteroid asteroids[];
    private float spawnAsteroid = 0;
    private float restartAsteroid = 0;

    public GameState currentState;
    private float gameSpeed = 1;

    private Rectangle tryAgainRect;
    private Rectangle pauseButton;
    private Rectangle menuRect;
    private Rectangle soundsRect;

    private AdsController adsController;

    private AssetManager manager;
    private Sound explosion;
    public boolean shake = false;
    private Music music;
    private Boolean musicPlay;

    public float cracksAlpha;

    private PostHiScore p;
    private RequestHiScore r;




    public void start() {
        currentState = GameState.RUNNING;
    }

    public void turnOnMusic() {
        music.play();
        musicPlay = true;
    }

    public void turnOffMusic() {
        music.stop();
        musicPlay = false;
    }

    public enum GameState {

        READY, RUNNING, GAMEOVER, HISCORE, PAUSE, DYING
    }

    public GameWorld(float width, float height, AdsController adsController, AssetManager manager, Music music, boolean clicking, PostHiScore p, RequestHiScore r) {
        this.music = music;
        currentState = GameState.READY;
        this.manager = manager;
        cracksAlpha = 0f;
        explosion = manager.get("data/audio/explosion.mp3", Sound.class);
        asteroids = new Asteroid[9];
        this.width = width;
        this.height = height;
        this.adsController = adsController;

        this.p = p;
        this.r = r;

        ship = new Ship(width / 12, height / 2 - width / 6 / 3, width / 6, width / 6 / 3 * 2, this, height, clicking);
        random = new Random();
        pauseButton = new Rectangle(width - width / 10, height/20, width/10, height/10 + height/20);
        tryAgainRect = new Rectangle(width / 3*2 -width/5/2 , height / 2, width / 5, height / 20 * 3);
        menuRect = new Rectangle(width / 3 - width/5/2, height/2, width/5, height/20*3);
        soundsRect = new Rectangle(width - width/11, AssetLoader.sSoundsF.getWidth() ,width/11 - width/14, AssetLoader.sSoundsF.getHeight());
        musicPlay = AssetLoader.getSounds();

    }

    public void update(float delta, float runTime) {

        switch (currentState) {
            case READY:
                updateReady(delta);
                break;
            case RUNNING:
                updateRunning(delta, runTime);
                break;
            case GAMEOVER:
                updateGameOver();
            case HISCORE:
                updateHiScore();
                break;
            case PAUSE:
                updatePause();
                break;
            case DYING:
                updateDying(delta);
                break;
        }

    }

    private void updateDying(float delta) {
        dyingTime += delta;
        ship.update(delta);
        if(cracksAlpha>0f){
            cracksAlpha -= delta / 2;
        }
        if(cracksAlpha<0.1f) {
            cracksAlpha = 0f;
        }
        if(dyingTime > 1) {

            if(adsController.isInternetConnected()) {adsController.showBannerAd();}
            if (AssetLoader.getHighScore() < score) {

                if(adsController.isInternetConnected() && AssetLoader.getName()){
                    p.post(new UserScore(AssetLoader.getUserName(), (int) score));
                }
                currentState = GameState.HISCORE;
                AssetLoader.setHighScore((int) score);
            } else {
                currentState = GameState.GAMEOVER;
            }
        }
    }

    private void updatePause() {
    }

    private void updateHiScore() {
    }

    private void updateGameOver() {
    }

    private void updateReady(float delta) {
        ship.update(delta);
    }

    private void updateRunning(float delta, float runTime) {
        ship.update(delta);
        if(cracksAlpha>0){
            cracksAlpha -= delta / 2;
        }
        for (int i = 0; i <= nAsteroids; i++) {
            asteroids[i].update(delta);
        }
        restartAsteroid += delta;
        if (restartAsteroid > 0.5) {
            for (int i = 0; i <= nAsteroids; i++) {
                if (asteroids[i].readyToRestart) {
                    asteroids[i].restart();
                    asteroids[i].readyToRestart = false;
                    restartAsteroid = 0;
                    break;
                }
            }
        }
        spawnAsteroid += delta;
        if (gameSpeed < 3) {
            gameSpeed += delta / 64;
        }
        if (spawnAsteroid > 6 && nAsteroids < 8) {
            asteroids[nAsteroids + 1] = new Asteroid(width, (float) random.nextInt((int) height), random.nextInt(200) - 100, this);
            nAsteroids++;
            spawnAsteroid = 0;
        }
        if (ship.getIsAlive()) {
            score += delta * gameSpeed;
        }
        if ((int) (runTime) % 10 == 0 && !eIsActive) {
            energy = new Energy(width, (float) random.nextInt((int) (height-3*width/21.33f))+ width/21.33f, width/21.33f, gameSpeed);
            eIsActive = true;

        }
        if (eIsActive) {
            energy.update(delta);
            if (energy.getX() + energy.radius <= 0) {
                energy = null;
                eIsActive = false;
            }
        }
        if (eIsActive && energy.collides(ship)) {
            ship.addEnergy();
            AssetLoader.setTotalEnergy(AssetLoader.getTotalEnergy() + 1);
            energy = null;
            eIsActive = false;
        }
        for (int i = 0; i <= nAsteroids; i++) {
            if (asteroids[i].collides(ship)) {
                cracksAlpha = 1f;
                shake = true;
                ship.collide();
                asteroids[i].restart();
            }
        }
        if (!ship.getIsAlive()) {
            if(AssetLoader.getSounds()) {
                explosion.play();
            }
            currentState = GameState.DYING;
        }

        for (int i = 0; i <= nAsteroids - 1; i++) {
            for (int i2 = i + 1; i2 <= nAsteroids; i2++) {
                if (collidesA(asteroids[i].getBoundingPolygon(), asteroids[i2].getBoundingPolygon())) {
                    if (asteroids[i].getX() > asteroids[i2].getX()) {
                        float stolenSpeed = asteroids[i].velocity.x / 16;
                        asteroids[i].velocity.x = stolenSpeed * 15;
                        asteroids[i2].velocity.x += stolenSpeed;
                    } else {
                        float stolenSpeed = asteroids[i2].velocity.x / 16;
                        asteroids[i2].velocity.x = stolenSpeed * 15;
                        asteroids[i].velocity.x += stolenSpeed;
                    }
                    float ySpeed = asteroids[i].velocity.y;
                    asteroids[i].velocity.y = asteroids[i2].velocity.y;
                    asteroids[i2].velocity.y = ySpeed;


                }
            }
        }
    }

    public void tryAgain() {
        adsController.hideBannerAd();
        ship.restart();
        energy = null;
        eIsActive = false;
        gameSpeed = 1;
        asteroids = null;
        asteroids = new Asteroid[9];
        nAsteroids = -1;
        spawnAsteroid = 0;
        score = 0;
        dyingTime = 0;
        cracksAlpha = 0f;
        shake = false;
        currentState = GameWorld.GameState.READY;
    }

    public boolean geteIsActive() {
        return eIsActive;
    }

    public Ship getShip() {
        return ship;
    }

    public void Pause() {
        currentState = GameState.PAUSE;
        if (music.isPlaying()) music.pause();
    }

    public Asteroid[] getAsteroids() {
        return asteroids;
    }

    public Rectangle getTryAgainRect() {
        return tryAgainRect;
    }

    public Energy getEnergy() {
        return energy;
    }

    public int getScore() {
        return (int) score;

    }

    public boolean collidesA(Polygon polygon1, Polygon polygon2) {
        return Intersector.overlapConvexPolygons(polygon1, polygon2);
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public float getGameSpeed() {
        return gameSpeed;
    }

    public Rectangle getPauseButton() {
        return pauseButton;
    }

    public Rectangle getMenuRect() {
        return menuRect;
    }

    public void resume() {
        adsController.hideBannerAd();
        currentState = GameState.RUNNING;
        if (AssetLoader.getSounds()) music.play();
    }

    public Rectangle getSoundsRect() {
        return soundsRect;
    }

    public AdsController getAdsController() {
        return  adsController;
    }

    public Boolean getMusicPlay() {
        return musicPlay;
    }

}
