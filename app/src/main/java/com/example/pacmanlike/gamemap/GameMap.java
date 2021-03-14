package com.example.pacmanlike.gamemap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.pacmanlike.gamemap.tiles.Tile;
import com.example.pacmanlike.objects.Vector;
import com.example.pacmanlike.main.AppConstants;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class GameMap {

    // game map on game map
    private Tile[][] _map;

    // pacman starting position on game map
    private Vector _startingPacPosition;

    // left teleport position on game map
    private Vector _leftTeleportPosition;

    // right teleport position on game map
    private Vector _rightTeleportPosition;

    // power pells positions on game map
    private ArrayList<Vector> _powerPelletsPosition = new ArrayList<>();

    // ghost homew position on game map
    private Home _home;

    private Bitmap _background;

    /**
     * Sets pacman starting position.
     * @param position Pacman starting position.
     */
    public void setStartingPacPosition(Vector position) { _startingPacPosition = position; }

    /**
     * Returns pacman starting position.
     * @return Position.
     */
    public Vector getStartingPacPosition() { return _startingPacPosition; }

    /**
     * Returns the game map.
     * @return Game map.
     */
    public Tile[][] getMap() { return _map; }

    /**
     * Sets the game map.
     * @param map Game map.
     */
    public void setMap(Tile[][] map) {_map = map;}

    /**
     * Returns the tile to the x and y positions in the game map.
     * @param x Value of x axis.
     * @param y Value of y axis.
     * @return Tile on position x and y.
     */
    public Tile getTile(int x, int y) {return  _map[y][x]; }

    /**
     * Returns the tile to the x and y absolute positions in the game map.
     * From an absolute position they express a relative position and
     * and assigns a tile to it onthe game map.
     * @param x Absolute value of x axis.
     * @param y Absolute value of y axis.
     * @return Tile on postion x and y.
     */
    public Tile getAbsoluteTile(int x, int y) {
        int tileX = x / AppConstants.getBlockSize();
        int tileY = y / AppConstants.getBlockSize();

        return getTile(tileX, tileY);
    }

    /**
     * Gets background map image
     * @return Bitmap game map.
     */
    public Bitmap getBackground() {return _background; }

    /**
     * Sets background image game map.
     * @param bitmap Game map.
     */
    public void setBackground(Bitmap bitmap) {_background = bitmap; }

    /**
     * Gets right teleport position.
     * @return Position.
     */
    public Vector getRightTeleportPosition(){ return _rightTeleportPosition;}

    /**
     * Sets left releport position.
     * @param leftTeleportPosition Position.
     */
    public void setLeftTeleportPosition(Vector leftTeleportPosition) {
        _leftTeleportPosition = leftTeleportPosition;
    }

    /**
     * Gets left teleport position.
     * @return Position.
     */
    public Vector getLeftTeleportPosition(){ return _leftTeleportPosition;}

    /**
     * Sets right teleport posotion.
     * @param rightTeleportPosition Position.
     */
    public void setRightTeleportPosition(Vector rightTeleportPosition){
        _rightTeleportPosition = rightTeleportPosition;
    }

    /**
     * Returns a list of power pells position.
     * @return Power pells.
     */
    public ArrayList<Vector> getPowerPelletsPosition() { return _powerPelletsPosition;}

    /**
     * Sets power pells
     * @param powerPellets List of power pelss position.
     */
    public void setPowerPelletsPosition(ArrayList<Vector> powerPellets){
        _powerPelletsPosition = powerPellets;
    }

    /**
     * Returns ghosts home.
     * @return Home.
     */
    public Home getHome(){ return _home;}

    /**
     * Sets ghosts home.
     * @param home Ghosts home.
     */
    public void setHome(Home home){
        _home = home;
    }

    /**
     * Returns a string that represents the current object.
     * @return String that represents the GameMap.
     */
    @Override
    public String toString(){
        // pacman
        String pac = AppConstants.PAC_STARTING_KEYWORD + AppConstants.KEY_VALUE_DELIMITER +
                _startingPacPosition.toString();

        // pellets
        StringBuilder positionStrings = new StringBuilder();
        String pellets = "";
        if (_powerPelletsPosition.size() > 0) {
            positionStrings.append(_powerPelletsPosition.get(0));
            for (int i = 1; i < _powerPelletsPosition.size(); i++) {
                positionStrings.append(AppConstants.MORE_DATA_DELIMITER);
                positionStrings.append(_powerPelletsPosition.get(i).toString());
            }

            pellets = AppConstants.POWER_STARTING_KEYWORD +
                            AppConstants.KEY_VALUE_DELIMITER +
                            positionStrings.toString();
        }

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

    /**
     * Draws background bitmap
     * @param canvas
     * 			canvas on which will be drawn the bitmap
     **/
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(_background, 0,0, paint);
    }
}
