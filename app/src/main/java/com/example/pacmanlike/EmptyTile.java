package com.example.pacmanlike;

public class EmptyTile extends Tile {

    public EmptyTile() {
        super(0);
        type = "Empty";
    }

    @Override
    public String toString() {
        return "X";
    }
}
