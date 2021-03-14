package com.example.pacmanlike.gamelogic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.pacmanlike.objects.ArrowIndicator;
import com.example.pacmanlike.objects.Direction;

/*
* Stores all object references that relevant for the game display
* Calls objects business logic methods, and draw them to the given Canvas from DisplayThread
* */
public abstract class GameEngine {
    /*MEMBERS*/

    // information on the end of the game
    protected Boolean _endGame = false, _gameOver = false;

    // the direction chosen by the player
    protected ArrowIndicator _arrowIdenticator;

    // synchronization object
    static final Object _sync = new Object();

    // last touch on the screen
    protected float _lastTouchedX, _lastTouchedY;

    // current score in the game
    protected int _SCORE;

    // basci paint (object)
    protected  Paint _paint;

    /**
     * Stores all object references that relevant for the game display
     * Calls objects business logic methods, and draw them to the given Canvas from DisplayThread
     * In the constructor I will set the properties to the default values
     * @param context Given context
     */
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
     * @return Player hero.
     * */
    public abstract Object getHero();

    /**
     * @return True if the game is over
     */
    public boolean isEndGame(){return _endGame; }

    /**
     * @return True if the player lost
     */
    public boolean isGameOver() {return _gameOver;}

    /**
     * @return The current score value in the game
     */
    public Integer getScore() {return _SCORE; }

    /**
     *
     * @param touchX
     * @param touchY
     */
    public void setSwipeDirection(int touchX, int touchY) {

        //
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