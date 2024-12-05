package com.samuelschwenn.labyrinth.game_logic.level;

import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.buildings.basis.Basis;
import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.monster.walking.DefaultMonster;
import com.samuelschwenn.labyrinth.game_logic.util.CoordsInt;
import org.javatuples.Pair;

public class Level1 extends Level{

    public Level1(Basis basis){
        super(basis);
        basis.setPosition(basisPosition);
        basis.setHealth(basis.getMaxHealth());
        addSpawnArea(new Pair<>(new CoordsInt(0, 0), new CoordsInt(0, height - 1)));
        addSpawnArea(new Pair<>(new CoordsInt(0, height - 1), new CoordsInt(width - 1, height - 1)));
        addSpawnArea(new Pair<>(new CoordsInt(0, 0), new CoordsInt(width - 1, 0)));
        addSpawnArea(new Pair<>(new CoordsInt(width - 1, 0), new CoordsInt(width - 1, height - 1)));

        addNumberOfMonstersToSpawnList(15, DefaultMonster.class);
    }
}
