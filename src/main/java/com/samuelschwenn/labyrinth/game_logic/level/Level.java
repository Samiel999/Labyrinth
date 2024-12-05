package com.samuelschwenn.labyrinth.game_logic.level;

import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.buildings.basis.Basis;
import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.monster.Monster;
import com.samuelschwenn.labyrinth.game_logic.util.CoordsInt;
import lombok.Getter;
import org.javatuples.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.GameObject.instantiate;
import static com.samuelschwenn.labyrinth.game_logic.level.DEFAULT_LEVEL.*;

public abstract class Level implements Serializable {
    @Getter
    protected List<Monster> monstersToSpawn;
    @Getter
    protected long spawnTime = DEFAULT_SPAWN_TIME;
    @Getter
    protected int width = DEFAULT_WIDTH;
    @Getter
    protected int height = DEFAULT_HEIGHT;
    @Getter
    protected CoordsInt basisPosition = DEFAULT_BASIS_POSITION;
    @Getter
    protected Basis basis;
    @Getter
    protected double startKapital = DEFAULT_START_KAPITAL;
    @Getter
    protected int anzahlMauern = DEFAULT_ANZAHL_MAUERN;

    protected boolean spawnAtPoint = true;
    @Getter
    protected CoordsInt spawnPoint;
    @Getter
    protected List<Pair<CoordsInt, CoordsInt>> spawnArea;

    public Level(Basis basis){
        this.basis = basis;
        monstersToSpawn = new ArrayList<>();
    }

    public void addSpawnArea(Pair<CoordsInt, CoordsInt> spawnArea) {
        if(spawnAtPoint && spawnPoint != null){
            return;
        }
        if(this.spawnArea == null){
            this.spawnArea = new ArrayList<>();
        }
        this.spawnArea.add(spawnArea);
        spawnAtPoint = false;
    }

    public void addSpawnPoint(CoordsInt spawnPoint) {
        if(!spawnAtPoint){
            return;
        }
        this.spawnPoint = spawnPoint;
    }

    public boolean spawnAtPoint() {
        return spawnAtPoint;
    }

    public void addNumberOfMonstersToSpawnList(int count, Class<? extends Monster> monsterClass) {
        for(int i = 0; i < count; i++) {
            Monster monster = (Monster) instantiate(monsterClass, new CoordsInt(-1, -1));
            monstersToSpawn.add(monster);
        }
    }

}
