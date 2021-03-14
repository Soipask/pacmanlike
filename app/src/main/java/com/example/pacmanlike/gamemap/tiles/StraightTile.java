package com.example.pacmanlike.gamemap.tiles;

import com.example.pacmanlike.R;
import com.example.pacmanlike.objects.Direction;

public class StraightTile extends Tile {
    public StraightTile(String rotation) {
        super(Integer.parseInt(rotation));
        _type = "Straight";
        _drawableId = R.drawable.straight;
        fillMoves();
    }

    private void fillMoves(){
        switch (_rotation % 180){
            case 0:
                _possibleMoves.add(Direction.LEFT);
                _possibleMoves.add(Direction.RIGHT);
                break;
            case 90:
                _possibleMoves.add(Direction.UP);
                _possibleMoves.add(Direction.DOWN);
                break;
        }
    }

    @Override
    public String toString() {
        return "S" + _rotation;
    }
}
