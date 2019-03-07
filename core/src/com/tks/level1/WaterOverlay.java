package com.tks.level1;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

class WaterOverlay extends AbstractGameObject {
    private TextureRegion regWaterOverlay;
    private float length;
    private Assets assets;

    WaterOverlay(float length){
        this.length = length;
        assets = Assets.getInstance();
        init();
    }

    private void init(){
        dimension.set(length * 10, 3);
        regWaterOverlay = assets.levelDecoration.waterOverlay;
        origin.x -= dimension.x / 2;
    }


    @Override
    void render(SpriteBatch batch) {
        TextureRegion reg = regWaterOverlay;
        batch.draw(reg.getTexture(), position.x + origin.x, position.y + origin.y, origin.x, origin.y,
                dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);

    }
}
