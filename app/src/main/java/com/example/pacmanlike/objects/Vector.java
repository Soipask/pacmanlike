package com.example.pacmanlike.objects;

/**
 * A class representing a location on a game map.
 */
public class Vector{

    // x and y coordinations in game map
    public int x = 0;
    public int y = 0;

    /**
     * Constructor for the vector with the values specified as a parameter.
     * @param x Value of axis x.
     * @param y Value of axis y.
     */
    public Vector(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Constructor for empty vector with default values x = 0 and y = 0.
     */
    public Vector(){}

    /**
     * Returns a string that represents the current object.
     * @return String that represents the Vector.
     */
    @Override
    public String toString() {
        return x + ";" + y;
    }
}
