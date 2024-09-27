package com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.buildings.wall;

import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.ObjectType;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.buildings.Building;
import com.samuelschwenn.game_logic.util.CoordsInt;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public abstract class Wall extends Building {
    public Wall(int health, CoordsInt position, double kosten, ObjectType type) {
        super(0, health, position, type, kosten);
    }
}
