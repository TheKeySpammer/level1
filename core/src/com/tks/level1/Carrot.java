package com.tks.level1;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

class Carrot extends AbstractGameObject {

    private TextureRegion regCarrot;

    Carrot () {
        init();
    }

    private void init() {
        dimension.set(0.25f, 0.5f);

        regCarrot = Assets.getInstance().carrot.carrot;

//        Set bounding box for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);
        origin.set(dimension.x / 2, dimension.y / 2);
    }

    @Override
    void render(SpriteBatch batch) {

        TextureRegion reg = regCarrot;

        batch.draw(reg.getTexture(), position.x - origin.x, position.y - origin.y,
                origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation,
                reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);

    }
}
