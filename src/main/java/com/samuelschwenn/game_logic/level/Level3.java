package com.samuelschwenn.game_logic.level;

import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.buildings.basis.Basis;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.monster.DefaultMonster;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.monster.Golem;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.monster.Lakai;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.monster.Runner;
import com.samuelschwenn.game_logic.util.CoordsInt;
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

        addNumberOfMonstersToSpawnList(11, DefaultMonster.class);
        addNumberOfMonstersToSpawnList(2, Golem.class);
        addNumberOfMonstersToSpawnList(3, Lakai.class);
        addNumberOfMonstersToSpawnList(6, Runner.class);

        basis.setHealth(basis.getMaxHealth());
        basis.setPosition(new CoordsInt(19, 9));
    }
}
