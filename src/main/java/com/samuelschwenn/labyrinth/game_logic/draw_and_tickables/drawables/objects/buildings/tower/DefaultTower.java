package com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.buildings.tower;

import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.GameObject;
import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.monster.Monster;
import com.samuelschwenn.labyrinth.game_logic.util.CoordsInt;

public class DefaultTower extends Tower {
    @Override
    protected GameObject build(CoordsInt position) {
        setStrength(5);
        setHealth(25);
        setPosition(position);
        setReach(4);
        setAttackSpeed(6);
        setCost(20);
        setImagePath("DefaultTower.png");
        return this;
    }

    @Override
    public Monster[] shoot() {
        return shootOneMonster();
    }
}
