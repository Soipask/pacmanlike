package com.example.pacmanlike;

import com.example.pacmanlike.objects.Direction;

public class CrossroadTile extends Tile{
    public CrossroadTile() {
        super(0);
        type = "Crossroad";
        drawableId = R.drawable.crossroad;
        fillMoves();
    }

    private void fillMoves(){
        possibleMoves.add(Direction.RIGHT);
        possibleMoves.add(Direction.UP);
        possibleMoves.add(Direction.LEFT);
        possibleMoves.add(Direction.DOWN);
    }

    @Override
    public String toString() {
        return "C";
    }
}
