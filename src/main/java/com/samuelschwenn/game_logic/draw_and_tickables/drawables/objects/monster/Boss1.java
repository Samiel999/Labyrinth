package com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.monster;

import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.GameObject;
import com.samuelschwenn.game_logic.util.CoordsInt;

public class Boss1 extends Monster{
    @Override
    protected GameObject build(CoordsInt position) {
        setStrength(30);
        setHealth(140);
        setPosition(position);
        setMovingSpeed(1.5f);
        setAttackSpeed(3);
        setBounty(100);
        setFlying(false);
        setImagePath("Boss1.png");
        return this;
    }

    @Override
    public void updateMonsterPath() {
        updateWalkingMonsterPath();
    }
}
