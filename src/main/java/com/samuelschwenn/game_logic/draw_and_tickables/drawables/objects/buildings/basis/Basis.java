package com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.buildings.basis;

import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.buildings.Building;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static com.samuelschwenn.Main.loop;
import static com.samuelschwenn.game_logic.util.LoopType.game_over;


@NoArgsConstructor
public abstract class Basis extends Building implements Serializable {
    @Override
    public void die() {
        loop.update(game_over);
    }
}
