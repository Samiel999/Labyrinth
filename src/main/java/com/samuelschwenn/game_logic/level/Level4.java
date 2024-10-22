package com.samuelschwenn.game_logic.level;

import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.buildings.basis.Basis;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.monster.BombCarrier;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.monster.Lakai;
import com.samuelschwenn.game_logic.util.CoordsInt;
import org.javatuples.Pair;
import java.util.ArrayList;

public class Level4 extends Level{
    public Level4(Basis basis){
        super(
                3,
                39,
                21,
                new CoordsInt(19, 10),
                basis,
                new ArrayList<>(),
                140,
                30
        );
        addSpawnArea(new Pair<>(new CoordsInt(0, 0), new CoordsInt(0, height - 1)));
        addSpawnArea(new Pair<>(new CoordsInt(0, height - 1), new CoordsInt(width - 1, height - 1)));
        addSpawnArea(new Pair<>(new CoordsInt(0, 0), new CoordsInt(width - 1, 0)));
        addSpawnArea(new Pair<>(new CoordsInt(width - 1, 0), new CoordsInt(width - 1, height - 1)));

        addNumberOfMonstersToSpawnList(6, Lakai.class);
        addNumberOfMonstersToSpawnList(2, BombCarrier.class);

        basis.setHealth(basis.getMaxHealth());
        basis.setPosition(new CoordsInt(19, 10));
    }
}
