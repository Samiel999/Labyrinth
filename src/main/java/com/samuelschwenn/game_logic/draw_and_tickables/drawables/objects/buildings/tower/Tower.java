package com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.buildings.tower;

import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.buildings.Building;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.monster.Monster;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.shots.classicshots.DefaultShot;
import com.samuelschwenn.game_logic.draw_and_tickables.Tickable;
import com.samuelschwenn.game_logic.LogicRepresentation;
import lombok.Setter;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.samuelschwenn.Main.loop;
import static java.util.Collections.min;

public abstract class Tower extends Building implements Tickable {
    @Setter
    protected int reach;
    @SuppressWarnings("CanBeFinal")
    @Setter
    protected int attackSpeed;
    protected double timeTilShoot = attackSpeed;

    private Monster[] shotMonsters = new Monster[1];

    public Tower() {
        loop.registerTickable(this);
    }

    @Override
    public void tick(double timeDelta) {
        if(isBlueprint) return;
        timeTilShoot = Math.max(0, timeTilShoot - timeDelta);
        if(timeTilShoot == 0){
            shotMonsters = shoot();
            if(shotMonsters[0] != null){
                timeTilShoot = attackSpeed;
            }
        }
    }

    public abstract Monster[] shoot();

    public Monster[] shootNormal() {
        List<Monster> monsters = LogicRepresentation.getInstance().getMonsterList();
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
            return new Monster[]{monsterToShoot};
        }
        return new Monster[1];
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
