package com.example.pacmanlike.gamemap.tiles;

import com.example.pacmanlike.R;
import com.example.pacmanlike.objects.Direction;

public class CrossroadTile extends Tile {
    public CrossroadTile() {
        super(0);
        _type = "Crossroad";
        _drawableId = R.drawable.crossroad;
        fillMoves();
    }

    private void fillMoves(){
        _possibleMoves.add(Direction.RIGHT);
        _possibleMoves.add(Direction.UP);
        _possibleMoves.add(Direction.LEFT);
        _possibleMoves.add(Direction.DOWN);
    }

    @Override
    public String toString() {
        return "C";
    }
}
