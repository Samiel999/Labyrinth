package com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.monster.walking;

import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.GameObject;
import com.samuelschwenn.labyrinth.game_logic.util.CoordsInt;

public class Runner extends WalkingMonster {
    @Override
    protected GameObject build(CoordsInt position) {
        setStrength(15);
        setHealth(10);
        setPosition(position);
        setMovingSpeed(6);
        setAttackSpeed(4);
        setBounty(5);
        setImagePath("Runner.png");
        return this;
    }
}
