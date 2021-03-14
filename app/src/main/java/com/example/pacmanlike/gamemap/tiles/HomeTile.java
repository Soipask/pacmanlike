package com.example.pacmanlike.gamemap.tiles;

import com.example.pacmanlike.R;
import com.example.pacmanlike.main.AppConstants;
import com.example.pacmanlike.objects.Direction;

public class HomeTile extends Tile {
    public int number;

    public HomeTile(String number) {
        super(0);
        _type = AppConstants.HOME_TILE;
        this.number = Integer.parseInt(number);
        _drawableId = R.drawable.empty;
        fillMoves();
    }

    private void fillMoves(){
        switch (number){
            case 1: _possibleMoves.add(Direction.RIGHT); break;
            case 2: _possibleMoves.add(Direction.UP); break;
            case 3: _possibleMoves.add(Direction.LEFT); break;
        }
    }

    @Override
    public String toString() {
        return "A" + number;
    }
}
