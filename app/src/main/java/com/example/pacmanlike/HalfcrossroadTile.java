package com.example.pacmanlike;

public class HalfcrossroadTile extends Tile {
    public HalfcrossroadTile(String rotation) {
        super(Integer.parseInt(rotation));
        type = "Turn";
        drawableId = R.drawable.halfcrossroad;
        fillMoves();
    }

    // TODO: Check rotation
    private void fillMoves(){
        switch (rotation){
            case 0:
                possibleMoves.add(new Vector(0,-1));
                possibleMoves.add(new Vector(-1,0));
                possibleMoves.add(new Vector(0,1));
                break;
            case 90:
                possibleMoves.add(new Vector(1,0));
                possibleMoves.add(new Vector(-1,0));
                possibleMoves.add(new Vector(0,1));
                break;
            case 180:
                possibleMoves.add(new Vector(1,0));
                possibleMoves.add(new Vector(0,-1));
                possibleMoves.add(new Vector(0,1));
                break;
            case 270:
                possibleMoves.add(new Vector(1,0));
                possibleMoves.add(new Vector(0,-1));
                possibleMoves.add(new Vector(-1,0));
                break;
        }
    }

    @Override
    public String toString() {
        return "H" + rotation;
    }
}
