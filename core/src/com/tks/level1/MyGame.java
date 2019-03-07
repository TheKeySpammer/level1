package com.tks.level1;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;

public class MyGame extends Game {

    @Override
    public void create() {
        Assets assets = Assets.getInstance();
        assets.init(new AssetManager());
        AudioManger audioManger = AudioManger.getInstance();
        GamePreferences.getInstance().load();
        audioManger.play(assets.music.song01);
        menuScreen(this);
    }

    void gameScreen(MyGame game){
        setScreen(new GameScreen(game));
    }

    void menuScreen(MyGame game){
        setScreen(new MenuScreen(game));
    }
}
