package com.example.pacmanlike;

import com.example.pacmanlike.objects.Direction;

public class TurnTile extends Tile {
    public TurnTile(String rotation) {
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
                break;
            case 90:
                possibleMoves.add(Direction.UP);
                possibleMoves.add(Direction.RIGHT);
                break;
            case 180:
                possibleMoves.add(Direction.DOWN);
                possibleMoves.add(Direction.RIGHT);
                break;
            case 270:
                possibleMoves.add(Direction.UP);
                possibleMoves.add(Direction.LEFT);
                break;
        }
    }

    @Override
    public String toString() {
        return "T" + rotation;
    }
}
