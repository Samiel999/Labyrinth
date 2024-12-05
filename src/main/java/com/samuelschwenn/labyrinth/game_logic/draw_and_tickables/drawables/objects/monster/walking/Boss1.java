package com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.monster.walking;

import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.GameObject;
import com.samuelschwenn.labyrinth.game_logic.util.CoordsInt;

public class Boss1 extends WalkingMonster {
    @Override
    protected GameObject build(CoordsInt position) {
        setStrength(30);
        setHealth(140);
        setPosition(position);
        setMovingSpeed(1.5f);
        setAttackSpeed(3);
        setBounty(100);
        setImagePath("Boss1.png");
        return this;
    }
}
