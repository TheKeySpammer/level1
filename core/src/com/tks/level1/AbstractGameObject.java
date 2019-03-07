package com.tks.level1;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

abstract class AbstractGameObject {
    Vector2 position;
    Vector2 dimension;
    Vector2 scale;
    Vector2 origin;
    float rotation;

    Vector2 velocity;
    Vector2 terminalVelocity;
    Vector2 acceleration;
    Vector2 friction;
    Rectangle bounds;

    Body body;

    AbstractGameObject(){
        position = new Vector2();
        dimension = new Vector2(1, 1);
        scale = new Vector2(1, 1);
        rotation = 0;
        origin = new Vector2();

        velocity = new Vector2();
        terminalVelocity = new Vector2(1, 1);
        friction = new Vector2();
        acceleration = new Vector2();
        bounds = new Rectangle();
    }

    void update(float delta){
        if(body == null) {
            updateMotionX(delta);
            updateMotionY(delta);

//        Move to new position
            position.x += velocity.x * delta;
            position.y += velocity.y * delta;
        }
        else {
            position.set(body.getPosition());
            rotation = body.getAngle() * MathUtils.radiansToDegrees;
        }

    }

    abstract void render(SpriteBatch batch);

    void updateMotionX (float deltaTime){
        if (velocity.x != 0){
//            Apply friction
            if (velocity.x > 0){
                velocity.x = Math.max(velocity.x - friction.x * deltaTime, 0);
            } else {
                velocity.x = Math.min(velocity.x + friction.x * deltaTime, 0);

            }
        }

//        Apply Acceleration
        velocity.x += acceleration.x * deltaTime;
//        Make sure the object's velocity does not exceed the positive or negative terminal velocity
        velocity.x = MathUtils.clamp(velocity.x, -terminalVelocity.x, terminalVelocity.x);
    }

    void updateMotionY (float deltaTime){
        if (velocity.y != 0){
//            Apply friction
            if (velocity.y > 0){
                velocity.y = Math.max(velocity.y - friction.y * deltaTime, 0);
            } else {
                velocity.y = Math.min(velocity.y + friction.y * deltaTime, 0);
            }
        }

//        Apply Acceleration
        velocity.y += acceleration.y * deltaTime;
//        Make sure the object's velocity does not exceed the positive or negative terminal velocity
        velocity.y = MathUtils.clamp(velocity.y, -terminalVelocity.y, terminalVelocity.y);
    }

}
