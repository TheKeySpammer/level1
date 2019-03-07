package com.tks.level1;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

class Goal extends AbstractGameObject {

    private TextureRegion regGoal;

    Goal () {
        init();
    }

    void init() {
        dimension.set(3.0f, 3.0f);
        regGoal = Assets.getInstance().goal.goal;

//        Collision detection box
        bounds.set(-0.1f, Float.MIN_VALUE, 10, Float.MAX_VALUE);
        origin.set(dimension.x / 2.0f, 0.0f);
    }

    @Override
    void render(SpriteBatch batch) {
        TextureRegion reg;
        reg = regGoal;

        batch.draw(reg.getTexture(), position.x - origin.x, position.y - origin.y, origin.x, origin.y,
                dimension.x, dimension.y, scale.x, scale.y, rotation,
                reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);

    }
}
