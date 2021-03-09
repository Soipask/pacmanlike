package com.example.pacmanlike;

import com.example.pacmanlike.objects.Direction;

public class StraightTile extends Tile {
    public StraightTile(String rotation) {
        super(Integer.parseInt(rotation));
        type = "Straight";
        drawableId = R.drawable.straight;
        fillMoves();
    }

    private void fillMoves(){
        switch (rotation){
            case 0:
                possibleMoves.add(Direction.LEFT);
                possibleMoves.add(Direction.RIGHT);
                break;
            case 90:
                possibleMoves.add(Direction.UP);
                possibleMoves.add(Direction.DOWN);
                break;
        }
    }

    @Override
    public String toString() {
        return "S" + rotation;
    }
}
