package com.example.pacmanlike;

public class TurnTile extends Tile {
    public TurnTile(int rotation) {
        super(rotation);
        type = "Turn";
        fillMoves();
    }

    // TODO: Check rotation
    private void fillMoves(){
        switch (rotation){
            case 0:
                possibleMoves.add(new Vector(0,-1));
                possibleMoves.add(new Vector(-1,0));
                break;
            case 90:
                possibleMoves.add(new Vector(0,1));
                possibleMoves.add(new Vector(-1,0));
                break;
            case 180:
                possibleMoves.add(new Vector(0,1));
                possibleMoves.add(new Vector(1,0));
                break;
            case 270:
                possibleMoves.add(new Vector(0,-1));
                possibleMoves.add(new Vector(1,0));
                break;
        }
    }
}
