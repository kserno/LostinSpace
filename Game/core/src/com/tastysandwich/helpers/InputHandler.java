package com.tastysandwich.helpers;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.tastysandwich.game.MainClass;
import com.tastysandwich.gameobjects.Ship;
import com.tastysandwich.gameworld.GameWorld;
import com.tastysandwich.screens.Menu;

/**
 * Created by solit_000 on 7.2.2015.
 */
public class InputHandler implements InputProcessor {
    private Ship ship;
    private GameWorld world;
    private MainClass game;
    private AssetManager manager;
    private boolean clicking;

    public InputHandler(Ship ship, GameWorld world, MainClass game, AssetManager manager, boolean clicking) {
        this.world = world;
        this.ship = ship;
        this.game = game;
        this.manager = manager;
        this.clicking = clicking;
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
            if (world.getMenuRect().contains(screenX, screenY)) {
                world.getAdsController().hideBannerAd();
                game.setScreen(new Menu(world.width, world.height,game, world.getAdsController(), manager));
            }
        }
        if (world.getCurrentState() == GameWorld.GameState.RUNNING && world.getPauseButton().contains((float) screenX, (float) screenY)){
            world.Pause();
            if(world.getAdsController().isInternetConnected()) {world.getAdsController().showBannerAd();}
        }
        if (world.getCurrentState() == GameWorld.GameState.RUNNING && clicking) {
            ship.onClick(screenX, screenY);
        }
        if (world.getCurrentState() == GameWorld.GameState.PAUSE) {
            if (world.getMusicPlay()) {
                world.turnOffMusic();
                AssetLoader.setSounds(false);
            } else {
                world.turnOnMusic();
                AssetLoader.setSounds(true);
            }
        }


        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (world.getCurrentState() == GameWorld.GameState.RUNNING && clicking == false) {
            ship.onDrag(screenX, screenY);
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
