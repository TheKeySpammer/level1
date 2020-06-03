package com.tks.level1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

class BunnyHead extends AbstractGameObject implements Constants{

    private TextureRegion regHead;

    private VIEW_DIRECTION view_direction;
    private float timeJumping;
    JUMP_STATE jump_state;
    private boolean hasFeatherPowerup;
    float timeLeftFeatherPowerup;



    private final ParticleEffect dustParticles = new ParticleEffect();

    BunnyHead (){
        init();
    }

    void init() {
        dimension.set(1, 1);
        regHead = Assets.getInstance().bunny.bunny_head;
//        Center image on game object
        origin.set(dimension.x / 2 , dimension.y / 2);
//        Bounding box for collision detection
        bounds.set(0, 0, dimension.x , dimension.y );
//        Set physics values
        terminalVelocity.set(3.0f, 4.f);
        friction.set(12.f, 0);
        acceleration.set(0, -25.f);
//        View Direction
        view_direction = VIEW_DIRECTION.RIGHT;
//        Jump state
        jump_state = JUMP_STATE.FALLING;
        timeJumping = 0;
//        Power-ups
        hasFeatherPowerup = false;
        timeLeftFeatherPowerup = 0;

//        Particles
        dustParticles.load(Gdx.files.internal("particles/dust.pfx"),
                Gdx.files.internal("particles"));
    }
    void setJumping (boolean jumpKeyPressed){
        switch (jump_state){
            case GROUNDED: // Character is standing on a platform
                if (jumpKeyPressed) {
                    AudioManger.getInstance().play(Assets.getInstance().sounds.jump);
//                    Start counting jump time from the beginning
                    timeJumping = 0;
                    jump_state = JUMP_STATE.JUMP_RISING;
                }
                break;
            case JUMP_RISING:
                if(!jumpKeyPressed)
                    jump_state = JUMP_STATE.FALLING;
                break;
            case FALLING: // Falling down
            case JUMP_FALLING: // falling down after jump
                if (jumpKeyPressed && hasFeatherPowerUp()){
                    AudioManger.getInstance().play(Assets.getInstance().sounds.jumpWithFeather, 1, MathUtils.random(1.0f, 1.1f));
                    timeJumping = JUMP_TIME_OFFSET_FLYING;
                    jump_state = JUMP_STATE.JUMP_RISING;
                }
                break;
        }
    }

    void setFeatherPowerup (boolean pickedUp){
        hasFeatherPowerup = pickedUp;
        if (pickedUp){
            timeLeftFeatherPowerup = ITEM_FEATHER_POWERUP_DURATION;
        }
    }


    private boolean hasFeatherPowerUp() {return hasFeatherPowerup && timeLeftFeatherPowerup > 0;}

    @Override
    void render(SpriteBatch batch) {

        TextureRegion reg = regHead;
        batch.setColor(CharacterSkin.values()[GamePreferences.getInstance().charSkin].getColor());

//        Draw Particles
        dustParticles.draw(batch);

//        Set special color when game object has a feather power-up
        if (hasFeatherPowerUp()){
            batch.setColor(1.0f, 0.8f, 0, 1);
        }

//        Draw Image
        reg = regHead;
        boolean flipX = view_direction == VIEW_DIRECTION.LEFT;

        batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y,
                dimension.x, dimension.y, scale.x, scale.y, rotation,
                reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), flipX, false);
        batch.setColor(1, 1, 1, 1);
    }

    @Override
    void update(float delta) {
        super.update(delta);
        if (velocity.x != 0) {
            view_direction = velocity.x < 0 ? VIEW_DIRECTION.LEFT : VIEW_DIRECTION.RIGHT;
        }
        if (timeLeftFeatherPowerup > 0){
            timeLeftFeatherPowerup -= delta;
            if (timeLeftFeatherPowerup < 0){
//                disable power-up
                timeLeftFeatherPowerup = 0;
                setFeatherPowerup(false);
            }
        }
        bounds.setPosition(position.x + 0.1f, position.y);
        dustParticles.update(delta);
    }


    @Override
    void updateMotionY(float deltaTime) {

        switch (jump_state) {
            case GROUNDED:
                jump_state = JUMP_STATE.FALLING;
                if (velocity.x != 0 ){
                    dustParticles.setPosition(position.x + dimension.x / 2, position.y);
                    dustParticles.start();
                }
                break;
            case JUMP_RISING:
//                Keep track of jump time
                timeJumping += deltaTime;
//                Jump time left ?
                if (timeJumping <= JUMP_TIME_MAX) {
//                    Still Jumping
                    velocity.y = terminalVelocity.y;
                }
                break;
            case FALLING:
                break;
            case JUMP_FALLING:
//                Add delta time to track the jump time
                timeJumping += deltaTime;
//                Jump to minimal height if jump key was pressed too short
                if (timeJumping > 0 && timeJumping <= JUMP_TIME_MIN) {
//                    Still Jumping
                    velocity.y = terminalVelocity.y;
                }
                break;
        }
        if (jump_state != JUMP_STATE.GROUNDED) {
            dustParticles.allowCompletion();
            super.updateMotionY(deltaTime);
        }

    }

}