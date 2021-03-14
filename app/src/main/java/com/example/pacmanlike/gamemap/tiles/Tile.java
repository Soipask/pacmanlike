package com.example.pacmanlike.gamemap.tiles;

import com.example.pacmanlike.objects.food.Food;
import com.example.pacmanlike.objects.Direction;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents one tile on the map.
 * Every tile has a type, rotation, id of a drawable object it represents and Food it currently has
 */
public abstract class Tile {
    public String _type;
    public int _rotation;
    private Food _content;
    public int _drawableId;
    protected List<Direction> _possibleMoves = new ArrayList<Direction>();

    public void setFood(Food content) { _content = content; }

    public Food getFood() { return _content; }

    public List<Direction> getPossibleMoves(){
        return _possibleMoves;
    }

    public Tile(int rotation){
        this._rotation = rotation;
    }

    public abstract String toString();
}
