package com.example.pacmanlike.gamemap.tiles;

import com.example.pacmanlike.R;
import com.example.pacmanlike.main.AppConstants;

public class EmptyTile extends Tile {
    public EmptyTile() {
        super(0);
        _type = "Empty";
        _drawableId = R.drawable.empty;
    }

    @Override
    public String toString() {
        return String.valueOf(AppConstants.CHAR_EMPTY);
    }
}
