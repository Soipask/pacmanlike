package com.example.pacmanlike.gamelogic;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.pacmanlike.activities.GameOverActivity;
import com.example.pacmanlike.activities.GameScreen;
import com.example.pacmanlike.activities.SelectionScreen;
import com.example.pacmanlike.main.AppConstants;
import com.example.pacmanlike.main.MainActivity;
import com.example.pacmanlike.view.GameView;

/**
 * Responsible for screen painting.
 * */
public class DisplayThread extends Thread {
    SurfaceHolder _surfaceHolder;
    Paint _backgroundPaint;

    long _sleepTime;

    //Delay amount between screen refreshes
    final long  DELAY = 4;
    boolean  _isOnRun;

    Context context;

    GameView view;

    /**
     * reates Display thread of games.
     * @param surfaceHolder Surface
     * @param context GameScreen
     * @param view GameView
     */
    public DisplayThread(SurfaceHolder surfaceHolder, Context context, GameView view) {
        _surfaceHolder = surfaceHolder;

        //black painter below to clear the screen before the game is rendered
        _backgroundPaint = new Paint();
        _backgroundPaint.setARGB(255, 0, 0, 0);
        _isOnRun = true;

        this.context = context;

        this.view = view;
    }

    /**
     * This is the main nucleus of our program.
     * From here will be called all the method that are associated with the display in GameEngine object
     * */
    @Override
    public void run() {
        //Looping until the boolean is false
        while (_isOnRun) {
            //Updates the game objects buisiness logic
            AppConstants.getEngine().update();

            //locking the canvas
            Canvas canvas = _surfaceHolder.lockCanvas(null);

            if (canvas != null) {
                //Clears the screen with black paint and draws object on the canvas
                synchronized (_surfaceHolder) {
                    canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), _backgroundPaint);
                    AppConstants.getEngine().draw(canvas);
                }

                //unlocking the Canvas
                _surfaceHolder.unlockCanvasAndPost(canvas);
            }


            if (AppConstants.getEngine().isEndGame()) {
                _isOnRun = false;
            }

            //delay time
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // end game
        // new activity
        if(AppConstants.getEngine().isEndGame()){
            Intent current = ((Activity) context).getIntent();
            String levelName = current.getStringExtra(AppConstants.SELECTED_LEVEL);

            Intent intent = new Intent(context, GameOverActivity.class);
            intent.putExtra(AppConstants.LEVEL_NAME, levelName);
            context.startActivity(intent);
        }
    }

    /**
     * @return whether the thread is running
     * */
    public boolean isRunning() { return _isOnRun; }

    /**
     * Sets the thread state, false = stoped, true = running
     **/
    public void setIsRunning(boolean state)
    {
        _isOnRun = state;
    }
}