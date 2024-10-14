package com.samuelschwenn.game_logic.util;

import java.io.Serializable;

public record CoordsInt(int x, int y) implements Serializable {

    public String toString() {
        return x + "_" + y;
    }

    public boolean isInRange(int reach, CoordsInt position) {
        return x - reach <= position.x() &&
                x + reach >= position.x() &&
                y - reach <= position.y() &&
                y + reach >= position.y();
    }

    public CoordsDouble toCoordsDouble(){
        return new CoordsDouble(this.x, this.y);
    }

    public boolean equals(CoordsInt other) {
        return other.x == this.x && other.y == this.y;
    }
}
