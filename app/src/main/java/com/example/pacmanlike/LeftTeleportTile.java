package com.example.pacmanlike;

import com.example.pacmanlike.objects.Direction;

public class LeftTeleportTile extends Tile {
    public LeftTeleportTile() {
        super(0);
        type = "LeftTeleport";
        drawableId = R.drawable.leftteleport;
        fillMoves();
    }

    private void fillMoves(){
        possibleMoves.add(Direction.RIGHT);
        possibleMoves.add(Direction.LEFT);
    }

    @Override
    public String toString() {
        return "L";
    }
}
