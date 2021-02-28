package com.example.pacmanlike;

public class LeftTeleportTile extends Tile {
    public LeftTeleportTile() {
        super(0);
        type = "LeftTeleport";
        fillMoves();
    }

    private void fillMoves(){
        possibleMoves.add(new Vector(1,0));
        possibleMoves.add(new Vector(-1,0));
    }
}
