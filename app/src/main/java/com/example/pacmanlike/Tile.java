package com.example.pacmanlike;

import java.util.ArrayList;
import java.util.List;

public abstract class Tile {
    public String type;
    public int rotation;
    public Food content;

    protected List<Vector> possibleMoves = new ArrayList<Vector>();
    public List<Vector> getPossibleMoves(){
        return possibleMoves;
    }

    public Tile(int rotation){
        this.rotation = rotation;
    }

    public abstract String toString();
}
