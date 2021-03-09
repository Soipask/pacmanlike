package com.example.pacmanlike;

import android.icu.text.CaseMap;

import com.example.pacmanlike.gamelogic.AppConstants;

public class GameMap {
    Tile[][] map;
    Vector startingPacPosition;

    public Vector getStartingPacPosition() { return startingPacPosition; }

    public Tile[][] getMap() { return map; }

    public Tile getTile(int x, int y) {return  map[y][x]; }

    public Tile getAbsoluteTile(int x, int y) {
        int tileX = x / AppConstants.getBlockSize();
        int tileY = y / AppConstants.getBlockSize();

        return getTile(tileX, tileY);
    }
}
