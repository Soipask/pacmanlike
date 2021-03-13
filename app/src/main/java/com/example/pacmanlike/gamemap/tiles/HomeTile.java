package com.example.pacmanlike.gamemap.tiles;

import com.example.pacmanlike.R;
import com.example.pacmanlike.main.AppConstants;
import com.example.pacmanlike.objects.Direction;

public class HomeTile extends Tile {
    public int number;

    public HomeTile(String number) {
        super(0);
        type = AppConstants.HOME_TILE;
        this.number = Integer.parseInt(number);
        drawableId = R.drawable.empty;
        fillMoves();
    }

    private void fillMoves(){
        switch (number){
            case 1: possibleMoves.add(Direction.RIGHT); break;
            case 2: possibleMoves.add(Direction.UP); break;
            case 3: possibleMoves.add(Direction.LEFT); break;
        }
    }

    @Override
    public String toString() {
        return "A" + number;
    }
}
