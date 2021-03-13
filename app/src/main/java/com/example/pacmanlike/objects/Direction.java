package com.example.pacmanlike.objects;

/**
 * Enum representing the different direction of moving objects,
 * that are placed on the game map.
 */
public enum Direction {

    UP(0),
    DOWN(1),
    LEFT(2),
    RIGHT(3),
    NONE(4);

    // iteration value
    private int value;

    // Index
    Direction(int value) { this.value = value; }

    // Direction To integer
    public int getValue() { return value; }

    /**
     * Converts the direction to rotation vector.
     * @param direction Given direction.
     * @return Rotation vector.
     */
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

    /**
     * Returns the opposite direction.
     * @param direction Current direction.
     * @return Opposite direction.
     */
    public static Direction getOppositeDirection(Direction direction){
        switch (direction) {
            case UP:
                return Direction.DOWN;
            case DOWN:
                return Direction.UP;
            case RIGHT:
                return Direction.LEFT;
            case LEFT:
                return Direction.RIGHT;
            default:
                return Direction.NONE;
        }
    }
}
