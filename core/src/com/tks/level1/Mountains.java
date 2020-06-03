package com.tks.level1;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

class Mountains extends AbstractGameObject {

    private TextureRegion mountainleft;
    private TextureRegion mountainright;
    private int length;
    private final Assets assets;

    Mountains(int length){
        this.length = length;
        assets = Assets.getInstance();
        init();
    }

    private void init(){
        dimension.set(10, 2);
        mountainleft = assets.levelDecoration.mountain_left;
        mountainright = assets.levelDecoration.mountain_right;

//        Shift mountains and extend mountains.

        origin.x = -dimension.x * 2;

        length += dimension.x * 2;
    }

    private void drawMountain(SpriteBatch batch, float offsetX, float offsetY, float tintColor, float parallaxSpeedX){
        TextureRegion reg;
        batch.setColor(tintColor, tintColor, tintColor, 1);

        float relX = dimension.x * offsetX;
        float relY = dimension.y * offsetY;

//        Mountain span the entire map
        int mountainLength = 0;
        mountainLength += MathUtils.ceil(length / (2*dimension.x) * (1 - parallaxSpeedX));
        mountainLength += MathUtils.ceil(0.5f + offsetX);
        for (int i = 0; i < mountainLength; i++){
//            mountain left
            reg = mountainleft;
            batch.draw(reg.getTexture(), origin.x + relX + position.x * parallaxSpeedX, position.y + origin.y + relY, origin.x, origin.y,
                    dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),reg.getRegionHeight(), false, false);

            relX += dimension.x;

//            Mountain right
            reg = mountainright;
            batch.draw(reg.getTexture(), origin.x + relX + position.x * parallaxSpeedX, position.y + origin.y + relY, origin.x, origin.y, dimension.x, dimension.y,
                    scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
            relX += dimension.x;
        }
//        Reset Color to white
        batch.setColor(1, 1, 1, 1);
    }



    @Override
    void render(SpriteBatch batch) {
//        Distant Mountain (dark grey)
        drawMountain(batch, 0.5f, 0.5f, 0.5f, 0.8f);
//        Distant mountain (grey)
        drawMountain(batch, 0.25f, 0.25f, 0.7f, 0.5f);
//        distant mountain (light grey)
        drawMountain(batch, 0.0f, 0.0f ,0.9f, 0.3f);
    }

    public void updateScrollPosition (Vector2 camPosition) {
        position.set(camPosition.x, position.y);
    }
}
