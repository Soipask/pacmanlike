package com.example.pacmanlike.objects;


public class Vector{

    public int x = 0;
    public int y = 0;

    public Vector(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Vector(){}

    @Override
    public String toString() {
        return x + ";" + y;
    }
}
