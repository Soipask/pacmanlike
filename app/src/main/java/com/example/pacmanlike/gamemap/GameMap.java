package com.example.pacmanlike.gamemap;

import android.graphics.Bitmap;

import com.example.pacmanlike.gamemap.tiles.Tile;
import com.example.pacmanlike.objects.Vector;
import com.example.pacmanlike.main.AppConstants;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GameMap {
    private Tile[][] _map;
    private Vector _startingPacPosition;
    private Vector _leftTeleportPosition;
    private Vector _rightTeleportPosition;
    private ArrayList<Vector> _powerPelletsPosition;

    private Bitmap _background;

    public void setStartingPacPosition(Vector position) { _startingPacPosition = position; }
    public Vector getStartingPacPosition() { return _startingPacPosition; }

    public Tile[][] getMap() { return _map; }
    public void setMap(Tile[][] map) {_map = map;}

    public Tile getTile(int x, int y) {return  _map[y][x]; }

    public Tile getAbsoluteTile(int x, int y) {
        int tileX = x / AppConstants.getBlockSize();
        int tileY = y / AppConstants.getBlockSize();

        return getTile(tileX, tileY);
    }

    public Bitmap getBackground() {return _background; }
    public void setBackground(Bitmap bitmap) {_background = bitmap; }

    public Vector getRightTeleportPosition(){ return _rightTeleportPosition;}
    public void setLeftTeleportPosition(Vector leftTeleportPosition) {
        _leftTeleportPosition = leftTeleportPosition;
    }

    public Vector getLeftTeleportPosition(){ return _leftTeleportPosition;}
    public void setRightTeleportPosition(Vector rightTeleportPosition){
        _rightTeleportPosition = rightTeleportPosition;
    }

    public ArrayList<Vector> getPowerPelletsPosition() { return _powerPelletsPosition;}
    public void setPowerPelletsPosition(ArrayList<Vector> powerPellets){
        _powerPelletsPosition = powerPellets;
    }
}
