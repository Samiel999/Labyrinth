package com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.monster.flying;

import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.GameObject;
import com.samuelschwenn.labyrinth.game_logic.util.CoordsInt;

public class BombCarrier extends FlyingMonster {
    @Override
    protected GameObject build(CoordsInt position) {
        setStrength(30);
        setHealth(35);
        setPosition(position);
        setMovingSpeed(1);
        setAttackSpeed(1);
        setBounty(40);
        setImagePath("BombCarrier.png");
        return this;
    }
}
