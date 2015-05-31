package com.tastysandwich.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.tastysandwich.game.PostHiScore;
import com.tastysandwich.game.RequestHiScore;
import com.tastysandwich.game.RequestUsername;
import com.tastysandwich.game.UserScore;
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
    private ImageButton Dragging, Clicking;
    private ImageButton LeaderBoards;
    private ImageButton LeaderBoardsBackground;
    private ImageButton ChangeName;

    private boolean playSounds;

    private SpriteDrawable imgbPlay,imgbHangar, imgbSoundsT, imgbSoundsF, imgbClick, imgbDrag, imgbLeaderBoards, imgbLeaderBoardsBackground, imgbChangeName;

    private Sprite menuBackground;

    private String score;
    private float scoreWidth, scoreHeight;

    private Music music;

    private AssetManager manager;

    private AdsController adsController;

    private PostHiScore p;
    private RequestHiScore r;
    private RequestUsername u;

    private boolean play;

    private boolean leaderBoards;

    private int loadingLB;

    private BitmapFont font;

    private UserScore[] userScores;
    public Menu(final float width, final float height, final MainClass game, final AdsController adsController, final AssetManager manager, final PostHiScore p, final RequestHiScore r, final RequestUsername u) {
        this.manager = manager;
        this.r=r;
        this.p=p;
        this.u = u;
        this.width = width;
        this.height = height;
        this.adsController = adsController;
        adsController.hideBannerAd();
        cam = new OrthographicCamera();
        cam.setToOrtho(true, width, height);

        play = false;
        leaderBoards = false;
        loadingLB = 0;

        font = AssetLoader.font;

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
        imgbLeaderBoardsBackground = AssetLoader.sdLeaderBoardsBackground;
        imgbPlay = AssetLoader.sdPlay;
        imgbHangar = AssetLoader.sdHangar;
        imgbSoundsT = AssetLoader.sdSoundsT;
        imgbSoundsF = AssetLoader.sdSoundsF;
        imgbClick = AssetLoader.sdClick;
        imgbDrag = AssetLoader.sdDrag;
        imgbLeaderBoards = AssetLoader.sdLeaderBoards;
        imgbChangeName = AssetLoader.sdChangeName;

        Clicking = new ImageButton(imgbClick);
        Clicking.setPosition(width / 2 - width / 16 * 5, height / 24 * 7);
        Clicking.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameScreen(width, height, adsController, game, manager, music, true, p, r,u));
                return true;
            }});
        Dragging = new ImageButton(imgbDrag);
        Dragging.setPosition(width / 2, height / 24 * 7);
        Dragging.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameScreen(width, height, adsController, game, manager, music, false, p, r,u));
                return true;
            }});

        Play = new ImageButton(imgbPlay);
        Play.setPosition(width / 2 - width / 16 * 5 / 2, height / 6);
        Play.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                play=true;
                table.removeActor(Play);
                table.removeActor(Hangar);
                table.removeActor(LeaderBoards);
                table.add(Clicking, Dragging);
                return true;
            }});

        Hangar = new ImageButton(imgbHangar);
        Hangar.setPosition(width / 2 - width / 16 * 5 / 2,height / 6 * 2);
        Hangar.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new HangarScreen(width,height, game, adsController, manager, p, r,u));
                return true;
            }
        });
        ChangeName = new ImageButton(imgbChangeName);
        ChangeName.setPosition(width / 6 / 2,height / 6);
        ChangeName.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                changeName(AssetLoader.getUserName());
                return true;
            }
        });
        LeaderBoards = new ImageButton(imgbLeaderBoards);
        LeaderBoards.setPosition(width / 2 - width / 16 * 5 / 2, height / 6 * 3);
        LeaderBoards.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                loadingLB = 1;
                table.removeActor(Play);
                table.removeActor(Hangar);
                table.removeActor(LeaderBoards);
                table.removeActor(ChangeName);
                table.add(LeaderBoardsBackground);
                return true;
            }});
        LeaderBoardsBackground = new ImageButton(imgbLeaderBoardsBackground);
        LeaderBoardsBackground.setPosition(0, 0);
        LeaderBoardsBackground.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                leaderBoards = false;
                loadingLB = 0;
                table.removeActor(LeaderBoardsBackground);
                table.add(Play, Hangar, LeaderBoards, ChangeName);
                return true;
            }});
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
        table.add(Play, Hangar, LeaderBoards, ChangeName, Sounds);

        if(!AssetLoader.getName()){
            changeName("");
        }
    }

    private void changeName(String name) {
        Input.TextInputListener listener = new Input.TextInputListener() {
            @Override
            public void input(String text) {
                if (!AssetLoader.getName()) {
                    if (u.requestUsername(text)) {
                        AssetLoader.setUserName(text);
                        System.out.println("username setted");
                    } else {
                        System.out.println("name already chosen");
                    }
                } else {
                    if (u.requestUsername(text, AssetLoader.getUserName())) {
                        AssetLoader.setUserName(text);
                        System.out.println("username setted last one deleted");
                    } else {
                        System.out.println("name already chosen");
                    }
                }
            }

            @Override
            public void canceled() {

            }
        };
        Gdx.input.getTextInput(listener, "Enter your name", name, "");
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
        Sounds.draw(batcher, 50f);
        font.draw(batcher,score , width/2 - scoreWidth/2, height/20 * 15);
        if(play) {
            Clicking.draw(batcher, 50f);
            Dragging.draw(batcher, 50f);
        }else {
            Play.draw(batcher, 50f);
            Hangar.draw(batcher, 50f);
            LeaderBoards.draw(batcher, 50f);
            ChangeName.draw(batcher, 50f);
            if (loadingLB != 0) {
                switch (loadingLB) {
                    case 1: {
                        font.draw(batcher, "LOADING", width / 2 - font.getBounds("LOADING").width / 2, height / 2 + font.getBounds("LOADING").height / 2);
                        loadingLB = 2;
                        break;
                    }
                    case 2: {
                        if (adsController.isInternetConnected())
                            userScores = r.getUserScores();
                        else userScores = null;
                        leaderBoards = true;
                        loadingLB = 0;
                        break;
                    }
                }
            } else if (leaderBoards) {
                LeaderBoardsBackground.draw(batcher, 50f);
                if (userScores != null) {
                    for (int i = 0; i < userScores.length; i++) {
                        font.draw(batcher, (i + 1) + ".", width / 8, (height - height / 5) / 10 * (i+1));
                        font.draw(batcher, "" + userScores[i].getScore(), width / 8 * 2, (height - height / 5) / 10 * (i+1));
                        font.draw(batcher, userScores[i].getUser(), width / 8 * 4, (height - height / 5) / 10 * (i+1));
                    }
                } else {
                    font.draw(batcher, "Sorry, aber du hast keine Internet", 0, height / 2);
                }
            }
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
}
