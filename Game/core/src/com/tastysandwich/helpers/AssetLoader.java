package com.tastysandwich.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * Created by solit_000 on 6.2.2015.
 */
public class AssetLoader {

    /////////////////////////////////////GAMESCREEN ASSETS////////////////////////////////////////////
    public static Sprite background,stars1,stars2;
    public static Texture[] asteroids;

    public static Animation shipAnimation;
    public static TextureRegion ship1, ship2, ship3;

    //Energia
    private static final int        FRAME_COLS = 37;         // #1
    private static final int        FRAME_ROWS = 2;         // #2

    public static Animation                       energyAnimation;          // #3
    public static Texture                         energySheet;              // #4
    public static TextureRegion[]                 energyFrames;

    public static Texture scorebg, highscorebg;
    public static TextureRegion tscorebg, thighscorebg;

    private static Preferences prefs;

    public static BitmapFont font;

    public static Texture[] energyBar;

    public static Sprite pause;

    /////////////////////////////////////MENU ASSETS////////////////////////////////////////////
    public static Sprite sMenuBackground;

    public static SpriteDrawable sdPlay,sdHangar,sdTryAgain, sdMenu, sdSoundsT, sdSoundsF;



    /////////////////////////////////////HANGAR ASSETS////////////////////////////////////////////

    public static Sprite hangarShip;

    public static SpriteDrawable[] hangarShips;





    public static void load(int width, int height) {
        // HIGHSCORE
        prefs = Gdx.app.getPreferences("LostInSpace");
        if (!prefs.contains("HighScore")) {
            prefs.putInteger("HighScore", 0);
        }
        if (!prefs.contains("nShip")) {
            prefs.putInteger("nShip", 0);
        }
        if (!prefs.contains("TotalEnergy")) {
            prefs.putInteger("TotalEnergy", 0);
        }
        loadFont(width);
        loadButtons(width, height);
        loadGameAssets(width, height);
        loadBackgrounds(width, height);
    }

    private static void loadFont(int width) {
        FileHandle exoFile = Gdx.files.internal("data/font/Raleway-SemiBold.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(exoFile);
        font = generator.generateFont((width/18));
        generator.dispose();
        font.setScale(1f, -1f);
    }

    private static void loadBackgrounds(int width, int height) {
        Texture texture = new Texture(Gdx.files.internal("data/loadingscreenhighscore.jpg"));
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        sMenuBackground = new Sprite(texture);
        sMenuBackground.setSize(width, height);
        sMenuBackground.flip(false,true);
        sMenuBackground.setY(0);
        sMenuBackground.setX(0);

    }


    private static void loadButtons(int width, int height) {
        // BUTTON PLAY
        Texture texture = new Texture("data/skins/play.png");
        Sprite sprite = new Sprite(texture);
        sprite.setSize(width / 4, height / 20 * 3);
        sprite.flip(false, true);
        sdPlay = new SpriteDrawable(sprite);

        // BUTTON HANGAR
        texture = new Texture("data/skins/hangar.png");
        sprite = new Sprite(texture);
        sprite.setSize(width / 4, height / 20 * 3);
        sprite.flip(false,true);
        sdHangar = new SpriteDrawable(sprite);

        //BUTTON MENU
        texture = new Texture("data/skins/menu.png");
        sprite = new Sprite(texture);
        sprite.setSize(width / 5, height / 20 * 3);
        sprite.flip(false,true);
        sdMenu = new SpriteDrawable(sprite);

        //BUTTON TRY AGAIN
        texture = new Texture("data/skins/try-again.png");
        sprite = new Sprite(texture);
        sprite.setSize(width / 5, height / 20 * 3);
        sprite.flip(false,true);
        sdTryAgain = new SpriteDrawable(sprite);

        //BUTTON SOUNDS TRUE
        texture = new Texture("data/soundsT.png");
        sprite = new Sprite(texture);
        sprite.setSize(width/14, width/14);
        sprite.flip(false,true);
        sdSoundsT = new SpriteDrawable(sprite);

        //BUTTON SOUNDS FALSE
        texture = new Texture("data/soundsF.png");
        sprite = new Sprite(texture);
        sprite.setSize(width/12, width/12);
        sprite.flip(false,true);
        sdSoundsF = new SpriteDrawable(sprite);



    }

    private static void loadGameAssets(int width, int height) {
        loadShip(getSelectedShip());
        loadGameBackground(width, height);
        loadAsteroids();
        loadEnergy();
        loadEnergyBar(width, height);
        loadGOTables();
        loadHangarShips();
        loadPause(width, height);
    }

    private static void loadHangarShips() {
        hangarShips = new SpriteDrawable[3];
        for(int i=0; i<3; i++) {
            if (i ==2 && getTotalEnergy() < 500) {
                hangarShip = new Sprite(new Texture(Gdx.files.internal("data/hangarships/lockedship2.png")));
                hangarShip.setSize(200f, 130f);
                hangarShip.flip(false, true);
            } else {
                hangarShip = new Sprite(new Texture(Gdx.files.internal("data/hangarships/" + (i+ 1) + ".png")));
                hangarShip.setSize(200f, 130f);
                hangarShip.flip(false, true);
            }
            hangarShips[i] = new SpriteDrawable(hangarShip);
        }
    }

    private static void loadEnergyBar(int width, int height) {
        energyBar = new Texture[11];
        for (int i=0; i<=10; i++){
            energyBar[i] = new Texture(Gdx.files.internal("data/energybar/" + i + ".png"));
        }
    }

    private static void loadGOTables() {
        // BACKGROUND GAMEOVER
        scorebg = new Texture(Gdx.files.internal("data/skins/score.png"));
        tscorebg = new TextureRegion(scorebg);
        tscorebg.flip(false, true);


        // BACKGROUND HISCORE
        highscorebg = new Texture(Gdx.files.internal("data/skins/newhighscore.png"));
        thighscorebg = new TextureRegion(highscorebg);
        thighscorebg.flip(false, true);
    }
    private static void loadPause(int width, int height) {
        pause = new Sprite(new Texture(Gdx.files.internal("data/pause.png")));
        pause.setSize(width/10, height/10 + height/20);
    }

    private static void loadEnergy() {
        // ENERGY
        energySheet = new Texture(Gdx.files.internal("data/energy_animation.png")); // #9
        TextureRegion[][] tmp = TextureRegion.split(energySheet, energySheet.getWidth()/FRAME_COLS, energySheet.getHeight()/FRAME_ROWS);              // #10
        energyFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                energyFrames[index++] = tmp[i][j];
            }
        }
        energyAnimation = new Animation(0.027f, energyFrames);
        energyAnimation.setPlayMode(Animation.PlayMode.LOOP);
    }

    private static void loadAsteroids() {
        // ASTEROIDS
        asteroids = new Texture[8];
        for (int i=1; i<=6; i++){
            asteroids[i] = new Texture(Gdx.files.internal("data/asteroids/" + i + ".png"));
        }
    }

    private static void loadGameBackground(int width, int height) {
        //pozadie
        Texture bg = new Texture(Gdx.files.internal("data/universe/universe.png"));
        bg.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        background = new Sprite(bg);
        background.setSize(width, height);
        background.setY(0);

        //hviezdy
        bg = new Texture(Gdx.files.internal("data/universe/stars1.png"));
        bg.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        stars1 = new Sprite(bg);
        stars1.setSize(width, height);
        stars1.setY(0);

        //hviezdy
        bg = new Texture(Gdx.files.internal("data/universe/stars2.png"));
        bg.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        stars2 = new Sprite(bg);
        stars2.setSize(width, height);
        stars2.setY(0);
    }

    public static void loadShip(int nShip) {
        // SHIP
        Texture bg = new Texture(Gdx.files.internal("data/ships/"+ (nShip+1) +".png"));
        bg.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        ship1 = new TextureRegion(bg, 0, 2, 450, 300);
        ship1.flip(false, true);

        ship2 = new TextureRegion(bg, 0, 304, 450, 300);
        ship2.flip(false, true);

        ship3 = new TextureRegion(bg, 0, 606, 450, 300);
        ship3.flip(false, true);

        TextureRegion[] ship = { ship1, ship2, ship3 };
        shipAnimation = new Animation(0.14f, ship);
        shipAnimation.setPlayMode(Animation.PlayMode.LOOP);
    }

    public static void setHighScore(int val) {
        prefs.putInteger("HighScore", val);
        prefs.flush();
    }

    public static int getHighScore() {
        return prefs.getInteger("HighScore");
    }

    public static void setSelectedShip(int val) {
        prefs.putInteger("nShip", val);
        prefs.flush();

    }

    public static int getSelectedShip() {
        return prefs.getInteger("nShip");
    }

    public static void setTotalEnergy(int val) {
        prefs.putInteger("TotalEnergy", val);
        prefs.flush();
    }

    public static int getTotalEnergy() {
        return prefs.getInteger("TotalEnergy");
    }

    public static void dispose() {
        font.dispose();
    }
}
