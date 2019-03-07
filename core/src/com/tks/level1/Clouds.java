package com.tks.level1;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

class Clouds extends AbstractGameObject {

    private Array<TextureRegion> regClouds;
    private Array<Cloud> clouds;

    private float length;

    private class Cloud extends AbstractGameObject{
        private TextureRegion regCloud;
        Cloud(){}

        void setRegion(TextureRegion region) {
            regCloud = region;
        }

        @Override
        void render(SpriteBatch batch) {

            TextureRegion reg = regCloud;
            batch.draw(reg.getTexture(), position.x + origin.x, position.y + origin.y, origin.x, origin.y,
                    dimension.x, dimension.y, scale.x, scale.y,rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false
                    );
        }
    }

    Clouds(float length){
        this.length = length;
        init();
    }

    void init(){
        Assets assets = Assets.getInstance();
        dimension.set(3.0f, 1.5f);
        regClouds = new Array<TextureRegion>();
        regClouds.add(assets.levelDecoration.cloud01);
        regClouds.add(assets.levelDecoration.cloud02);
        regClouds.add(assets.levelDecoration.cloud03);

        int distFac = 5;
        int numClouds = (int)(length / distFac);
        clouds = new Array<Cloud>(2 * numClouds);
        for (int i = 0; i < numClouds; i++) {
            Cloud cloud = spawnCloud();
            cloud.position.x = i * distFac;
            clouds.add(cloud);
        }
    }

    private Cloud spawnCloud(){
        Cloud cloud = new Cloud();
        cloud.dimension.set(dimension);
//        Select random cloud image
        cloud.setRegion(regClouds.random());
//        position
        Vector2 pos = new Vector2();
//        Speed
        Vector2 speed = new Vector2();
        speed.x += 0.2f; // vase speed
//        Random addition speed
        speed.x += MathUtils.random(0.0f, 0.25f);
        cloud.terminalVelocity.set(speed);
        speed.x *= -1;
        cloud.velocity.set(speed);
        pos.x = length + 10; // Position after the end of the level
        pos.y += 1.75; // base position
        pos.y += MathUtils.random(0.0f, 0.2f)
        *(MathUtils.randomBoolean() ? 1: -1); // random additional position
        cloud.position.set(pos);
        return cloud;
    }

    @Override
    void render(SpriteBatch batch) {
        for (Cloud cloud :
                clouds) {
            cloud.render(batch);
        }
    }

    @Override
    public void update (float deltaTime) {
        for(int i = clouds.size - 1; i >= 0; i--){
            Cloud cloud = clouds.get(i);
            cloud.update(deltaTime);
            if(cloud.position.x < -10){
//                Cloud moves outside the world
//                Destroy and spawn new cloud at end of level
                clouds.removeIndex(i);
                clouds.add(spawnCloud());
            }
        }
    }

}
