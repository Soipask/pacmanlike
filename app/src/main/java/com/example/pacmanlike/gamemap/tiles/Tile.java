package com.example.pacmanlike.gamemap.tiles;

import com.example.pacmanlike.objects.Food;
import com.example.pacmanlike.objects.Direction;

import java.util.ArrayList;
import java.util.List;

public abstract class Tile {
    public String type;
    public int rotation;
    public Food content;
    public int drawableId;

    protected List<Direction> possibleMoves = new ArrayList<Direction>();
    public List<Direction> getPossibleMoves(){
        return possibleMoves;
    }

    public Tile(int rotation){
        this.rotation = rotation;
    }

    public abstract String toString();
}
