package com.example.pacmanlike;

public class StraightTile extends Tile {
    public StraightTile(int rotation) {
        super(rotation);
        type = "Straight";
        fillMoves();
    }

    private void fillMoves(){
        switch (rotation){
            case 0:
                possibleMoves.add(new Vector(0,1));
                possibleMoves.add(new Vector(0,-1));
                break;
            case 90:
                possibleMoves.add(new Vector(1,0));
                possibleMoves.add(new Vector(-1,0));
                break;
        }
    }
}
