package com.samuelschwenn.drawables.objects.buildings.basis;

import com.samuelschwenn.drawables.objects.ObjectType;
import com.samuelschwenn.drawables.objects.buildings.Building;
import com.samuelschwenn.util.CoordsInt;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static com.samuelschwenn.Main.loop;
import static com.samuelschwenn.logic.LoopType.game_over;


@NoArgsConstructor
public abstract class Basis extends Building implements Serializable {
    public Basis(int strength, int health, CoordsInt position, ObjectType type){
        super(strength, health, position, type, 0);
    }

    @Override
    public void die() {
        loop.update(game_over);
    }
}