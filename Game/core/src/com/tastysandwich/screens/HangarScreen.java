package com.tastysandwich.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tastysandwich.helpers.AssetLoader;
import com.tastysandwich.game.MainClass;
import com.tastysandwich.screens.Menu;

import static com.badlogic.gdx.scenes.scene2d.ui.ImageButton.*;

/**
 * Created by Filip on 23.2.2015.
 */
public class HangarScreen implements Screen {

    private float width,height;

    private Stage stage;
    private OrthographicCamera cam;
    private SpriteBatch batcher;

    private SpriteDrawable imgbMenu;

    private Sprite menuBackground;

    private ImageButton menu;

    private ImageButton left, right;

    private ImageButton ship;

    private SpriteDrawable[] hangarShips;

    private ShapeRenderer renderer;

    private int nship;

    public HangarScreen(final float width, final float height, final MainClass game) {
        this.width = width;
        this.height = height;
        cam = new OrthographicCamera();
        cam.setToOrtho(true, width, height);

        renderer = new ShapeRenderer();

        batcher = new SpriteBatch();
        // Attach batcher to camera
        batcher.setProjectionMatrix(cam.combined);

        nship = AssetLoader.getSelectedShip();

        stage = new Stage(new ScreenViewport(cam));
        Gdx.input.setInputProcessor(stage); //** stage is responsive **//

        stage.clear();

        menuBackground = AssetLoader.sMenuBackground;

        imgbMenu = AssetLoader.sdMenu;

        hangarShips = AssetLoader.hangarShips;

        ship = new ImageButton(hangarShips[nship]);

        menu = new ImageButton(imgbMenu); //** Button text and style **//
        menu.setPosition(width / 2 - width / 5 / 2, height / 3); //** Button location **//
        menu.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new Menu(width, height, game ));
                return true;
            }});
        stage.addActor(menu);

        left = new ImageButton(imgbMenu);
        left.setPosition(0, height/2); //** Button location **//
        left.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if (nship== 0) nship =2; else nship--;
                ship.setStyle(new ImageButtonStyle(hangarShips[nship], hangarShips[nship], hangarShips[nship], hangarShips[nship], hangarShips[nship], hangarShips[nship]));
                return true;
            }});
        stage.addActor(left);
        right = new ImageButton(imgbMenu);
        right.setPosition(width- width/5, height/2); //** Button location **//
        right.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if (nship== 2) nship =0; else nship++;
                ship.setStyle(new ImageButtonStyle(hangarShips[nship],hangarShips[nship],hangarShips[nship],hangarShips[nship],hangarShips[nship],hangarShips[nship] ));
                return true;
            }});
        stage.addActor(right);

        ship = new ImageButton(hangarShips[nship]);
        ship.setPosition(width / 2 - width / 5 / 2, height / 2);
        ship.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (nship == 2 && AssetLoader.getTotalEnergy() < 500) {
                   // do nothing
                } else {
                    AssetLoader.setSelectedShip(nship);
                    AssetLoader.loadShip(nship);
                }
                return true;
            }
        });
        stage.addActor(ship);






    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batcher.begin();
        menuBackground.draw(batcher);
        menu.draw(batcher, 50f);
        left.draw(batcher, 50f);
        right.draw(batcher, 50f);

        ship.draw(batcher, 50f);
        batcher.end();
        if (AssetLoader.getSelectedShip()== nship) {
            renderer.begin(ShapeRenderer.ShapeType.Line);
            renderer.setColor(Color.GREEN);
            renderer.rect(ship.getX(), ship.getY()-ship.getHeight(), ship.getWidth(), ship.getHeight());
            renderer.end();
        }


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
}
