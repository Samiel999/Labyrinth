package com.samuelschwenn.labyrinth.game_app.visuals.register;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ImageRegister {
    private final Map<String, Image> register = new HashMap<>();
    private static ImageRegister instance;

    public static ImageRegister getInstance() {
        if (instance == null) {
            instance = new ImageRegister();
        }
        return instance;
    }

    public Image getFromRegister(String key) {
        return register.get(key);
    }

    public void addToRegister(String key, Image value) {
        register.put(key, value);
    }
}
