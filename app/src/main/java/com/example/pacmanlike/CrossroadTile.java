package com.example.pacmanlike;

public class CrossroadTile extends Tile{
    public CrossroadTile(int rotation) {
        super(rotation);
        type = "Crossroad";
        fillMoves();
    }

    private void fillMoves(){
        possibleMoves.add(new Vector(1,0));
        possibleMoves.add(new Vector(0,-1));
        possibleMoves.add(new Vector(-1,0));
        possibleMoves.add(new Vector(0,1));
    }
}
