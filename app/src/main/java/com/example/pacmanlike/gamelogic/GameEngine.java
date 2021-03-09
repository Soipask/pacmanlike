package com.example.pacmanlike.gamelogic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.example.pacmanlike.GameMap;
import com.example.pacmanlike.Tile;
import com.example.pacmanlike.Vector;
import com.example.pacmanlike.objects.ArrowIndicator;
import com.example.pacmanlike.objects.Direction;
import com.example.pacmanlike.objects.PacMan;

/*
* Stores all object references that relevant for the game display
* Calls objects business logic methods, and draw them to the given Canvas from DisplayThread
* */
public class GameEngine {
    /*MEMBERS*/
    static final int BUBBLE_BOUNCE = 1;
    static PacMan _pacman;

    static ArrowIndicator _arrowIdenticator;

    // static List<> _bubles;
    static final Object _sync = new Object();
    static float _lastTouchedX, _lastTouchedY;

    private static int _pacSpeed;
    private static int _pacStep;



    public GameEngine(Context context) {

        _paint = new Paint();

        _pacman = new PacMan(context, AppConstants.getGameMap().getStartingPacPosition());
        _arrowIdenticator = new ArrowIndicator(context);

        _pacSpeed = 10;
        _pacStep = 1;

    }

    Paint _paint;

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
                movePacman(_pacStep);
            }
        }
    }

    private boolean testCenterTile(Vector position) {

        int blockSize = AppConstants.getBlockSize();
        int centerX = position.x % blockSize;
        int centerY = position.y % blockSize;

        if(centerX == blockSize/2 && centerY == blockSize /2) {
            return true;
        }else {
            return false;
        }
    }

    private void detectWallCollision() {
        GameMap gameMap = AppConstants.getGameMap();
        Vector position = _pacman.getPosition();

        if(testCenterTile(position)) {

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
        Vector position = _pacman.getPosition();

        if(testCenterTile(position)) {

            Tile tile = gameMap.getAbsoluteTile(position.x, position.y);

            if (tile.getPossibleMoves().contains(_arrowIdenticator.getDirection())) {
                _pacman.setDirection(_arrowIdenticator.getDirection());
            }
        }
    }

    /**
     *
     * @param step
     */
    private void movePacman(int step) {

        Vector position = _pacman.getPosition();

        switch (_pacman.getDirection())
        {
            case UP:
                _pacman.setPosition(new Vector(position.x, position.y - step));
                break;
            case DOWN:
                _pacman.setPosition(new Vector(position.x, position.y + step));
                break;
            case LEFT:
                _pacman.setPosition(new Vector(position.x - step, position.y));
                break;
            case RIGHT:;
                _pacman.setPosition(new Vector(position.x + step, position.y));
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
        _pacman.draw(canvas, _paint);
        _arrowIdenticator.draw(canvas, _paint);
    }

    /**
     * Draws background bitmap
     * @param canvas
     * 			canvas on which will be drawn the bitmap
     **/
    private void drawBackground(Canvas canvas) {
        canvas.drawBitmap(AppConstants.m, 0,0, _paint);
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