package com.samuelschwenn.labyrinth.game_app.sounds;

import com.samuelschwenn.labyrinth.game_app.util.SoundUtils;

import static com.samuelschwenn.labyrinth.game_app.util.SoundUtils.playMusic;

public class MusicController {
    private static MusicController instance;

    public boolean musicPlaying = false;

    private MusicController() {
    }

    public static MusicController getInstance() {
        if (instance == null) {
            instance = new MusicController();
        }
        return instance;
    }

    public void playMainMenuMusic(){
        if(!musicPlaying) {
            playMusic(3);
            musicPlaying = true;
        }
    }

    public void playGameNotStartedMusic(){
        playMusic(2);
        musicPlaying = true;
    }

    public void stopMusic(){
        SoundUtils.stopMusic();
        musicPlaying = false;
    }
}
