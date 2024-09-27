package com.samuelschwenn.game_logic.draw_and_tickables;

import java.awt.*;

public interface Drawable {
    float getOpacity();
    default void draw(Graphics g){
    }
}
