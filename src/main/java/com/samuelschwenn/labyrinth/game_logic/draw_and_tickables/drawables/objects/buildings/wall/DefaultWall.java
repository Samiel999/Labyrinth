package com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.buildings.wall;

import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.GameObject;
import com.samuelschwenn.labyrinth.game_logic.util.CoordsInt;

public class DefaultWall extends Wall {
    @Override
    protected GameObject build(CoordsInt position) {
        setHealth(10);
        setPosition(position);
        setCost(0);
        setImagePath("DefaultWall.png");
        return this;
    }
}
