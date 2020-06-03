package com.tks.level1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {

    private final static String TAG = Assets.class.getName();
    AssetBunny bunny;
    AssetFeather feather;
    AssetGoldCoin goldCoin;
    AssetRock rock;
    AssetLevelDecoration levelDecoration;
    AssetCarrot carrot;
    AssetFonts fonts;
    AssetGoal goal;

    AssetSounds sounds;
    AssetMusic music;

    private static Assets instance;

    private AssetManager assetManager;

//    singleton instance for instantiation from other classes.

    private Assets(){ }


    void init(AssetManager assetManager){
        this.assetManager = assetManager;
        assetManager.setErrorListener(this);
        assetManager.load(Constants.TEXTURE_ATLAS_OBJECT, TextureAtlas.class);
//        Load Sounds
        assetManager.load("Sounds/jump.wav", Sound.class);
        assetManager.load("Sounds/jump_with_feather.wav", Sound.class);
        assetManager.load("Sounds/live_lost.wav", Sound.class);
        assetManager.load("Sounds/pickup_coin.wav", Sound.class);
        assetManager.load("Sounds/pickup_feather.wav", Sound.class);

//        Load Music
        assetManager.load("Music/keith303_-_brand_new_highscore.mp3", Music.class);
        assetManager.finishLoading();
        Gdx.app.debug(TAG, "Number of assets loaded: "+assetManager.getAssetNames().size);
        for (String name: assetManager.getAssetNames()){
            Gdx.app.debug(TAG, "Asset :"+name);
        }

//        Creating Assets
        TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECT);
        for(Texture t: atlas.getTextures()){
            t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

        fonts = new AssetFonts();
        bunny = new AssetBunny(atlas);
        rock = new AssetRock(atlas);
        goldCoin = new AssetGoldCoin(atlas);
        feather = new AssetFeather(atlas);
        levelDecoration = new AssetLevelDecoration(atlas);
        carrot = new AssetCarrot(atlas);
        goal = new AssetGoal(atlas);

        sounds = new AssetSounds(assetManager);
        music = new AssetMusic(assetManager);

    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Error loading asset '"+asset.fileName+"'", throwable);
    }

    @Override
    public void dispose() {

        assetManager.dispose();
        fonts.defaultSmall.dispose();
        fonts.defaultNormal.dispose();
        fonts.defaultBig.dispose();

    }

    static class AssetBunny{
        final TextureAtlas.AtlasRegion bunny_head;
        AssetBunny(TextureAtlas atlas){
            bunny_head = atlas.findRegion("bunny_head");
        }
    }

    static class AssetRock{
        final TextureAtlas.AtlasRegion edge;
        final TextureAtlas.AtlasRegion middle;

        AssetRock(TextureAtlas atlas){
            edge = atlas.findRegion("rock_edge");
            middle = atlas.findRegion("rock_middle");
        }
    }

    static class AssetCarrot{
        final TextureAtlas.AtlasRegion carrot;
        AssetCarrot(TextureAtlas atlas){carrot = atlas.findRegion("carrot");}
    }

    static class AssetGoldCoin{
        final TextureAtlas.AtlasRegion goldCoin;
        AssetGoldCoin(TextureAtlas atlas){
            goldCoin = atlas.findRegion("item_gold_coin");

        }
    }

    static class AssetGoal {
        final TextureAtlas.AtlasRegion goal;
        AssetGoal(TextureAtlas atlas){
            goal = atlas.findRegion("goal");
        }
    }

    static class AssetFeather{
        final TextureAtlas.AtlasRegion feather;
        AssetFeather(TextureAtlas atlas){
            feather = atlas.findRegion("item_feather");
        }
    }

    static class AssetLevelDecoration{
        final TextureAtlas.AtlasRegion cloud01;
        final TextureAtlas.AtlasRegion cloud02;
        final TextureAtlas.AtlasRegion cloud03;
        final TextureAtlas.AtlasRegion mountain_left;
        final TextureAtlas.AtlasRegion mountain_right;
        final TextureAtlas.AtlasRegion waterOverlay;
        AssetLevelDecoration(TextureAtlas atlas){
            cloud01 = atlas.findRegion("cloud01");
            cloud02 = atlas.findRegion("cloud02");
            cloud03 = atlas.findRegion("cloud03");
            mountain_left = atlas.findRegion("mountain_left");
            mountain_right = atlas.findRegion("mountain_right");
            waterOverlay = atlas.findRegion("water_overlay");
        }
    }

    public static class AssetFonts {
        BitmapFont defaultSmall;
        BitmapFont defaultNormal;
        BitmapFont defaultBig;

        AssetFonts (){
//            Create three fonts
            defaultSmall = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
            defaultNormal = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
            defaultBig = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);

            defaultSmall.getData().setScale(0.75f);
            defaultNormal.getData().setScale(1.0f);
            defaultBig.getData().setScale(2.0f);

            defaultSmall.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            defaultNormal.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            defaultBig.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }

    }

    static class AssetSounds {
        Sound jump;
        Sound jumpWithFeather;
        Sound pickUpCoin;
        Sound pickUpFeather;
        Sound liveLost;
        AssetSounds (AssetManager am){
            jump = am.get("Sounds/jump.wav", Sound.class);
            jumpWithFeather = am.get("Sounds/jump_with_feather.wav", Sound.class);
            liveLost = am.get("Sounds/live_lost.wav", Sound.class);
            pickUpCoin = am.get("Sounds/pickup_coin.wav", Sound.class);
            pickUpFeather = am.get("Sounds/pickup_feather.wav", Sound.class);
        }
    }

    static class AssetMusic {
        Music song01;
        AssetMusic( AssetManager am) {
            song01 = am.get("Music/keith303_-_brand_new_highscore.mp3", Music.class);
        }
    }


    static Assets getInstance(){
        if(instance == null){
            instance = new Assets();
        }
        return instance;
    }
}
