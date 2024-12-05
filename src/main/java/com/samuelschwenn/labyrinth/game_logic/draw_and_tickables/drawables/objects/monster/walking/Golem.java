package com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.monster.walking;

import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.GameObject;
import com.samuelschwenn.labyrinth.game_logic.util.CoordsInt;

public class Golem extends WalkingMonster {
    @Override
    protected GameObject build(CoordsInt position) {
        setStrength(20);
        setHealth(60);
        setPosition(position);
        setMovingSpeed(1);
        setAttackSpeed(1);
        setBounty(20);
        setImagePath("Golem.png");
        return this;
    }
}
