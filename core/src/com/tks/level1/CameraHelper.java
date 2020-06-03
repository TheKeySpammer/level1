package com.tks.level1;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

class CameraHelper {

    private float zoom;
    private final Vector2 position;
    private AbstractGameObject target;
    private final float FOLLOW_SPEED = 4.0f;

    CameraHelper(){
        zoom = 1.2f;
//        zoom = 1.0f;
        position = new Vector2(2, 0.5f);
//        position = new Vector2(0, 0);
    }

    void update (float deltaTime) {
        if (!hasTarget()) return;

        position.lerp(new Vector2(target.position.x + 0.15f, target.position.y), FOLLOW_SPEED * deltaTime);

//        Prevent camera from moving down too far
        position.y = Math.max(-0.75f, position.y);
    }
    void setPosition (float x, float y) {
        this.position.set(x, y);
    }
    Vector2 getPosition () { return position; }
    void addZoom (float amount) { setZoom(zoom + amount); }
    void setZoom (float zoom) {
        this.zoom = MathUtils.clamp(zoom,Constants.MAX_ZOOM_IN, Constants.MAX_ZOOM_OUT);
    }
    float getZoom () { return zoom; }
    void setTarget (AbstractGameObject target) { this.target = target; }
    AbstractGameObject getTarget () { return target; }
    boolean hasTarget () { return target != null; }
    boolean hasTarget (AbstractGameObject target) {
        return hasTarget() && this.target.equals(target);
    }

    void applyTo(OrthographicCamera camera) {
        camera.position.x = position.x;
        camera.position.y = position.y;
        camera.zoom = zoom;
        camera.update();
    }
}

