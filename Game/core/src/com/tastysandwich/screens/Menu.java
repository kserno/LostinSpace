package com.tastysandwich.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
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

    private Music music;

    private AssetManager manager;

    public Menu(final float width, final float height, final MainClass game, final AdsController adsController, final AssetManager manager) {
        this.manager = manager;
        this.width = width;
        this.height = height;
        if(adsController.isInternetConnected()) adsController.loadAd();
        adsController.hideBannerAd();
        cam = new OrthographicCamera();
        cam.setToOrtho(true, width, height);

        playSounds = AssetLoader.getSounds();
        music = manager.get("data/audio/background_music.mp3", Music.class);
        music.setLooping(true);
        if(playSounds){
            music.play();
        }

        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);

        score = String.valueOf(AssetLoader.getHighScore());

        scoreWidth = AssetLoader.font.getBounds(score).width;
        scoreHeight = AssetLoader.font.getBounds(score).height;

        stage = new Stage(new ScreenViewport(cam));
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        menuBackground = AssetLoader.sMenuBackground;
        imgbPlay = AssetLoader.sdPlay;
        imgbHangar = AssetLoader.sdHangar;
        imgbSoundsT = AssetLoader.sdSoundsT;
        imgbSoundsF = AssetLoader.sdSoundsF;

        Play = new ImageButton(imgbPlay);
        Play.setPosition(width / 2 - width / 16 * 5 / 2, height / 6);
        Play.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                adsController.hideBannerAd();
                game.setScreen(new GameScreen(width, height, adsController, game, manager));
                return true;
            }});

        Hangar = new ImageButton(imgbHangar);
        Hangar.setPosition(width / 2 - width / 16 * 5 / 2,height / 12 * 5);
        Hangar.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new HangarScreen(width,height, game, adsController, manager));
                return true;
            }
        });
        if(playSounds){
            Sounds = new ImageButton(imgbSoundsT);
        }else {
            Sounds = new ImageButton(imgbSoundsF);
        }
        Sounds.setPosition(width - width / 11, width / 11 - width / 14);
        Sounds.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if(playSounds) {
                    playSounds = false;
                    AssetLoader.setSounds(playSounds);
                    music.stop();
                    Sounds.setStyle(new ImageButton.ImageButtonStyle(imgbSoundsF, imgbSoundsF, imgbSoundsF, imgbSoundsF, imgbSoundsF, imgbSoundsF));
                }else {
                    playSounds = true;
                    AssetLoader.setSounds(playSounds);
                    music.play();
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
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
