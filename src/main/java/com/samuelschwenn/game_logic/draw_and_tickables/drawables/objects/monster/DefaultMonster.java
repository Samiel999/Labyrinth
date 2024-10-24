package com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.monster;

import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.GameObject;
import com.samuelschwenn.game_logic.util.CoordsInt;

public class DefaultMonster extends Monster{
    @Override
    protected GameObject build(CoordsInt position) {
        setStrength(10);
        setHealth(20);
        setPosition(position);
        setMovingSpeed(2);
        setAttackSpeed(4);
        setBounty(8);
        setFlying(false);
        setImagePath("DefaultMonster.png");
        return this;
    }

    @Override
    public void updateMonsterPath() {
        updateWalkingMonsterPath();
    }
}
