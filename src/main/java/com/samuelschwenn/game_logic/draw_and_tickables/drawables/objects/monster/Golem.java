package com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.monster;

import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.GameObject;
import com.samuelschwenn.game_logic.util.CoordsInt;

public class Golem extends Monster{
    @Override
    protected GameObject build(CoordsInt position) {
        setStrength(20);
        setHealth(60);
        setPosition(position);
        setMovingSpeed(1);
        setAttackSpeed(1);
        setBounty(20);
        setFlying(false);
        setImagePath("Golem.png");
        return this;
    }

    @Override
    public void updateMonsterPath() {
        updateWalkingMonsterPath();
    }
}
