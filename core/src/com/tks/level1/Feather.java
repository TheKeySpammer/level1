package com.tks.level1;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

class Feather extends AbstractGameObject {

    private TextureRegion regFeather;

    boolean collected;

    Feather(){
        init();
    }

    private void init(){
        dimension.set(0.5f, 0.5f);

        regFeather = Assets.getInstance().feather.feather;

//        Set bounding box for collision detection

        bounds.set(0, 0, dimension.x, dimension.y);

        collected = false;
    }

    @Override
    void render(SpriteBatch batch) {
        if (collected) return;

        TextureRegion reg = regFeather;
        batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y,
                dimension.x, dimension.y, scale.x, scale.y, rotation,
                reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);

    }

    int getScore(){
        return 250;
    }

}
