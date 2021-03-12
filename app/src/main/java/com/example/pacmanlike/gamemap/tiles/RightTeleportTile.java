package com.example.pacmanlike.gamemap.tiles;

import com.example.pacmanlike.R;
import com.example.pacmanlike.gamemap.tiles.Tile;
import com.example.pacmanlike.main.AppConstants;
import com.example.pacmanlike.objects.Direction;

public class RightTeleportTile extends Tile {
    public RightTeleportTile() {
        super(0);
        type = AppConstants.RIGHT_TELEPORT;
        drawableId = R.drawable.rightteleport;
        fillMoves();
    }

    private void fillMoves(){
        //possibleMoves.add(Direction.RIGHT);
        possibleMoves.add(Direction.LEFT);
    }

    @Override
    public String toString() {
        return "R";
    }
}
