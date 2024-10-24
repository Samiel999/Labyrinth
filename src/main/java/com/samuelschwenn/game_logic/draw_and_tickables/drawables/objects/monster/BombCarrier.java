package com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.monster;

import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.GameObject;
import com.samuelschwenn.game_logic.util.CoordsInt;

public class BombCarrier extends Monster {
    @Override
    protected GameObject build(CoordsInt position) {
        setStrength(30);
        setHealth(35);
        setPosition(position);
        setMovingSpeed(1);
        setAttackSpeed(1);
        setBounty(40);
        setFlying(true);
        setImagePath("BombCarrier.png");
        return this;
    }

    @Override
    public void updateMonsterPath() {
        updateFlyingMonsterPath();
    }
}
