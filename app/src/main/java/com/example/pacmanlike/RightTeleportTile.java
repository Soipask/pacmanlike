package com.example.pacmanlike;

public class RightTeleportTile extends Tile {
    public RightTeleportTile() {
        super(0);
        type = "RightTeleport";
        fillMoves();
    }

    private void fillMoves(){
        possibleMoves.add(new Vector(1,0));
        possibleMoves.add(new Vector(-1,0));
    }

    @Override
    public String toString() {
        return "R";
    }
}
