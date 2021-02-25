package com.example.pacmanlike;

import java.util.List;

public class Tile {
    public String type;
    public int rotation;
    public Food content;

    private List<Vector> possibleMoves;
    public List<Vector> getPossibleMoves(){
        return possibleMoves;
    }
}