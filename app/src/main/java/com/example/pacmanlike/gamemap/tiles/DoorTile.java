package com.example.pacmanlike.gamemap.tiles;

import com.example.pacmanlike.R;
import com.example.pacmanlike.main.AppConstants;
import com.example.pacmanlike.objects.Direction;

public class DoorTile extends Tile {
    public DoorTile(){
        super(0);
        _type = "Door";
        _drawableId = R.drawable.door;
        fillMoves();
    }

    private void fillMoves(){
        switch (_rotation){
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
        return String.valueOf(AppConstants.CHAR_DOOR) + _rotation;
    }
}
