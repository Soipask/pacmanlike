package com.example.pacmanlike.gamemap.tiles;

import com.example.pacmanlike.R;
import com.example.pacmanlike.objects.Direction;

public class HalfcrossroadTile extends Tile {
    public HalfcrossroadTile(String rotation) {
        super(Integer.parseInt(rotation));
        _type = "Turn";
        _drawableId = R.drawable.halfcrossroad;
        fillMoves();
    }

    // TODO: Check rotation
    private void fillMoves(){
        switch (_rotation){
            case 0:
                _possibleMoves.add(Direction.UP);
                _possibleMoves.add(Direction.LEFT);
                _possibleMoves.add(Direction.RIGHT);
                break;
            case 90:
                _possibleMoves.add(Direction.RIGHT);
                _possibleMoves.add(Direction.UP);
                _possibleMoves.add(Direction.DOWN);
                break;
            case 180:
                _possibleMoves.add(Direction.RIGHT);
                _possibleMoves.add(Direction.LEFT);
                _possibleMoves.add(Direction.DOWN);
                break;
            case 270:
                _possibleMoves.add(Direction.DOWN);
                _possibleMoves.add(Direction.UP);
                _possibleMoves.add(Direction.LEFT);
                break;
        }
    }

    @Override
    public String toString() {
        return "H" + _rotation;
    }
}
