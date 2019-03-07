package com.tks.level1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

class GameScreen implements Screen, Constants {

    private WorldController worldController;
    private WorldRenderer worldRenderer;
    private SpriteBatch batch;
    private Assets assets;
    private MyGame game;

    GameScreen (MyGame game){
        this.game = game;
    }

    @Override
    public void show() {
        assets = Assets.getInstance();
        batch = new SpriteBatch();
        GamePreferences.getInstance().load();
        worldController = new WorldController(game);
        worldRenderer = new WorldRenderer(worldController);
    }

    @Override
    public void render(float delta) {
        worldController.update(delta);
        Gdx.gl20.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        batch.setProjectionMatrix(camera.combined);
        worldRenderer.render(batch);
    }

    @Override
    public void resize(int width, int height) {
        worldRenderer.resize(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume(){
        assets.init(new AssetManager());
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        worldController.dispose();
        assets.dispose();
        worldRenderer.dispose();
    }
}
