package com.example.pacmanlike;

import com.example.pacmanlike.objects.Direction;

public class HalfcrossroadTile extends Tile {
    public HalfcrossroadTile(String rotation) {
        super(Integer.parseInt(rotation));
        type = "Turn";
        fillMoves();
    }

    // TODO: Check rotation
    private void fillMoves(){
        switch (rotation){
            case 0:
                possibleMoves.add(Direction.UP);
                possibleMoves.add(Direction.LEFT);
                possibleMoves.add(Direction.RIGHT);
                break;
            case 90:
                possibleMoves.add(Direction.RIGHT);
                possibleMoves.add(Direction.UP);
                possibleMoves.add(Direction.DOWN);
                break;
            case 180:
                possibleMoves.add(Direction.RIGHT);
                possibleMoves.add(Direction.LEFT);
                possibleMoves.add(Direction.DOWN);
                break;
            case 270:
                possibleMoves.add(Direction.DOWN);
                possibleMoves.add(Direction.UP);
                possibleMoves.add(Direction.LEFT);
                break;
        }
    }

    @Override
    public String toString() {
        return "H" + rotation;
    }
}
