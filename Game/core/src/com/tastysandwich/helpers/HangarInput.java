package com.tastysandwich.helpers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.tastysandwich.screens.HangarScreen;

/**
 * Created by solit_000 on 9.4.2015.
 */
public class HangarInput implements InputProcessor {

    private HangarScreen screen;

    public HangarInput(HangarScreen screen){
        this.screen = screen;

    }
    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.BACK){
            screen.setScreen();
        }
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
        if(screen.getMenuButton().contains(screenX, screen.height - screenY)){
            screen.setScreen();
        }else if(screen.getLeftArrow().contains(screenX, screen.height - screenY)){
            screen.moveLeft();
        }else if(screen.getRightArrow().contains(screenX, screen.height - screenY)){
            screen.moveRight();
        }else if(screen.getShipRect().contains(screenX, screenY)){
            screen.changeShip();
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
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
