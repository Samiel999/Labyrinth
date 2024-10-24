package com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.buildings.tower;

import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.GameObject;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.monster.Monster;
import com.samuelschwenn.game_logic.util.CoordsInt;

public class Minigun extends Tower {

    @Override
    protected GameObject build(CoordsInt position) {
        setStrength(2);
        setHealth(25);
        setPosition(position);
        setReach(5);
        setAttackSpeed(2);
        setCost(40);
        setImagePath("Minigun.png");
        return this;
    }

    @Override
    public Monster[] shoot() {
        return shootNormal();
    }
}
