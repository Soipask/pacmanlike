package com.example.pacmanlike.gamemap.tiles;

import com.example.pacmanlike.R;
import com.example.pacmanlike.main.AppConstants;
import com.example.pacmanlike.objects.Direction;

public class LeftTeleportTile extends Tile {
    public LeftTeleportTile() {
        super(0);
        _type = AppConstants.LEFT_TELEPORT;
        _drawableId = R.drawable.leftteleport;
        fillMoves();
    }

    private void fillMoves(){
        _possibleMoves.add(Direction.RIGHT);
        // possibleMoves.add(Direction.LEFT);
    }

    @Override
    public String toString() {
        return "L";
    }
}
