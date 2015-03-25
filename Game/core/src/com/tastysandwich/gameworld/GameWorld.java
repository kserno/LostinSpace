package com.tastysandwich.gameworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
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
    private Random r;
    public Energy energy;

    public boolean eIsActive = false;

    public float score;

    public int nAsteroids = -1;
    public Asteroid asteroids[];
    private float spawnAsteroid = 0;
    private float restartAsteroid = 0;

    public GameState currentState;

    private float gameSpeed = 1;

    private ImageButton ibTryAgain;
    private Rectangle tryAgainRect;

    public void start() {
        Gdx.app.log("GameState", "RUNNING");
        currentState = GameState.RUNNING;

    }

    public enum GameState {

        READY, RUNNING, GAMEOVER, HISCORE, PAUSE
    }


    public GameWorld(float width, float height) {
        currentState = GameState.READY;
        Gdx.app.log("GameState", "READY");
        asteroids = new Asteroid[9];
        this.width = width;
        this.height = height;
        ship = new Ship(width / 12, height / 3, width / 6, width / 6 / 3 * 2, this, height);
        r = new Random();
        ibTryAgain = new ImageButton(AssetLoader.sdTryAgain);
        ibTryAgain.setPosition(width / 2 - width / 5 / 2, height / 2);
        tryAgainRect = new Rectangle(width / 2 - width / 5 / 2, height / 2, width / 5, height / 20 * 3);
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
                updateGameOver(delta);
            case HISCORE:
                updateHiScore();
                break;
            case PAUSE:
                updatePause();
                break;
        }

    }

    private void updatePause() {
        // do nothing
    }

    private void updateHiScore() {
    }

    private void updateGameOver(float delta) {

    }

    private void updateReady(float delta) {
        ship.update(delta);
    }

    private void updateRunning(float delta, float runTime) {
        ship.update(delta);
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
        if (spawnAsteroid > 7 && nAsteroids < 8) {
            asteroids[nAsteroids + 1] = new Asteroid(width, (float) r.nextInt((int) height), r.nextInt(200) - 100, this);
            nAsteroids++;
            spawnAsteroid = 0;
            Gdx.app.log("Asteroid", String.valueOf(nAsteroids));
        }
        if (ship.getIsAlive()) {
            score += delta * gameSpeed;
        }
        if ((int) (runTime) % 10 == 0 && !eIsActive) {
            energy = new Energy(width, (float) r.nextInt((int) height), width/21.33f);
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
            Gdx.app.log("totale", String.valueOf(AssetLoader.getTotalEnergy()));
            energy = null;
            eIsActive = false;
        }
        for (int i = 0; i <= nAsteroids; i++) {
            if (asteroids[i].collides(ship)) {
                Gdx.app.log("Ship", "Collided!");
                ship.collide();
                asteroids[i].restart();
            }
        }
        if (!ship.getIsAlive()) {

            if (AssetLoader.getHighScore() < score) {
                currentState = GameState.HISCORE;
                AssetLoader.setHighScore((int) score);
                Gdx.app.log("GameState ", "HISCORE");
            } else {
                currentState = GameState.GAMEOVER;
                Gdx.app.log("GameState", "GAMEOVER");
            }
        }

        for (int i = 0; i <= nAsteroids - 1; i++) {
            for (int i2 = i + 1; i2 <= nAsteroids; i2++) {
                if (collidesA(asteroids[i].getBoundingPolygon(), asteroids[i2].getBoundingPolygon())) {
                    Gdx.app.log("Asteroid", "Collided");
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
        ship.restart();
        energy = null;
        eIsActive = false;
        gameSpeed = 1;
        asteroids = null;
        asteroids = new Asteroid[9];
        nAsteroids = -1;
        spawnAsteroid = 0;
        score = 0;
        currentState = GameWorld.GameState.READY;
    }

    public void restart() {
        ship.restart();
    }

    public boolean geteIsActive() {
        return eIsActive;
    }

    public Ship getShip() {
        return ship;
    }

    public Asteroid[] getAsteroids() {
        return asteroids;
    }

    public ImageButton getIbTryAgain() {
        return ibTryAgain;
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
}
