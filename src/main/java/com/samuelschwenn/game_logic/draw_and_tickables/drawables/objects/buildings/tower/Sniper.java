package com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.buildings.tower;

import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.GameObject;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.monster.Monster;
import com.samuelschwenn.game_logic.util.CoordsInt;

public class Sniper extends Tower {
    @Override
    protected GameObject build(CoordsInt position) {
        setStrength(20);
        setHealth(25);
        setPosition(position);
        setReach(8);
        setAttackSpeed(10);
        setCost(50);
        setImagePath("Sniper.png");
        return this;
    }

    @Override
    public Monster[] shoot() {
        return shootNormal();
    }
}
