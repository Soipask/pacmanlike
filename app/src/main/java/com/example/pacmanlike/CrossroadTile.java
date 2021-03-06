package com.example.pacmanlike;

public class CrossroadTile extends Tile{
    public CrossroadTile() {
        super(0);
        type = "Crossroad";
        drawableId = R.drawable.crossroad;
        fillMoves();
    }

    private void fillMoves(){
        possibleMoves.add(new Vector(1,0));
        possibleMoves.add(new Vector(0,-1));
        possibleMoves.add(new Vector(-1,0));
        possibleMoves.add(new Vector(0,1));
    }

    @Override
    public String toString() {
        return "C";
    }
}
