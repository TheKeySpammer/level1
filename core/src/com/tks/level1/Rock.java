package com.tks.level1;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

class Rock extends AbstractGameObject {

    private TextureRegion regEdge;
    private TextureRegion regMiddle;

    private final float FLOAT_CYCLE_TIME = 2.0f;
    private final float FLOAT_AMPLITUDE = 0.25f;

    private float floatCycleTimeLeft;
    private boolean floatingDownwards;
    private Vector2 floatTargetPosition;

    private int length;

    Rock(){init();}

    private void init(){
        Assets assets = Assets.getInstance();
        dimension.set(1, 1.5f);
        regEdge = assets.rock.edge;
        regMiddle = assets.rock.middle;
        setLength(1);

        floatingDownwards = false;
        floatCycleTimeLeft = MathUtils.random(0, FLOAT_CYCLE_TIME / 2);
        floatTargetPosition = null;
    }

    int getLength(){
        return length;
    }

    void setLength (int length){
        this.length = length;
//        Update bounding box for collision detection
        bounds.set(0, 0, dimension.x  * length , dimension.y );
    }

    void increaseLength(int amount){
        setLength(length + amount);
    }

    @Override
    void render(SpriteBatch batch) {

//        Setting up a Texture Region.
        TextureRegion reg;

        float relX = 0;
        float relY = 0.1f;

//        Drawing the left edge
        reg = regEdge;
        relX -= dimension.x/4;

        batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x, origin.y, dimension.x/4, dimension.y, scale.x, scale.y,
        rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);

//        Drawing the middle portion of rock
        relX = 0;
        reg = regMiddle;

        for (int i = 0; i < length; i++){
            batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x, origin.y,
                    dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
            relX += dimension.x;
        }

//        Drawing the right edge
        reg = regEdge;
        batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x + dimension.x/8, origin.y, dimension.x/4, dimension.y, scale.x, scale.y,
                rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), true, false);
    }

    @Override
    public void update (float deltaTime){
        super.update(deltaTime);

        floatCycleTimeLeft -= deltaTime;
        if (floatTargetPosition == null)
            floatTargetPosition = new Vector2(position);
        if(floatCycleTimeLeft <= 0){
            floatCycleTimeLeft = FLOAT_CYCLE_TIME;
            floatingDownwards = !floatingDownwards;
            body.setLinearVelocity(0, FLOAT_AMPLITUDE * (floatingDownwards ? -1 : 1));
        } else {
            body.setLinearVelocity(body.getLinearVelocity().scl(0.98f));
        }

    }

}

