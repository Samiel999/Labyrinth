package com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.buildings;

import com.samuelschwenn.game_logic.LogicRepresentation;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.GameObject;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.monster.Monster;
import com.samuelschwenn.game_logic.util.CoordsDouble;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public abstract class Building extends GameObject {
    @Getter
    @Setter
    protected double cost;
    protected boolean isBlueprint;
    public Building() {
        this.isBlueprint = false;
    }

    public void setBlueprint(boolean blueprint){
        this.isBlueprint = blueprint;
    }

    @Override
    public CoordsDouble getDrawnPosition() {
        return position.toCoordsDouble();
    }

    @Override
    public float getOpacity() {
        return isBlueprint? .5f : 1.0f;
    }

    @Override
    public void die() {
        super.die();
        LogicRepresentation.getInstance().removeBuilding(position);
        List<Monster> monsters = LogicRepresentation.getInstance().getMonsterList();
        for (Monster monster : monsters){
            monster.updateMonsterPath();
        }
    }
}
