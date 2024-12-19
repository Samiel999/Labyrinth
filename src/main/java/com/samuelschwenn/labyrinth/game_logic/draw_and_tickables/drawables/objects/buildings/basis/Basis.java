package com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.buildings.basis;

import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.buildings.Building;

import java.io.Serializable;

import static com.samuelschwenn.labyrinth.LabyrinthApplication.loop;
import static com.samuelschwenn.labyrinth.game_logic.util.LoopType.game_over;


public abstract class Basis extends Building implements Serializable {
    @Override
    public void die() {
        loop.update(game_over);
    }
}
