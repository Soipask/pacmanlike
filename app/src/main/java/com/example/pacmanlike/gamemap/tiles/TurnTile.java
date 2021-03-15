package com.example.pacmanlike.gamemap.tiles;

import com.example.pacmanlike.R;
import com.example.pacmanlike.main.AppConstants;
import com.example.pacmanlike.objects.Direction;

public class TurnTile extends Tile {
    public TurnTile(String rotation) {
        super(Integer.parseInt(rotation));
        _type = "Turn";
        _drawableId = R.drawable.turn;
        fillMoves();
    }

    // TODO: Check rotation
    private void fillMoves(){
        switch (_rotation){
            case 0:
                _possibleMoves.add(Direction.UP);
                _possibleMoves.add(Direction.LEFT);
                break;
            case 90:
                _possibleMoves.add(Direction.UP);
                _possibleMoves.add(Direction.RIGHT);
                break;
            case 180:
                _possibleMoves.add(Direction.DOWN);
                _possibleMoves.add(Direction.RIGHT);
                break;
            case 270:
                _possibleMoves.add(Direction.DOWN);
                _possibleMoves.add(Direction.LEFT);
                break;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(AppConstants.CHAR_TURN) + _rotation;
    }
}
