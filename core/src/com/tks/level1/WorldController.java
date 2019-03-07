package com.tks.level1;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;


class WorldController extends InputAdapter implements Disposable {
    CameraHelper cameraHelper;
    private Assets assets;
    Level level;
    int lives;
    int score;
    private MyGame game;
    float livesVisual;
    int currentLevel;

    private boolean goalReached;
    World world;

    private Rectangle r1 = new Rectangle();
    private Rectangle r2 = new Rectangle();
    private float timeLeftGameOverDelay;

    private final static String TAG = WorldController.class.getName();

    WorldController(MyGame game){
        this.game = game;
        init();
    }

    private void init(){
        assets = Assets.getInstance();
        cameraHelper = new CameraHelper();
        Gdx.input.setInputProcessor(this);
        lives = Constants.LIVES_START - 1;
        livesVisual = lives;
        timeLeftGameOverDelay = 0;
        currentLevel = Constants.INITIAL_LEVEL;
        initLevel(currentLevel);
    }

    private void init(int levelNumber){
        lives = Constants.LIVES_START - 1;
        livesVisual = lives;
        timeLeftGameOverDelay = 0;
        initLevel(levelNumber);
    }

    private void initLevel(int level_number){
        score = 0;
        goalReached = false;
        level = new Level(Constants.LEVEL[level_number]);
        cameraHelper.setTarget(level.bunnyHead);
        initPhysics();
    }


    private void initPhysics(){
        if(world != null) world.dispose();
        world = new World(new Vector2(0, -10.0f), true);

//        Rocks
        Vector2 origin = new Vector2();
        for (Rock rock: level.rocks){
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.KinematicBody;
            bodyDef.position.set(rock.position);
            Body body = world.createBody(bodyDef);
            rock.body = body;

            PolygonShape polygonShape = new PolygonShape();
            origin.x = rock.bounds.width / 2.0f;
            origin.y = rock.bounds.height / 2.0f;
            polygonShape.setAsBox(rock.bounds.width / 2.0f, rock.bounds.height / 2.0f, origin, 0);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = polygonShape;
            body.createFixture(fixtureDef);
            polygonShape.dispose();
        }
    }

    boolean isGameOver(){
        return lives < 0;
    }

    private boolean isPlayerInWater() {
        return level.bunnyHead.position.y < -5;
    }



    void update (float delta){
        handleDebugInput(delta);
        if (isGameOver()){
            timeLeftGameOverDelay -= delta;
            if (timeLeftGameOverDelay < 0) game.menuScreen(game);
        }else if(goalReached){
            timeLeftGameOverDelay -= delta;
            if (timeLeftGameOverDelay < 0) {
             if(currentLevel == Constants.NUMBER_OF_LEVELS - 1){
                 game.menuScreen(game);
             }else{
                 init(++currentLevel);
             }
            }
        }
        else{
            handleInputGame(delta);
        }
        level.update(delta);
        testCollisions();
        world.step(delta, 8, 3);
        cameraHelper.update(delta);


//        Logic to reduce lives
        if (!isGameOver() && isPlayerInWater()){
            AudioManger.getInstance().play(assets.sounds.liveLost);
            lives--;
            if(isGameOver())
                timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
            else
                initLevel(currentLevel);
        }
        level.mountains.updateScrollPosition(cameraHelper.getPosition());

        if (livesVisual > lives)
            livesVisual = Math.max(lives, livesVisual - 1*delta);


    }



    private void handleDebugInput(float delta){
//        Camera debugging only available on desktop
        if(Gdx.app.getType() != Application.ApplicationType.Desktop) return;

        if (!cameraHelper.hasTarget(level.bunnyHead)) {
//        Camera Movement

            float camMoveSpeed = 5 * delta;
            float camMoveSpeedAccelerationFactor = 5;
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT))
                camMoveSpeed *= camMoveSpeedAccelerationFactor;
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) moveCamera(-camMoveSpeed, 0);
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) moveCamera(camMoveSpeed, 0);
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) moveCamera(0, camMoveSpeed);
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) moveCamera(0, -camMoveSpeed);
            if (Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)) cameraHelper.setPosition(0, 0);


            // Camera Controls (zoom)

            float camZoomSpeed = 1 * delta;
            float camZoomSpeedAccelerationFactor = 5;
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT))
                camZoomSpeed *= camZoomSpeedAccelerationFactor;
            if (Gdx.input.isKeyPressed(Input.Keys.COMMA)) cameraHelper.addZoom(camZoomSpeed);
            if (Gdx.input.isKeyPressed(Input.Keys.PERIOD)) cameraHelper.addZoom(-camZoomSpeed);
            if (Gdx.input.isKeyPressed(Input.Keys.SLASH)) {
                cameraHelper.setZoom(1.2f);
            }
        }
    }


    private void moveCamera(float x, float y){
        x += cameraHelper.getPosition().x;
        y += cameraHelper.getPosition().y;
        cameraHelper.setPosition(x, y);
    }


    private void handleInputGame (float deltaTime){
        if (cameraHelper.hasTarget(level.bunnyHead)){
//            Player Movement
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
                level.bunnyHead.velocity.x = -level.bunnyHead.terminalVelocity.x;
            }

            else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
            }
            else {
//             Use Accelerometer for movement if available
                float amount = Gdx.input.getAccelerometerY() / 10.0f;
                amount *= 90.0f;
                if(Math.abs(amount) < Constants.ACCEL_MAX_ANGLE_MAX_MOVEMENT){
                    amount = 0;
                }else {
                    amount /= Constants.ACCEL_MAX_ANGLE_MAX_MOVEMENT;
                }
                level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x * amount;
            }

//            Bunny Jump
            if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.SPACE)){
                level.bunnyHead.setJumping(true);
            }
            else{
                level.bunnyHead.setJumping(false);
            }
        }
    }


    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.R){
            init(currentLevel);
            Gdx.app.log(TAG, "World Reset");
        }

//        Toggle Camera Follow
        else if (keycode == Input.Keys.P) {
            cameraHelper.setTarget(cameraHelper.hasTarget() ? null: level.bunnyHead);
            Gdx.app.debug(TAG, "Camera follow enabled : "+cameraHelper.hasTarget());
        }
        else if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK){
            game.menuScreen(game);
        }
        return true;
    }


    private void testCollisions (){
        r1.set(level.bunnyHead.position.x, level.bunnyHead.position.y, level.bunnyHead.bounds.width, level.bunnyHead.bounds.height);

//        Test collision: BunnyHead <-> Rocks
        for (Rock rock: level.rocks){
            r2.set(rock.position.x, rock.position.y, rock.bounds.width, rock.bounds.height);
//            r2.set(rock.bounds);
            if(!r1.overlaps(r2)) continue;
            onCollisionWithRock(rock);
//            MUST DO ALL COLLISIONS FOR VALID EDGE TESTING ON ROCKS
        }

//        Test collision: BunnyHead <-> Gold Coins
        for (GoldCoin goldCoin: level.goldCoins){
            if(goldCoin.collected) continue;
            r2.set(goldCoin.position.x, goldCoin.position.y, goldCoin.bounds.width, goldCoin.bounds.height);
            if(!r1.overlaps(r2)) continue;
            onCollisionWithGoldCoin(goldCoin);
            break;
        }

//        Test collision: BunnyHead <-> Feathers
        for (Feather feather: level.feathers){
            if(feather.collected) continue;
            r2.set(feather.position.x, feather.position.y, feather.bounds.width, feather.bounds.height);
            if(!r1.overlaps(r2)) continue;
            onCollisionWithFeather(feather);
            break;
        }

//        Test Collision: BunnyHead <-> Goal
        if(!goalReached) {
            r2.set(level.goal.bounds);
            r2.x += level.goal.position.x;
            r2.y += level.goal.position.y;
            if (r1.overlaps(r2)) onCollisionWithGoal();
        }


    }


    private void onCollisionWithRock(Rock rock){
        BunnyHead bunnyHead = level.bunnyHead;
        float heightDifference = Math.abs(bunnyHead.position.y - (rock.position.y + rock.bounds.height));

        if(heightDifference > 0.25f){
            boolean hitLeftEdge = bunnyHead.position.x > (rock.position.x + rock.bounds.width / 2.0f);
            if(hitLeftEdge){
                bunnyHead.position.x = rock.position.x + rock.bounds.width;
            } else {
                bunnyHead.position.x = rock.position.x - bunnyHead.bounds.width ;
            }
            return;
        }

        switch (bunnyHead.jump_state){
            case GROUNDED:
                break;
            case FALLING:
            case JUMP_FALLING:
                bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height + bunnyHead.origin.y;
                bunnyHead.jump_state = Constants.JUMP_STATE.GROUNDED;
                break;
            case JUMP_RISING:
                bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height + bunnyHead.origin.y;
                break;
        }

    }

    private void onCollisionWithGoldCoin(GoldCoin goldCoin){
        goldCoin.collected = true;
        AudioManger.getInstance().play(assets.sounds.pickUpCoin);
        score += goldCoin.getScore();
        Gdx.app.log(TAG, "Gold coin collected");

    }

    private void onCollisionWithFeather(Feather feather){
        feather.collected = true;
        score += feather.getScore();
        AudioManger.getInstance().play(assets.sounds.pickUpFeather);
        level.bunnyHead.setFeatherPowerup(true);
        Gdx.app.log(TAG, "Feather collected");

    }

    private void onCollisionWithGoal() {
        goalReached = true;
        timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_FINISHED;
        Vector2 centerPosBunnyHead = new Vector2(level.bunnyHead.position);
        centerPosBunnyHead.x += level.bunnyHead.bounds.width;
        spawnCarrots(centerPosBunnyHead, Constants.CARROT_SPAWN_MAX, Constants.CARROTS_SPAWN_RADIUS);

    }

    private void spawnCarrots (Vector2 pos, int numCarrots, float radius){
        float carrotShapeScale = 0.5f;
//        Create carrots with box2d body and fixture
        for (int i = 0;i < numCarrots; i++){
            Carrot carrot = new Carrot();
//            Calculate random spawn points, rotation and scale
            float x = MathUtils.random(-radius, radius);
            float y = MathUtils.random(5.0f, 15.0f);
            float rotation = MathUtils.random(0.0f, 360.f) * MathUtils.radiansToDegrees;
            float carrotScale = MathUtils.random(0.5f, 1.5f);
            carrot.scale.set(carrotScale, carrotScale);
//            Create box2d body for carrot with start position
//            and angle of rotation
            BodyDef bodyDef = new BodyDef();
            bodyDef.position.set(pos);
            bodyDef.position.add(x, y);
            bodyDef.angle = rotation;
            Body body = world.createBody(bodyDef);
            body.setType(BodyDef.BodyType.DynamicBody);
            carrot.body = body;
//            Create a rectangular shape for carrot to allow interactions with other objects
            PolygonShape polygonShape = new PolygonShape();
            float halfWidth = carrot.bounds.width / 2.0f * carrotScale;
            float halfHeight = carrot.bounds.height / 2.0f * carrotScale;
            polygonShape.setAsBox(halfWidth * carrotShapeScale, halfHeight * carrotShapeScale);
//            Set physics attribute
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = polygonShape;
            fixtureDef.density = 50;
            fixtureDef.restitution = 0.5f;
            fixtureDef.friction = 0.5f;
            body.createFixture(fixtureDef);
            polygonShape.dispose();
            level.carrots.add(carrot);
        }
    }



    @Override
    public void dispose() {
        if(world != null) world.dispose();
    }
}
