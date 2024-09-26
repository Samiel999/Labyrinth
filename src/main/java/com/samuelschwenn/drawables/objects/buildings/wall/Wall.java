package com.samuelschwenn.drawables.objects.buildings.wall;

import com.samuelschwenn.drawables.objects.ObjectType;
import com.samuelschwenn.drawables.objects.buildings.Building;
import com.samuelschwenn.util.CoordsInt;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public abstract class Wall extends Building {
    public Wall(int health, CoordsInt position, double kosten, ObjectType type) {
        super(0, health, position, type, kosten);
    }
}
