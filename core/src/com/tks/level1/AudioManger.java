package com.tks.level1;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

class AudioManger {
    private static AudioManger instance;

    private Music playingMusic;
    private AudioManger () {
    }

    void play (Sound sound){
        play(sound, 1);
    }

    void play (Sound sound, float volume){
        play(sound, volume, 1);
    }

    void play (Sound sound, float volume, float pitch){
        play(sound, volume, pitch, 0);
    }

    void play (Sound sound, float volume, float pitch, float pan){
        if (!GamePreferences.getInstance().sound) return;
        sound.play(GamePreferences.getInstance().volSound * volume, pitch, pan);
    }


    static AudioManger getInstance(){
        if (instance == null){
            instance = new AudioManger();
        }
        return instance;
    }

    void play (Music music){
        stopMusic();
        playingMusic = music;
        if (GamePreferences.getInstance().music){
            music.setLooping(true);
            music.setVolume(GamePreferences.getInstance().volMusic);
            music.play();
        }
    }

    void stopMusic() {
        if (playingMusic != null) playingMusic.stop();
    }

    void onSettingUpdated () {
        if (playingMusic == null) return;
        playingMusic.setVolume(GamePreferences.getInstance().volMusic);
        if (GamePreferences.getInstance().music){
            if(!playingMusic.isPlaying()) playingMusic.play();
        } else {
            playingMusic.pause();
        }
    }
}
