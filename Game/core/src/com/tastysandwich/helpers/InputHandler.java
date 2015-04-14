package com.tastysandwich.helpers;

import com.badlogic.gdx.InputProcessor;
import com.tastysandwich.gameobjects.Ship;
import com.tastysandwich.gameworld.GameWorld;

/**
 * Created by solit_000 on 7.2.2015.
 */
public class InputHandler implements InputProcessor {
    private Ship ship;
    private GameWorld world;

    public InputHandler(Ship ship, GameWorld world) {
        this.world = world;
        this.ship = ship;
    }
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (world.getCurrentState() == GameWorld.GameState.READY) {
            world.start();
        }
        if (world.getCurrentState() == GameWorld.GameState.PAUSE) {
            world.resume();
        }
        if (world.getCurrentState() == GameWorld.GameState.GAMEOVER || world.getCurrentState() == GameWorld.GameState.HISCORE) {
            if(world.getTryAgainRect().contains(screenX, screenY)){
                world.tryAgain();
            }
        }
        if (world.getCurrentState() == GameWorld.GameState.RUNNING && world.getPauseButton().contains((float) screenX, (float) screenY)) world.Pause();

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (world.getCurrentState() == GameWorld.GameState.RUNNING || world.getCurrentState() == GameWorld.GameState.DYING) {
            ship.onClick(screenX, screenY);
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
