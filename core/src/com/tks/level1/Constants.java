package com.tks.level1;

import com.badlogic.gdx.graphics.Color;

interface Constants {
    float WORLD_SIZE = 5.0f;
    Color BACKGROUND_COLOR = new Color(0x64/255.0f, 0x95/255.0f, 0xed/255.0f, 0xff/255.0f);
    String TEXTURE_ATLAS_OBJECT = "images/canyonbunny.pack";



    //    GUI Width
    float   VIEWPORT_GUI_WIDTH = 800.0f;
//    GUI Height
    float   VIEWPORT_GUI_HEIGHT = 480.0f;

//    Amount of extra lives at level start
    int LIVES_START = 3;

    int NUMBER_OF_LEVELS = 3;
    int INITIAL_LEVEL = 0;
    String[] LEVEL = new String[]{"levels/level-001.png", "levels/level-002.png", "levels/level-003.png"};


//    Constants for Bunny Head

    float MAX_ZOOM_IN = 0.25f;
    float MAX_ZOOM_OUT = 10.0f;
    float ITEM_FEATHER_POWERUP_DURATION = 9.0f;


    float JUMP_TIME_MAX = 0.3f;
    float JUMP_TIME_MIN = 0.1f;
    float JUMP_TIME_OFFSET_FLYING = JUMP_TIME_MAX - 0.018f;

    enum VIEW_DIRECTION {
        LEFT,
        RIGHT
    }

    enum JUMP_STATE{
        GROUNDED, FALLING, JUMP_RISING, JUMP_FALLING
    }

//    Delay after game over
    float TIME_DELAY_GAME_OVER = 3;


//    Menu Screen
    String SKIN_CANYONBUNNY_UI = "images/canyonbunny-ui.json";
    String TEXTURE_ATLAS_UI = "images/canyonbunny-ui.pack";
    String PREFERENCES = "canyonbunny.prefs";

    String SKIN_LIBGDX_UI = "images/uiskin.json";
    String TEXTURE_ATLAS_LIBGDX_UI = "images/uiskin.atlas";


    float ACCEL_MAX_ANGLE_MAX_MOVEMENT = 5.0f;

    int CARROT_SPAWN_MAX = 100;

    float CARROTS_SPAWN_RADIUS = 3.5f;

    float TIME_DELAY_GAME_FINISHED = 6;


}
