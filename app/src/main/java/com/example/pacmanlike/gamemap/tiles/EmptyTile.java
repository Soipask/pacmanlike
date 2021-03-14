package com.example.pacmanlike.gamemap.tiles;

import com.example.pacmanlike.R;

public class EmptyTile extends Tile {
    public EmptyTile() {
        super(0);
        _type = "Empty";
        _drawableId = R.drawable.empty;
    }

    @Override
    public String toString() {
        return "X";
    }
}
