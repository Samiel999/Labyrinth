package com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.buildings.basis;

import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.GameObject;
import com.samuelschwenn.labyrinth.game_logic.util.CoordsInt;

public class DefaultBasis extends Basis{
    @Override
    protected GameObject build(CoordsInt position) {
        setStrength(0);
        setHealth(200);
        setPosition(position);
        setImagePath("DefaultBasis.png");
        return this;
    }
}
