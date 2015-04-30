package com.tastysandwich.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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

    public static Animation energyAnimation;
    public static Animation explosionAnimation;

    public static Sprite scorebg;

    private static Preferences prefs;

    public static BitmapFont font;

    public static Sprite[] energyBar;

    public static Sprite pause, pauseScreen, startScreen, hit;

    public static Sprite shieldOn, shieldOff;

    public static Sprite sSoundsF, sSoundsT;

    /////////////////////////////////////MENU ASSETS////////////////////////////////////////////
    public static Sprite sMenuBackground, hangarBackground;

    public static SpriteDrawable sdPlay,sdHangar, sdSoundsT, sdSoundsF;



    /////////////////////////////////////HANGAR ASSETS////////////////////////////////////////////


    public static Sprite[] hangarShips;





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
        if (!prefs.contains("Sounds")) {
            prefs.putBoolean("Sounds", true);
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

        texture = new Texture(Gdx.files.internal("data/hangarbackground.jpg"));
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        hangarBackground = new Sprite(texture);
        hangarBackground.setSize(width, height);
        hangarBackground.flip(false,true);
        hangarBackground.setY(0);
        hangarBackground.setX(0);

    }


    private static void loadButtons(int width, int height) {
        // BUTTON PLAY
        Texture texture = new Texture("data/skins/play.png");
        Sprite sprite = new Sprite(texture);
        sprite.setSize(width / 16 * 5, height / 20 * 3);
        sprite.flip(false, true);
        sdPlay = new SpriteDrawable(sprite);

        // BUTTON HANGAR
        texture = new Texture("data/skins/hangar.png");
        sprite = new Sprite(texture);
        sprite.setSize(width / 16 * 5, height / 20 * 3);
        sprite.flip(false,true);
        sdHangar = new SpriteDrawable(sprite);

        //BUTTON SOUNDS TRUE
        texture = new Texture("data/soundsT.png");
        sSoundsT = new Sprite(texture);
        sSoundsT.setSize(width/14, width/14);
        sSoundsT.flip(false,true);
        sdSoundsT = new SpriteDrawable(sSoundsT);

        //BUTTON SOUNDS FALSE
        texture = new Texture("data/soundsF.png");
        sSoundsF = new Sprite(texture);
        sSoundsF.setSize(width/14, width/14);
        sSoundsF.flip(false,true);
        sdSoundsF = new SpriteDrawable(sSoundsF);



    }

    private static void loadGameAssets(int width, int height) {
        loadShip(getSelectedShip());
        loadGameBackground(width, height);
        loadAsteroids();
        loadEnergy();
        loadExplosion();
        loadEnergyBar(width, height);
        loadGOTables(width, height);
        loadHangarShips(width, height);
        loadPause(width, height);
        loadShield(width, height);
    }

    private static void loadShield(int width, int height) {
        shieldOff = new Sprite(new Texture(Gdx.files.internal("data/energybar/a.jpg")));
        shieldOff.setPosition((width / 4) * 3 - height/16, height - height/18);
        shieldOff.setSize(height/18, height/18);
        shieldOff.flip(false, true);

        shieldOn = new Sprite(new Texture(Gdx.files.internal("data/energybar/b.jpg")));
        shieldOn.setPosition((width / 4) * 3 - height/16, height - height/18);
        shieldOn.setSize(height/18, height/18);
        shieldOn.flip(false, true);

    }

    private static void loadHangarShips(int width, int height) {
        hangarShips = new Sprite[7];
        for(int i=0; i<7; i++) {
            hangarShips[i] = new Sprite(new Texture(Gdx.files.internal("data/hangarships/" + (i+ 1) + ".png")));
            hangarShips[i].setSize(width / 6 * 4, height / 6 * 4);
            hangarShips[i].setPosition(width / 6, height / 8);
            hangarShips[i].flip(false, true);
        }
    }

    private static void loadEnergyBar(int width, int height) {
        energyBar = new Sprite[11];
        for (int i=0; i<=10; i++){
            energyBar[i] = new Sprite(new Texture(Gdx.files.internal("data/energybar/" + i + ".png")));
            energyBar[i].setPosition((width / 4) * 3, (height / 18) * 17);
            energyBar[i].setSize(width / 4, height/18);
        }
    }

    private static void loadGOTables(int width, int height) {
        // BACKGROUND GAMEOVER
        scorebg = new Sprite(new Texture(Gdx.files.internal("data/skins/score.png")));
        scorebg.setPosition(width /8, height/8);
        scorebg.setSize(width-(width/8+width/8), height-(height/8+height/8));
        scorebg.flip(false, true);
    }
    private static void loadPause(int width, int height) {
        pause = new Sprite(new Texture(Gdx.files.internal("data/pause/pause.png")));
        pause.setSize(width/10, height/10 + height/20);

        pauseScreen = new Sprite(new Texture(Gdx.files.internal("data/pause/pausescreen.png")));
        pauseScreen.setSize(width, height);
        pauseScreen.setPosition(0,0);
        pauseScreen.flip(false, true);

        startScreen = new Sprite(new Texture(Gdx.files.internal("data/pause/taptostart.png")));
        startScreen.setSize(width, height);
        startScreen.setPosition(0,0);
        startScreen.flip(false, true);

        hit = new Sprite(new Texture(Gdx.files.internal("data/pause/praskliny.png")));
        hit.setSize(width, height);
        hit.setPosition(0,0);
        hit.setAlpha(0);
        hit.flip(false, true);


    }

    private static void loadEnergy() {
        // ENERGY
        Texture energySheet = new Texture(Gdx.files.internal("data/energy_animation.png")); // #9
        TextureRegion[][] tmp = TextureRegion.split(energySheet, energySheet.getWidth()/37, energySheet.getHeight()/2);              // #10
        TextureRegion[] energyFrames = new TextureRegion[37 * 2];
        int index = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 37; j++) {
                energyFrames[index++] = tmp[i][j];
            }
        }
        energyAnimation = new Animation(0.027f, energyFrames);
        energyAnimation.setPlayMode(Animation.PlayMode.LOOP);
    }

    private static void loadExplosion() {
        // ENERGY
        Texture explosionSheet = new Texture(Gdx.files.internal("data/explosion_animation.png")); // #9
        TextureRegion[][] tmp = TextureRegion.split(explosionSheet, explosionSheet.getWidth()/9, explosionSheet.getHeight()/5);              // #10
        TextureRegion[] explosionFrames = new TextureRegion[9 * 5];
        int index = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 9; j++) {
                explosionFrames[index++] = tmp[i][j];
            }
        }
        explosionAnimation = new Animation(0.027f, explosionFrames);
        explosionAnimation.setPlayMode(Animation.PlayMode.NORMAL);
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
        stars1 = new Sprite(bg);
        stars1.setSize(width, height);
        stars1.setY(0);

        //hviezdy
        bg = new Texture(Gdx.files.internal("data/universe/stars2.png"));
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


    public static void setSounds(boolean val) {
        prefs.putBoolean("Sounds", val);
        prefs.flush();
    }

    public static boolean getSounds() {
        return prefs.getBoolean("Sounds");
    }

    public static void dispose() {
        font.dispose();
    }
}
