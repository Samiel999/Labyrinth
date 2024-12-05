package com.samuelschwenn.labyrinth.game_logic.level;

import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.buildings.basis.Basis;
import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.monster.flying.Lakai;
import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.monster.walking.Boss1;
import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.monster.walking.Runner;
import com.samuelschwenn.labyrinth.game_logic.util.CoordsInt;
import org.javatuples.Pair;

public class Level5 extends Level{
    public Level5(Basis basis){
        super(basis);
        anzahlMauern = 30;
        basis.setPosition(basisPosition);
        basis.setHealth(basis.getMaxHealth());
        addSpawnArea(new Pair<>(new CoordsInt(0, 0), new CoordsInt(0, height - 1)));
        addSpawnArea(new Pair<>(new CoordsInt(0, height - 1), new CoordsInt(width - 1, height - 1)));
        addSpawnArea(new Pair<>(new CoordsInt(0, 0), new CoordsInt(width - 1, 0)));
        addSpawnArea(new Pair<>(new CoordsInt(width - 1, 0), new CoordsInt(width - 1, height - 1)));

        addNumberOfMonstersToSpawnList(4, Lakai.class);
        addNumberOfMonstersToSpawnList(5, Runner.class);
        addNumberOfMonstersToSpawnList(1, Boss1.class);
    }
}
