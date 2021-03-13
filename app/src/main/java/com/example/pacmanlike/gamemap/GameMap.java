package com.example.pacmanlike.gamemap;

import android.graphics.Bitmap;

import com.example.pacmanlike.gamemap.tiles.Tile;
import com.example.pacmanlike.objects.Vector;
import com.example.pacmanlike.main.AppConstants;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class GameMap {
    private Tile[][] _map;
    private Vector _startingPacPosition;
    private Vector _leftTeleportPosition;
    private Vector _rightTeleportPosition;
    private ArrayList<Vector> _powerPelletsPosition = new ArrayList<>();
    private Home _home;

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

    public Home getHome(){ return _home;}
    public void setHome(Home home){
        _home = home;
    }

    @Override
    public String toString(){
        // pacman
        String pac = AppConstants.PAC_STARTING_KEYWORD + AppConstants.KEY_VALUE_DELIMITER +
                _startingPacPosition.toString();

        // pellets
        StringBuilder positionStrings = new StringBuilder();
        positionStrings.append(_powerPelletsPosition.get(0));
        for (int i = 1; i < _powerPelletsPosition.size(); i++){
            positionStrings.append(AppConstants.MORE_DATA_DELIMITER);
            positionStrings.append(_powerPelletsPosition.get(i).toString());
        }

        String pellets = (_powerPelletsPosition.size() > 0)?
                AppConstants.POWER_STARTING_KEYWORD +
                        AppConstants.KEY_VALUE_DELIMITER +
                        positionStrings.toString():
                "";

        // map
        StringBuilder map = new StringBuilder();
        for (Tile[] row : _map){
            map.append(row[0]);
            for (int i = 1; i < row.length; i++){
                map.append(AppConstants.CSV_DELIMITER);
                map.append(row[i].toString());
            }
            map.append("\n");
        }
        String mapString = map.toString();

        return pac + AppConstants.CSV_DELIMITER + pellets + "\n" + mapString;
    }
}
