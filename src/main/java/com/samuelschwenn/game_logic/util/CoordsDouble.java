package com.samuelschwenn.game_logic.util;


import java.io.Serializable;

import static java.lang.Math.max;
import static java.lang.Math.min;


public record CoordsDouble(double x, double y) implements Serializable {
    public String toString() {
        return x + "_" + y;
    }

    public static CoordsDouble getNormalized(Direction direction) {
        return switch (direction) {
            case EAST -> new CoordsDouble(1, 0);
            case NORTH -> new CoordsDouble(0, 1);
            case SOUTH -> new CoordsDouble(0, -1);
            case WEST -> new CoordsDouble(-1, 0);
        };
    }

    public Direction getDirectionTo(CoordsDouble target){
        if(this.equals(target)) return null;

        double differenceInXAxis = target.x() - x;
        double differenceInYAxis = target.y() - y;
        if (differenceInXAxis > 0) {
            return Direction.EAST;
        } else if (differenceInXAxis < 0) {
            return Direction.WEST;
        } else if (differenceInYAxis > 0) {
            return Direction.NORTH;
        } else{
            return Direction.SOUTH;
        }
    }

    public CoordsDouble multipliedBy(double factor) {
        return new CoordsDouble(x * factor, y * factor);
    }

    public CoordsDouble add(CoordsDouble other) {
        return new CoordsDouble(x + other.x, y + other.y);
    }

    public CoordsDouble subtract(CoordsDouble other) {
        return new CoordsDouble(x - other.x, y - other.y);
    }

    public CoordsDouble maxCoords(CoordsDouble other) {
        return new CoordsDouble(max(other.x(), x), max(other.y(), y));
    }

    public CoordsDouble minCoords(CoordsDouble other) {
        return new CoordsDouble(min(other.x(), x), min(other.y(), y));
    }

    public CoordsInt toCoordsInt() {
        return new CoordsInt((int) x, (int) y);
    }
}
