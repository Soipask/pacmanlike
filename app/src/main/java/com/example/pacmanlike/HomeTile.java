package com.example.pacmanlike;

import com.example.pacmanlike.objects.Direction;

public class HomeTile extends Tile {
    int number;

    public HomeTile(String number) {
        super(0);
        type = "Home";
        this.number = Integer.parseInt(number);
        fillMoves();
    }

    private void fillMoves(){
        switch (number){
            case 1: possibleMoves.add(Direction.RIGHT);
            case 2: possibleMoves.add(Direction.UP);
            case 3: possibleMoves.add(Direction.LEFT);
        }
    }

    @Override
    public String toString() {
        return "A" + number;
    }
}
