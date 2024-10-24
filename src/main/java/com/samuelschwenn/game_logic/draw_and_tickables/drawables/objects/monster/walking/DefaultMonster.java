package com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.monster.walking;

import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.GameObject;
import com.samuelschwenn.game_logic.util.CoordsInt;

public class DefaultMonster extends WalkingMonster {
    @Override
    protected GameObject build(CoordsInt position) {
        setStrength(10);
        setHealth(20);
        setPosition(position);
        setMovingSpeed(2);
        setAttackSpeed(4);
        setBounty(8);
        setImagePath("DefaultMonster.png");
        return this;
    }
}
