package com.samuelschwenn.game_logic.draw_and_tickables;

import com.samuelschwenn.game_logic.LogicRepresentation;

public interface Tickable {
    void tick(double timeDelta, LogicRepresentation logicRepresentation);
}
