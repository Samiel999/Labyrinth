package com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.buildings;

import com.samuelschwenn.game_logic.LogicRepresentation;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.GameObject;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.monster.Monster;
import com.samuelschwenn.game_logic.util.CoordsDouble;
import com.samuelschwenn.game_logic.util.CoordsInt;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Building extends GameObject {
    @Getter
    @Setter
    protected double cost;
    protected boolean isBlueprint;
    private static final Map<Class<? extends Building>,Building> instances = new HashMap<>();

    public Building() {
        this.isBlueprint = false;
    }

    public static Image getStaticImageOf(Class<? extends Building> buildingClass) {
        if(instances.get(buildingClass) == null){
            Building building = (Building) instantiate(buildingClass, new CoordsInt(-1, -1));
            building.isBlueprint = true;
            instances.put(buildingClass, building);
        }
        return instances.get(buildingClass).getImage();
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
