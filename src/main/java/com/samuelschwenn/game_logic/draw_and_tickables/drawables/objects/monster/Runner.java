package com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.monster;

import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.GameObject;
import com.samuelschwenn.game_logic.util.CoordsInt;

public class Runner extends Monster {
    @Override
    protected GameObject build(CoordsInt position) {
        setStrength(15);
        setHealth(10);
        setPosition(position);
        setMovingSpeed(6);
        setAttackSpeed(4);
        setBounty(5);
        setFlying(false);
        setImagePath("Runner.png");
        return this;
    }

    @Override
    public void updateMonsterPath() {
        updateWalkingMonsterPath();
    }
}
