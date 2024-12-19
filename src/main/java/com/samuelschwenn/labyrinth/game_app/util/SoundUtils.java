package com.samuelschwenn.labyrinth.game_app.util;

import com.samuelschwenn.labyrinth.LabyrinthApplication;
import com.samuelschwenn.labyrinth.game_app.visuals.main_menu.Settings;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public final class SoundUtils {
    public static void setVolume(float volume, Clip clip) {
        if (clip == null) {
            return; // Exit if clip is not initialized
        }
        if (volume < 0f || volume > 1f) {
            throw new IllegalArgumentException("Volume not valid: " + volume);
        }
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(volume));
    }

    public static void playMusic(int i) {
        if (!Settings.musicMuted) {
            LabyrinthApplication.sound.setFile(i);
            LabyrinthApplication.sound.play();
            LabyrinthApplication.sound.loop();
        }
    }

    public static void stopMusic() {
        try {LabyrinthApplication.sound.stop();}
        catch(Exception e) {System.out.println("Nope");}
    }

    public static void playSFX(int i) {
        if (!Settings.soundmute) {
            LabyrinthApplication.sfx.setFile(i);
            LabyrinthApplication.sfx.play();
        }
    }
}
