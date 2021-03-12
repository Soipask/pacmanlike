package com.example.pacmanlike.gamelogic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import com.example.pacmanlike.gamemap.GameMap;
import com.example.pacmanlike.gamemap.Home;
import com.example.pacmanlike.gamemap.tiles.Tile;
import com.example.pacmanlike.main.AppConstants;
import com.example.pacmanlike.objects.DrawalbeObjects;
import com.example.pacmanlike.objects.Food;
import com.example.pacmanlike.objects.ghosts.Ghost;
import com.example.pacmanlike.objects.Vector;
import com.example.pacmanlike.objects.ArrowIndicator;
import com.example.pacmanlike.objects.Direction;
import com.example.pacmanlike.objects.PacMan;
import com.example.pacmanlike.objects.ghosts.engine.GhostEngineAdvanced;
import com.example.pacmanlike.objects.ghosts.engine.GhostsEngine;

import java.util.ArrayList;

/*
* Stores all object references that relevant for the game display
* Calls objects business logic methods, and draw them to the given Canvas from DisplayThread
* */
public class GameEngine {
    /*MEMBERS*/



   static Boolean _ENDGAME = false;

    static PacMan _pacman;

    static ArrowIndicator _arrowIdenticator;

    static final Object _sync = new Object();
    static float _lastTouchedX, _lastTouchedY;

    private static int _pacSpeed;
    private static int _pacStep;

    private static int _SCORE;
    private static int _PELLSCORE = 20, _POWERPELLSCORE = 50, _GHOSTSCORE = 200;

    static ArrayList<Ghost> _ghosts = new ArrayList<Ghost>();

    private static GhostsEngine _ghostsEngine;

    public GameEngine(Context context) {

        _paint = new Paint();
        _paintPells = new Paint();
        _paintPells.setStyle(Paint.Style.FILL);
        _paintPells.setColor(Color.rgb(255,165,0));

        _paintPowerPells = new Paint();
        _paintPowerPells.setStyle(Paint.Style.FILL);
        _paintPowerPells.setColor(Color.YELLOW);


        // Adds game objects
        _pacman = new PacMan(context, AppConstants.getGameMap().getStartingPacPosition());
        _ghostsEngine = new GhostEngineAdvanced(context, Home.getInstance().getCoordinates());
        addPells(AppConstants.getGameMap());
        _arrowIdenticator = new ArrowIndicator(context);

        _pacSpeed = 10;
        _pacStep = 1;
        _SCORE = 0;

    }

    Paint _paint, _paintPells, _paintPowerPells;


    public void addPells(GameMap map) {

        for (Vector powerPell :map.getPowerPelletsPosition()) {

            int x = powerPell.x;
            int y = powerPell.y;

            Tile tile = map.getTile(x,y);

            if(!tile.type.equals("Empty") && !tile.type.equals("Home") &&
                    !(x == map.getStartingPacPosition().x && y == map.getStartingPacPosition().y)){
                    tile.setFood(Food.PowerPellet);
            }
        }



        for (int y = 0; y < AppConstants.MAP_SIZE_Y; y++) {
            for (int x = 0; x < AppConstants.MAP_SIZE_X; x++)
            {
                Tile tile = map.getTile(x,y);

                if(!tile.type.equals("Empty") && !tile.type.equals("Home") && tile.getFood() != Food.PowerPellet &&
                        !(x == map.getStartingPacPosition().x && y == map.getStartingPacPosition().y)) {
                    tile.setFood(Food.Pellet);
                }
            }
        }
    }

    /**
     * Updates all relevant objects business logic
     * */
    public void update()
    {
        updatePacman();
    }


    /**
     * Iterates through the Bubble list and advances them
     * */
    private void updatePacman() {

        synchronized (_sync) {

            for (int step = 0; step < _pacSpeed; step++) {
                updataPacmanDirection();
                detectWallCollision();
                _ghostsEngine.update();

                updatePells();
                movePacman(_pacStep);
                updatePells();

                upadateTeleporation(_pacman);

                _ghostsEngine.moveGhosts(_pacStep);

               _ghostsEngine.updateTeleporation();

               interactionPacGhosts();

            }
        }
    }



    public void interactionPacGhosts(){

        Vector _pacmanPosition = _pacman.getAbsolutePosition();
        int _blockSize = AppConstants.getBlockSize();

        for (Ghost g: _ghostsEngine.getGhosts()) {

            Vector gPosition = g.getAbsolutePosition();
            int distanceX  = _pacmanPosition.x - gPosition.x;
            int distanceY = _pacmanPosition.y - gPosition.y;

            int distance = (distanceX*distanceX) + (distanceY*distanceY);
            int radius = (_blockSize /4)*(_blockSize/4);

            if( distance < radius){

                if(g.isVulnerable()){
                    _SCORE += _GHOSTSCORE;
                    g.setVulnerable(false);
                    g.setRelativePosition(_ghostsEngine.getHome());

                }else {
                    _ENDGAME = true;
                }
            }
        }
    }

    private void detectWallCollision() {
        GameMap gameMap = AppConstants.getGameMap();
        Vector position = _pacman.getAbsolutePosition();

        if(AppConstants.testCenterTile(position)) {

            Tile tile = gameMap.getAbsoluteTile(position.x, position.y);

            if (!tile.getPossibleMoves().contains(_pacman.getDirection())) {
                if(_pacman.getDirection() != Direction.NONE) {
                    _pacman.setDirection(Direction.NONE);
                }
            }
        }
    }

    private void updataPacmanDirection() {
        GameMap gameMap = AppConstants.getGameMap();
        Vector position = _pacman.getAbsolutePosition();

        if(AppConstants.testCenterTile(position)) {

            Tile tile = gameMap.getAbsoluteTile(position.x, position.y);

            if (tile.getPossibleMoves().contains(_arrowIdenticator.getDirection())) {
                _pacman.setDirection(_arrowIdenticator.getDirection());
            }
        }
    }



    private void upadateTeleporation(DrawalbeObjects entity){
        GameMap gameMap = AppConstants.getGameMap();
        Vector position = entity.getAbsolutePosition();

        if(AppConstants.testCenterTile(position)) {

            Tile tile = gameMap.getAbsoluteTile(position.x, position.y);
            int _blockSize = AppConstants.getBlockSize();

            if(tile.type.equals("LeftTeleport"))
            {
                Vector right = gameMap.getRightTeleportPosition();
                entity.setRelativePosition(right);
            }

            if(tile.type.equals("RightTeleport")){
                Vector left = gameMap.getLeftTeleportPosition();

                entity.setRelativePosition(left);
            }
        }
    }

    private void updatePells(){
        GameMap gameMap = AppConstants.getGameMap();
        Vector position = _pacman.getAbsolutePosition();

        if(AppConstants.testCenterTile(position)) {
            Tile tile = gameMap.getAbsoluteTile(position.x, position.y);

            if(tile.getFood() == Food.Pellet) {

                _SCORE += _PELLSCORE;
                tile.setFood(Food.None);
            } else if(tile.getFood() == Food.PowerPellet) {
                _SCORE += _POWERPELLSCORE;
                _ghostsEngine.startVulnereble();
                tile.setFood(Food.None);
            }
        }
    }
    /**
     *
     * @param step
     */
    private void movePacman(int step) {
        Vector position = _pacman.getAbsolutePosition();
        switch (_pacman.getDirection())
        {
            case UP:
                _pacman.setAbsolutePosition(new Vector(position.x, position.y - step));
                break;
            case DOWN:
                _pacman.setAbsolutePosition(new Vector(position.x, position.y + step));
                break;
            case LEFT:
                _pacman.setAbsolutePosition(new Vector(position.x - step, position.y));
                break;
            case RIGHT:;
                _pacman.setAbsolutePosition(new Vector(position.x + step, position.y));
                break;
            default:
                break;
        }
    }


    /**
     * Draws all objects according to their parameters
     * @param canvas
     * 			canvas on which will be drawn the objects
     * */
    public void draw(Canvas canvas) {

        drawBackground(canvas);
        drawPells(canvas);

        drawGhosts(canvas, _paint);
        _pacman.draw(canvas, _paint);
        _arrowIdenticator.draw(canvas, _paint);
    }

    public void drawPells(Canvas canvas) {

        GameMap map = AppConstants.getGameMap();
        int _blockSize = AppConstants.getBlockSize();

        for (int y = 0; y < AppConstants.MAP_SIZE_Y; y++) {
            for (int x = 0; x < AppConstants.MAP_SIZE_X; x++)
            {
                if(map.getTile(x,y).getFood() == Food.Pellet) {
                    canvas.drawCircle(x * _blockSize + _blockSize / 2, y * _blockSize + _blockSize / 2, AppConstants.getBlockSize() * 0.1f, _paintPells);
                } else if(map.getTile(x,y).getFood() == Food.PowerPellet) {

                    canvas.drawCircle(x * _blockSize + _blockSize / 2, y * _blockSize + _blockSize / 2, AppConstants.getBlockSize() * 0.2f, _paintPells);
                    canvas.drawCircle(x * _blockSize + _blockSize / 2, y * _blockSize + _blockSize / 2, AppConstants.getBlockSize() * 0.1f, _paintPowerPells);
                }
            }
        }
    }

    private void drawGhosts(Canvas canvas, Paint paint) {
        _ghostsEngine.draw(canvas, paint);
    }

    /**
     * Draws background bitmap
     * @param canvas
     * 			canvas on which will be drawn the bitmap
     **/
    private void drawBackground(Canvas canvas) {
        canvas.drawBitmap(AppConstants.getGameMap().getBackground(), 0,0, _paint);
    }

    /**
     * Sets cannon bitmap rotation accordingly to touch event
     * @param touchX
     * 			x coordinate of the touch event
     * @param touchY
     * 			y coordinate of the touch event
     * */
    public void setSwipeDirection(int touchX, int touchY) {
        float xDiff = (touchX - _lastTouchedX);
        float yDiff = (touchY - _lastTouchedY);

        if (Math.abs(yDiff) > Math.abs(xDiff)) {
            if (yDiff < 0) {
                _arrowIdenticator.setDirection(Direction.UP);
            } else if (yDiff > 0) {
                _arrowIdenticator.setDirection(Direction.DOWN);
            }
        } else {
            if (xDiff < 0) {
                _arrowIdenticator.setDirection(Direction.LEFT);
            } else if (xDiff > 0) {
                _arrowIdenticator.setDirection(Direction.RIGHT);
            }
        }
    }

    /**
     * @return cannon object
     * */
    public PacMan getPacman() { return _pacman; }

    /**
     * Sets previous touch coordinates
     * @param x
     * 		current touch x coordinate
     * @param y
     * 		current touch y coordinate
     * */
    public void setLastTouch(float x, float y) {
        _lastTouchedX = x;
        _lastTouchedY = y;
    }
}