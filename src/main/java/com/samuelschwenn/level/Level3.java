package com.samuelschwenn.level;

import com.samuelschwenn.drawables.objects.buildings.basis.Basis;
import com.samuelschwenn.drawables.objects.monster.DefaultMonster;
import com.samuelschwenn.drawables.objects.monster.Golem;
import com.samuelschwenn.drawables.objects.monster.Lakai;
import com.samuelschwenn.drawables.objects.monster.Runner;
import com.samuelschwenn.util.CoordsInt;
import org.javatuples.Pair;
import java.util.ArrayList;

public class Level3 extends Level{
    public Level3(Basis basis){
        super(
                3,
                39,
                21,
                new CoordsInt(19, 9),
                basis,
                new ArrayList<>(),
                80,
                30
        );
        addSpawnArea(new Pair<>(new CoordsInt(0, 0), new CoordsInt(0, height - 1)));
        addSpawnArea(new Pair<>(new CoordsInt(0, height - 1), new CoordsInt(width - 1, height - 1)));
        addSpawnArea(new Pair<>(new CoordsInt(0, 0), new CoordsInt(width - 1, 0)));
        addSpawnArea(new Pair<>(new CoordsInt(width - 1, 0), new CoordsInt(width - 1, height - 1)));
        monstersToSpawn.add(new DefaultMonster(new CoordsInt(-1, -1)));
        monstersToSpawn.add(new DefaultMonster(new CoordsInt(-1, -1)));
        monstersToSpawn.add(new DefaultMonster(new CoordsInt(-1, -1)));
        monstersToSpawn.add(new DefaultMonster(new CoordsInt(-1, -1)));
        monstersToSpawn.add(new DefaultMonster(new CoordsInt(-1, -1)));
        monstersToSpawn.add(new DefaultMonster(new CoordsInt(-1, -1)));
        monstersToSpawn.add(new DefaultMonster(new CoordsInt(-1, -1)));
        monstersToSpawn.add(new DefaultMonster(new CoordsInt(-1, -1)));
        monstersToSpawn.add(new DefaultMonster(new CoordsInt(-1, -1)));
        monstersToSpawn.add(new DefaultMonster(new CoordsInt(-1, -1)));
        monstersToSpawn.add(new DefaultMonster(new CoordsInt(-1, -1)));
        monstersToSpawn.add(new Golem(new CoordsInt(-1, -1)));
        monstersToSpawn.add(new Golem(new CoordsInt(-1, -1)));
        monstersToSpawn.add(new Lakai(new CoordsInt(-1, -1)));
        monstersToSpawn.add(new Lakai(new CoordsInt(-1, -1)));
        monstersToSpawn.add(new Lakai(new CoordsInt(-1, -1)));
        monstersToSpawn.add(new Runner(new CoordsInt(-1, -1)));
        monstersToSpawn.add(new Runner(new CoordsInt(-1, -1)));
        monstersToSpawn.add(new Runner(new CoordsInt(-1, -1)));
        monstersToSpawn.add(new Runner(new CoordsInt(-1, -1)));
        monstersToSpawn.add(new Runner(new CoordsInt(-1, -1)));
        monstersToSpawn.add(new Runner(new CoordsInt(-1, -1)));
        basis.setHealth(basis.getMaxHealth());
        basis.setPosition(new CoordsInt(19, 9));
    }
}
