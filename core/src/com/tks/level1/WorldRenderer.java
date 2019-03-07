package com.tks.level1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

class WorldRenderer{

    private WorldController worldController;
    private OrthographicCamera camera;
    private OrthographicCamera cameraGUI;
    private Assets assets;
    private ShapeRenderer renderer;
    private Box2DDebugRenderer debugRenderer;
    private static final boolean DEBUG_DRAW_BOX2D = false;


    WorldRenderer(WorldController worldController){
        this.worldController = worldController;
        init();
    }

    private void init() {
        camera = new OrthographicCamera(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        camera.position.set(0, 0, 0);
        camera.update();

        cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        cameraGUI.position.set(0, 0, 0);
        cameraGUI.setToOrtho(true); // flip y-axis
        cameraGUI.update();

        assets = Assets.getInstance();

        renderer = new ShapeRenderer();
        debugRenderer = new Box2DDebugRenderer();
    }

    void render(SpriteBatch batch){
        renderWorld(batch);
        renderGUI(batch);
    }

    private void renderWorld(SpriteBatch batch){
        batch.setProjectionMatrix(camera.combined);
        renderer.setProjectionMatrix(camera.combined);
        batch.begin();
        worldController.cameraHelper.applyTo(camera);
        worldController.level.render(batch, renderer);
        batch.end();

        if(DEBUG_DRAW_BOX2D){
            debugRenderer.render(worldController.world, camera.combined);
        }
    }

    private void renderGUI (SpriteBatch batch){
        batch.setProjectionMatrix(cameraGUI.combined);
        batch.begin();
        renderGuiScore(batch);
//        draw collected feather icon
        renderGuiFeatherPowerup(batch);
        renderGuiExtraLive(batch);
        if (GamePreferences.getInstance().showFPS)
            renderGuiFpsCounter(batch);
        renderGuiGameOverMessage(batch);
        batch.end();
    }

    private void renderGuiScore(SpriteBatch batch) {
        float x = -15;
        float y = Constants.VIEWPORT_GUI_HEIGHT - 75;
        batch.draw(assets.goldCoin.goldCoin, x, y,50, 50, 100, 100, 0.35f, -0.35f, 0);
        assets.fonts.defaultBig.draw(batch, "" + worldController.score, x + 75, y + 37);
    }

    private void renderGuiExtraLive (SpriteBatch batch){
        float x = cameraGUI.viewportWidth - 50 - Constants.LIVES_START * 50;
        float y = -15;
        for (int i = 0; i < Constants.LIVES_START; i++) {
            if (worldController.lives + 1 <= i)
                batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
            batch.draw(assets.bunny.bunny_head, x + i * 50, y, 50, 50, 120, 100, 0.35f, -0.35f, 0);
            batch.setColor(1, 1, 1, 1);
        }

        if(worldController.lives >= 0 && worldController.livesVisual > worldController.lives){
            int i = worldController.lives + 1 ;
            float alphaColor = Math.max(0, worldController.livesVisual - worldController.lives - 0.5f);
            float alphaScale = -0.35f * (2 + worldController.lives - worldController.livesVisual) * 2;
            float alphaRotate = -45 * alphaColor;
            batch.setColor(1.0f, 0.7f, 0.7f, alphaColor);
            batch.draw(Assets.getInstance().bunny.bunny_head, x + i * 50, y , 50, 50, 120, 100, alphaScale, -alphaScale, alphaRotate);
            batch.setColor(1, 1, 1, 1);
        }

    }

    private void renderGuiFpsCounter (SpriteBatch batch){
        float x = cameraGUI.viewportWidth - 55;
        float y = cameraGUI.viewportHeight - 15;
        int fps = Gdx.graphics.getFramesPerSecond();
        BitmapFont fpsFont = assets.fonts.defaultNormal;
        if (fps >= 45){
            fpsFont.setColor(0, 1, 0, 1);

        }
        else if (fps >= 30){
            fpsFont.setColor(1, 1, 0, 1);
        }
        else {
            fpsFont.setColor(1, 0, 0, 1);
        }

        fpsFont.draw(batch, "FPS: "+fps, x, y);
        fpsFont.setColor(1, 1, 1, 1);
    }


    private void renderGuiGameOverMessage (SpriteBatch batch) {
        float x = cameraGUI.viewportWidth / 2;
        float y = cameraGUI.viewportHeight / 2;
        if (worldController.isGameOver()){
            BitmapFont fontGameOver = assets.fonts.defaultBig;
            fontGameOver.getData().setScale(3.5f, 3.5f);
            fontGameOver.setColor(1, 0.75f, 0.25f, 1);
            fontGameOver.draw(batch, "GAME OVER", x - 150, y + 15);
            fontGameOver.setColor(1, 1, 1, 1);
        }
    }

    private void renderGuiFeatherPowerup (SpriteBatch batch){
        float x = -10;
        float y = Constants.VIEWPORT_GUI_HEIGHT - 75 - 60;
        float timeLeftFeatherPowerup = worldController.level.bunnyHead.timeLeftFeatherPowerup;
        if(timeLeftFeatherPowerup > 0){
//            Start icon fade in/out if the left power-up time is less than 4 seconds.
//            The fade interval is set to 5 changes per seconds.
            if (timeLeftFeatherPowerup < 4){
                if (((int)(timeLeftFeatherPowerup * 5)%2) != 0) {
                    batch.setColor(1, 1, 1, 0.5f);
                }
            }
            batch.draw(assets.feather.feather, x, y, 50 ,50, 100, 100, 0.45f, -0.45f, 0);
            batch.setColor(1, 1, 1, 1);
            assets.fonts.defaultNormal.draw(batch, ""+(int)timeLeftFeatherPowerup, x+60, y+57);
        }
    }

    private void renderTutorialText(SpriteBatch batch){
        float x = 3.0f;
        float y = 2.0f;

        batch.setColor(1, 1, 1, 1);
        assets.fonts.defaultBig.draw(batch, "Use arrow keys to move", x, y);
    }


    void resize(int width, int height){
        cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
        cameraGUI.viewportWidth = Constants.VIEWPORT_GUI_WIDTH / (float) height * (float) width;
        cameraGUI.position.set(cameraGUI.viewportWidth / 2 ,cameraGUI.viewportHeight / 2, 0);
        cameraGUI.update();
        camera.viewportWidth = (Constants.WORLD_SIZE / height) * width;
        camera.update();
    }

    void dispose(){
        renderer.dispose();
    }
}
