package com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.buildings;

import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.ObjectType;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.Objekt;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.monster.Monster;
import com.samuelschwenn.game_logic.util.CoordsDouble;
import com.samuelschwenn.game_logic.util.CoordsInt;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.samuelschwenn.Main.loop;


@NoArgsConstructor
public abstract class Building extends Objekt {
    @Getter
    protected double cost;
    protected boolean isBlueprint;
    public Building(int pStrength, int pHealth, CoordsInt position, ObjectType type, double cost) {
        super(pStrength, pHealth, position, type);
        this.cost = cost;
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
        loop.getLogic_representation().removeBuilding(position);
        List<Monster> monsters = loop.getLogic_representation().getMonsterList();
        for (Monster monster : monsters){
            monster.updateMonsterPath(loop.getLogic_representation());
        }
    }
}
