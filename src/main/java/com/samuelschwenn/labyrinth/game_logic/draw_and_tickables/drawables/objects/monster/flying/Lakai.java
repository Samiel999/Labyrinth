package com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.monster.flying;

import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.GameObject;
import com.samuelschwenn.labyrinth.game_logic.util.CoordsInt;

public class Lakai extends FlyingMonster {
    @Override
    protected GameObject build(CoordsInt position) {
        setStrength(10);
        setHealth(20);
        setPosition(position);
        setMovingSpeed(4);
        setAttackSpeed(3);
        setBounty(20);
        setImagePath("Lakai.png");
        return this;
    }
}
