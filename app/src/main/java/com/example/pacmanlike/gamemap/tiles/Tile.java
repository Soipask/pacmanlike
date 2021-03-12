package com.example.pacmanlike.gamemap.tiles;

import com.example.pacmanlike.objects.Food;
import com.example.pacmanlike.objects.Direction;

import java.util.ArrayList;
import java.util.List;

public abstract class Tile {
    public String type;
    public int rotation;
    private Food _content;
    public int drawableId;


    public void setFood(Food content) { _content = content; }

    public Food getFood() { return _content; }

    protected List<Direction> possibleMoves = new ArrayList<Direction>();
    public List<Direction> getPossibleMoves(){
        return possibleMoves;
    }

    public Tile(int rotation){
        this.rotation = rotation;
    }

    public abstract String toString();
}
