package com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.shots.classic_shots;

import com.samuelschwenn.labyrinth.game_logic.util.CoordsDouble;

import java.awt.*;

public class DefaultShot extends ClassicShot {
    public DefaultShot(CoordsDouble startingPosition, CoordsDouble targetPosition) {
        super(Color.RED, 3, 0.5, startingPosition, targetPosition, 1);
    }
}
