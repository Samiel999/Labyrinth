package com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.buildings.basis;

import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.ObjectType;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.buildings.Building;
import com.samuelschwenn.game_logic.util.CoordsInt;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.io.Serializable;

import static com.samuelschwenn.Main.loop;
import static com.samuelschwenn.game_logic.util.LoopType.game_over;


@NoArgsConstructor
public abstract class Basis extends Building implements Serializable {
    public Basis(int strength, int health, CoordsInt position, ObjectType type, Image image){
        super(strength, health, position, type, 0, image);
    }

    @Override
    public void die() {
        loop.update(game_over);
    }
}
