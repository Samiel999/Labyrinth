package com.samuelschwenn.interfaces;

import com.samuelschwenn.logic.LogicRepresentation;

public interface Tickable {
    void tick(double timeDelta, LogicRepresentation logicRepresentation);
}
