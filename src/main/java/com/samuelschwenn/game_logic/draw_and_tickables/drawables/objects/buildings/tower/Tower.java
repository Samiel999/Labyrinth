package com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.buildings.tower;

import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.ObjectType;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.buildings.Building;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.monster.Monster;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.shots.classicshots.DefaultShot;
import com.samuelschwenn.game_logic.draw_and_tickables.Tickable;
import com.samuelschwenn.game_logic.LogicRepresentation;
import com.samuelschwenn.game_logic.util.CoordsInt;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.samuelschwenn.Main.loop;
import static java.util.Collections.min;

@NoArgsConstructor
public abstract class Tower extends Building implements Tickable {
    protected int reach;
    @SuppressWarnings("CanBeFinal")
    protected int attackSpeed;
    protected double timeTilShoot = attackSpeed;

    private Monster[] shotMonsters = new Monster[0];

    public Tower(int strength, int health, CoordsInt position, int reach, int speed, double kosten, ObjectType type, Image image) {
        super(strength, health, position, type, kosten, image);
        attackSpeed = speed;
        this.reach = reach;
        loop.registerTickable(this);
    }

    @Override
    public void tick(double timeDelta, LogicRepresentation logicRepresentation) {
        if(isBlueprint) return;
        timeTilShoot = Math.max(0, timeTilShoot - timeDelta);
        if(timeTilShoot == 0){
            shotMonsters = shoot(logicRepresentation);
            if(shotMonsters[0] != null){
                timeTilShoot = attackSpeed;
            }
        }
    }

    public abstract Monster[] shoot(LogicRepresentation logicRepresentation);

    public Monster shootNormal(LogicRepresentation logicRepresentation) {
        List<Monster> monsters = logicRepresentation.getMonsterList();
        List<Monster> shootingMonsters = new ArrayList<>();
        for (Monster monster: monsters) {
            if(position.isInRange(reach, monster.getPosition())){
                shootingMonsters.add(monster);
            }
        }
        if(!shootingMonsters.isEmpty()) {
            HashMap<Integer, Monster> monsterMap = new HashMap<>();
            for(Monster monster : shootingMonsters){
                monsterMap.put(monster.getStepsToGoal(), monster);
            }
            int minimum = min(monsterMap.keySet());
            Monster monsterToShoot = monsterMap.get(minimum);
            monsterToShoot.setHealth(monsterToShoot.getHealth() - strength);
            return monsterToShoot;
        }
        return null;
    }

    @Override
    public void die() {
        super.die();
        loop.unregisterTickable(this);
    }

    @Override
    public void draw(Graphics g){
        super.draw(g);
        for(Monster monster : shotMonsters){
            if(monster == null){
                continue;
            }
            new DefaultShot(this.getDrawnPosition(), monster.getDrawnPosition());
            shotMonsters = new Monster[0];
        }
    }
}
