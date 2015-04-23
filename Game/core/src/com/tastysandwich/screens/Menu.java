package com.tastysandwich.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tastysandwich.game.AdsController;
import com.tastysandwich.game.MainClass;
import com.tastysandwich.helpers.AssetLoader;

/**
 * Created by Filip on 16.2.2015.
 */
public class Menu implements Screen {

    final private float height, width;

    private OrthographicCamera cam;
    private Stage stage;
    private SpriteBatch batcher;

    private Table table = new Table();

    private ImageButton Play;
    private ImageButton Hangar;
    private ImageButton Sounds;

    private boolean playSounds;

    private SpriteDrawable imgbPlay,imgbHangar, imgbSoundsT, imgbSoundsF;

    private Sprite menuBackground;

    private String score;
    private float scoreWidth, scoreHeight;

    public Menu(final float width, final float height, final MainClass game, final AdsController adsController) {
        this.width = width;
        this.height = height;
        if(adsController.isInternetConnected()) adsController.loadAd();
        adsController.hideBannerAd();
        cam = new OrthographicCamera();
        cam.setToOrtho(true, width, height);

        playSounds = AssetLoader.getSounds();

        if(playSounds){
            AssetLoader.music.play();
        }

        batcher = new SpriteBatch();
        // Attach batcher to camera
        batcher.setProjectionMatrix(cam.combined);

        score = String.valueOf(AssetLoader.getHighScore());

        scoreWidth = AssetLoader.font.getBounds(score).width;
        scoreHeight = AssetLoader.font.getBounds(score).height;

        stage = new Stage(new ScreenViewport(cam));
        Gdx.input.setInputProcessor(stage); //** stage is responsive **//
        stage.clear();

        menuBackground = AssetLoader.sMenuBackground;
        imgbPlay = AssetLoader.sdPlay;
        imgbHangar = AssetLoader.sdHangar;
        imgbSoundsT = AssetLoader.sdSoundsT;
        imgbSoundsF = AssetLoader.sdSoundsF;

        Play = new ImageButton(imgbPlay); //** Button text and style **//
        Play.setPosition(width / 2 - width / 16 * 5 / 2, height / 6); //** Button location **//
        Play.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                adsController.hideBannerAd();
                game.setScreen(new GameScreen(width, height, adsController,game));//** Usually used to start Game, etc. **//
                return true;
            }});

        Hangar = new ImageButton(imgbHangar);
        Hangar.setPosition(width / 2 - width / 16 * 5 / 2,height / 12 * 5);
        Hangar.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new HangarScreen(width,height, game, adsController));
                return true;
            }
        });
        if(playSounds){
            Sounds = new ImageButton(imgbSoundsT);
        }else {
            Sounds = new ImageButton(imgbSoundsF);
        }
        Sounds.setPosition(width - width / 11, width / 11 - width / 14); //** Button location **//
        Sounds.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(playSounds) {
                    playSounds = false;
                    AssetLoader.setSounds(playSounds);
                    AssetLoader.music.stop();
                    Sounds.setStyle(new ImageButton.ImageButtonStyle(imgbSoundsF, imgbSoundsF, imgbSoundsF, imgbSoundsF, imgbSoundsF, imgbSoundsF));
                }else {
                    playSounds = true;
                    AssetLoader.setSounds(playSounds);
                    AssetLoader.music.play();
                    Sounds.setStyle(new ImageButton.ImageButtonStyle(imgbSoundsT, imgbSoundsT, imgbSoundsT, imgbSoundsT, imgbSoundsT, imgbSoundsT));
                }
                return true;
            }});
        stage.addActor(table);
        table.add(Play);
        table.add(Hangar);
        table.add(Sounds);
    }

    

    @Override
    public void  show() {




    }

    @Override
    public void render(float delta) {
        batcher.begin();
        menuBackground.draw(batcher);
        batcher.enableBlending();
        Play.draw(batcher, 50f);
        Hangar.draw(batcher, 50f);
        Sounds.draw(batcher, 50f);
        AssetLoader.font.draw(batcher,score , width/2 - scoreWidth/2, height/20 * 15);
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
}
