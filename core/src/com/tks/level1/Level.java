package com.tks.level1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

class Level {

    private static final String TAG = Level.class.getName();

    enum BLOCK_TYPE {
        EMPTY(0, 0, 0), // black
        ROCK(0, 255, 0), // green
        PLAYER_SPAWNPOINT(255, 255, 255), // white
        ITEM_FEATHER(255, 0, 255), // purple
        ITEM_GOLD_COIN(255, 255, 0), // yellow
        GOAL (255, 0,0); // red

        private int color;

        BLOCK_TYPE(int r, int g, int b){
            color = r << 24 | g << 16 | b << 8 | 0xff;
        }

        boolean sameColor(int color){
            return this.color == color;
        }

        int getColor(){
            return color;
        }
    }

//  objects

    Array<Rock> rocks;
    BunnyHead bunnyHead;
    Array<GoldCoin> goldCoins;
    Array<Feather> feathers;
    Array<Carrot> carrots;
    Goal goal;

//    decoration

    Clouds clouds;
    Mountains mountains;
    WaterOverlay waterOverlay;

    Level(String filename){
        init(filename);
    }

    private void init(String filename){
//        Objects
        rocks = new Array<Rock>();
        goldCoins = new Array<GoldCoin>();
        feathers = new Array<Feather>();
        carrots = new Array<Carrot>();

//        Player Character

        bunnyHead = null;


//        Load image file that represents data
        Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
//        Scan pixels from top-left to bottom-right
        int lastPixel = -1;
        for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++){
            for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++){
                AbstractGameObject object;
                float offsetHeight = 0;
//                Height grows from bottom to top
                float baseHeight = pixmap.getHeight() - pixelY;
//                get color of current pixel as 32-bit RGBA value
                int currentPixel = pixmap.getPixel(pixelX, pixelY);
//                find matching color value to identify block type at (x, y)
//                point and create the corresponding game object if there is a match.
//                empty space
                if(BLOCK_TYPE.EMPTY.sameColor(currentPixel)){
//                    do nothing
                }
//                rock
                else if(BLOCK_TYPE.ROCK.sameColor(currentPixel)){

                    if(lastPixel != currentPixel){
                        object = new Rock();
                        float heightIncreaseFactor = 0.25f;
                        offsetHeight = -2.5f;
                        object.position.set(pixelX, baseHeight * object.dimension.y * heightIncreaseFactor + offsetHeight);
                        rocks.add((Rock) object);
                    } else{
                        rocks.get(rocks.size - 1).increaseLength(1);
                    }
                }
//                Player spawn point
                else if (BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel)){

                    object = new BunnyHead();
                    offsetHeight = -2.0f;
                    object.position.set(pixelX, baseHeight*object.dimension.y + offsetHeight);
                    bunnyHead = (BunnyHead) object;

                }
//                Feather
                else if(BLOCK_TYPE.ITEM_FEATHER.sameColor(currentPixel)){

                    object = new Feather();
                    offsetHeight = -1.5f;
                    object.position.set(pixelX, baseHeight * object.dimension.y + offsetHeight);
                    feathers.add((Feather)object);
                }
//                Gold Coin
                else if(BLOCK_TYPE.ITEM_GOLD_COIN.sameColor(currentPixel)){

                    object = new GoldCoin();
                    offsetHeight = -1.5f;
                    object.position.set(pixelX, baseHeight * object.dimension.y + offsetHeight);

                    goldCoins.add((GoldCoin)object);
                }
                else if(BLOCK_TYPE.GOAL.sameColor(currentPixel)){
                    object = new Goal();
                    offsetHeight = -7.0f;
                    object.position.set(pixelX, baseHeight + offsetHeight);
                    goal = (Goal)object;

                }
//                Unknown object / pixel color
                else{
                    int r = 0xff & (currentPixel >>> 24); // red color channel
                    int g = 0xff & (currentPixel >>> 16); // green color channel
                    int b = 0xff & (currentPixel >>> 8); // blue color channel
                    int a = 0xff & currentPixel; // alpha channel
                    Gdx.app.error(TAG, "Unknown object at x <" + pixelX + "> y <"
                            +pixelY +">: r<" + r + "> g<"+ g +"> b<" + b +"> a<"+a+">");
                }
                lastPixel = currentPixel;
            }
        }

//        Decoration
        clouds = new Clouds(pixmap.getWidth());
        clouds.position.set(0, 2);
        mountains = new Mountains(pixmap.getWidth());
        mountains.position.set(-1, -1);
        waterOverlay = new WaterOverlay(pixmap.getWidth());
        waterOverlay.position.set(0, -3.75f);
//        Free memory
        pixmap.dispose();
        Gdx.app.debug(TAG, "Level '" + filename + "' loaded");
    }
    public void render(SpriteBatch batch, ShapeRenderer renderer){
//        Draw Mountains
        mountains.render(batch);

//        Draw Goal
        goal.render(batch);

//        Draw Rocks
        for (Rock rock: rocks){
            rock.render(batch);
        }

//        Draw Gold Coins
        for (GoldCoin goldCoin: goldCoins)
            goldCoin.render(batch);

//        Draw Feathers
        for (Feather feather: feathers)
            feather.render(batch);

        for (Carrot carrot: carrots)
            carrot.render(batch);

//        Draw Player Character
        bunnyHead.render(batch);
        batch.end();

//        renderHitBox(renderer);

        batch.begin();


//        Draw WaterOverlay
        waterOverlay.render(batch);

//        Draw Clouds
        clouds.render(batch);
    }

    void update(float delta){
        bunnyHead.update(delta);
        for(Rock rock: rocks)
            rock.update(delta);
        for (GoldCoin goldCoin: goldCoins)
            goldCoin.update(delta);
        for (Feather feather: feathers)
            feather.update(delta);
        for (Carrot carrot: carrots)
            carrot.update(delta);
        clouds.update(delta);
    }

    void renderHitBox(ShapeRenderer renderer){
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.RED);
        renderer.rect(bunnyHead.bounds.getX(), bunnyHead.bounds.getY(), bunnyHead.bounds.width, bunnyHead.bounds.height);
        renderer.end();
    }

}
