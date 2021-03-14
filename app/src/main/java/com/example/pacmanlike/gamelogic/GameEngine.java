package com.example.pacmanlike.gamelogic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import com.example.pacmanlike.gamemap.GameMap;
import com.example.pacmanlike.gamemap.tiles.Tile;
import com.example.pacmanlike.main.AppConstants;
import com.example.pacmanlike.objects.DrawableObjects;
import com.example.pacmanlike.objects.Food;
import com.example.pacmanlike.objects.Pells;
import com.example.pacmanlike.objects.ghosts.Ghost;
import com.example.pacmanlike.objects.Vector;
import com.example.pacmanlike.objects.ArrowIndicator;
import com.example.pacmanlike.objects.Direction;
import com.example.pacmanlike.objects.PacMan;
import com.example.pacmanlike.objects.ghosts.engine.GhostsEngine;
import com.example.pacmanlike.objects.ghosts.engine.GhostsEngineAdvanced;

/*
* Stores all object references that relevant for the game display
* Calls objects business logic methods, and draw them to the given Canvas from DisplayThread
* */
public abstract class GameEngine {
    /*MEMBERS*/
    protected Boolean _endGame = false, _gameOver = false;
    protected ArrowIndicator _arrowIdenticator;
    static final Object _sync = new Object();
    protected float _lastTouchedX, _lastTouchedY;
    protected int _SCORE;

    protected  Paint _paint;

    public GameEngine(Context context) {

        _SCORE = 0;
        _endGame = false;
        _gameOver = false;

        _paint = new Paint();
        _arrowIdenticator = new ArrowIndicator(context);
    }

    /**
     * Updates all relevant objects business logic
     * */
    public abstract void update();

    /**
     * Draws all objects according to their parameters
     * @param canvas
     * 			canvas on which will be drawn the objects
     * */
    public abstract void draw(Canvas canvas);

    /**
     * @return
     * */
    public abstract Object getHero();

    public boolean isEndGame(){return _endGame; }

    public boolean isGameOver() {return _gameOver;}

    public Integer getScore() {return _SCORE; }

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