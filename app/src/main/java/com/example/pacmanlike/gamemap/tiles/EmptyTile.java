package com.example.pacmanlike.gamemap.tiles;

import com.example.pacmanlike.R;
import com.example.pacmanlike.gamemap.tiles.Tile;

public class EmptyTile extends Tile {
    public EmptyTile() {
        super(0);
        type = "Empty";
        drawableId = R.drawable.empty;
    }

    @Override
    public String toString() {
        return "X";
    }
}
