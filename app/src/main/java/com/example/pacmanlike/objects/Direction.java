package com.example.pacmanlike.objects;

import com.example.pacmanlike.Vector;

public enum Direction {
    UP(0),
    DOWN(1),
    LEFT(2),
    RIGHT(3),
    NONE(4);

    private int value;
    Direction(int value) { this.value = value; }

    public int getValue() { return value; }

    public static Vector convertDirectionToVector(Direction direction) {

        switch (direction) {
            case UP:
                return new Vector(0, -1);
            case DOWN:
                return new Vector(0, 1);
            case RIGHT:
                return new Vector(1, 0);
            case LEFT:
                return new Vector(-1, 0);
            default:
                return new Vector(0, 0);
        }
    }
}
