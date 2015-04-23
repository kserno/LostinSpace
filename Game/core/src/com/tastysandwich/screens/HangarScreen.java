package com.tastysandwich.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tastysandwich.game.AdsController;
import com.tastysandwich.helpers.AssetLoader;
import com.tastysandwich.game.MainClass;
import com.tastysandwich.helpers.HangarInput;
import com.tastysandwich.screens.Menu;

import static com.badlogic.gdx.scenes.scene2d.ui.ImageButton.*;

/**
 * Created by Filip on 23.2.2015.
 */
public class HangarScreen implements Screen {

    public float width,height;

    private OrthographicCamera cam;
    private SpriteBatch batcher;

    private MainClass game;

    private Sprite hangarBackground;
    private Sprite[] hangarShips;

    private int nship;

    private Rectangle menuButton, leftArrow, rightArrow;

    private AdsController adsController;

    public HangarScreen(final float width, final float height, final MainClass game, AdsController adsController) {
        this.width = width;
        this.height = height;
        this.game = game;
        this.adsController = adsController;
        if(adsController.isInternetConnected()) {adsController.showBannerAd();}
        cam = new OrthographicCamera();
        cam.setToOrtho(true, width, height);

        menuButton = new Rectangle(width / 24 * 9 , height / 16 * 2, width / 12 * 3, height / 6);
        leftArrow = new Rectangle(width / 20, height / 30 * 13, width / 15, width / 15);
        rightArrow = new Rectangle(width - width / 8.75f, height / 30 * 13, width / 15, width / 15);

        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);

        nship = AssetLoader.getSelectedShip();
        hangarBackground = AssetLoader.hangarBackground;
        hangarShips = AssetLoader.hangarShips;

        Gdx.input.setInputProcessor(new HangarInput(this));
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batcher.begin();
        hangarBackground.draw(batcher);
        if (AssetLoader.getSelectedShip()== nship) {
            hangarShips[nship+3].draw(batcher);
        }else {
            hangarShips[nship].draw(batcher);
        }
        batcher.end();

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

    public Rectangle getMenuButton() { return menuButton; }

    public Rectangle getLeftArrow() { return leftArrow; }

    public Rectangle getRightArrow() { return rightArrow; }

    public Rectangle getShipRect() { return hangarShips[nship].getBoundingRectangle(); }

    public void setScreen() {
        game.setScreen(new Menu(width, height, game, adsController));
    }

    public void moveLeft() {
        if (nship== 2) nship =0; else nship++;
    }

    public void moveRight() {
        if (nship== 0) nship =2; else nship--;
    }

    public void changeShip() {
        if (nship == 2 && AssetLoader.getTotalEnergy() < 500) {
            // do nothing
        } else {
            AssetLoader.setSelectedShip(nship);
            AssetLoader.loadShip(nship);
        }
    }
}
