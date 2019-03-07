package com.tks.level1;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

class GoldCoin extends AbstractGameObject {

    private TextureRegion regGoldCoin;
    boolean collected;
    Assets assets;

    GoldCoin (){
        init();
    }

    private void init(){
        assets = Assets.getInstance();
        dimension.set(0.5f, 0.5f);

        regGoldCoin = assets.goldCoin.goldCoin;

//        Set Bounding box for collision Detection
        bounds.set(0, 0, dimension.x, dimension.y);

        collected = false;
    }

    @Override
    void render(SpriteBatch batch) {
        if(collected) return;

        TextureRegion reg = regGoldCoin;
        batch.draw(reg.getTexture(), position.x, position.y,origin.x, origin.y,
                dimension.x, dimension.y, scale.x, scale.y, rotation,
                reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);

    }

    int getScore(){
        return 100;
    }
}
