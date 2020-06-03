package com.tks.level1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

class GamePreferences {
    private static GamePreferences instance;

    boolean sound;
    boolean music;
    float volSound;
    float volMusic;
    int charSkin;
    boolean showFPS;

    private final Preferences prefs;

//    Singleton
    private GamePreferences(){
        prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
    }


    void load() {
        sound = prefs.getBoolean("sound", true);
        music = prefs.getBoolean("music", true);
        volSound = MathUtils.clamp(prefs.getFloat("volSound", 0.5f), 0, 1.0f);
        volMusic = MathUtils.clamp(prefs.getFloat("volMusic", 0.5f), 0, 1.0f);
        charSkin = MathUtils.clamp(prefs.getInteger(
                "charSkin", 0), 0, 2);
        showFPS = prefs.getBoolean("showFPS", false);
    }
    void save() {
        prefs.putBoolean("sound", sound);
        prefs.putBoolean("music", music);
        prefs.putFloat("volSound", volSound);
        prefs.putFloat("volMusic", volMusic);
        prefs.putInteger("charSkin", charSkin);
        prefs.putBoolean("showFPS", showFPS);
        prefs.flush();
    }

    static GamePreferences getInstance () {
        if(instance == null){
            instance = new GamePreferences();
        }
        return instance;
    }
}
