package com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.monster;

import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.GameObject;
import com.samuelschwenn.game_logic.util.CoordsInt;

public class Lakai extends Monster{
    @Override
    protected GameObject build(CoordsInt position) {
        setStrength(10);
        setHealth(20);
        setPosition(position);
        setMovingSpeed(4);
        setAttackSpeed(3);
        setBounty(20);
        setFlying(true);
        setImagePath("Lakai.png");
        return this;
    }

    @Override
    public void updateMonsterPath() {
        updateFlyingMonsterPath();
    }
}
