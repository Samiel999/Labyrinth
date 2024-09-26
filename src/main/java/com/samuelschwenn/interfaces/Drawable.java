package com.samuelschwenn.interfaces;

import java.awt.*;

public interface Drawable {
    float getOpacity();
    default void draw(Graphics g){
    }
}
