package com.example.pacmanlike.view;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.pacmanlike.main.AppConstants;
import com.example.pacmanlike.gamelogic.DisplayThread;
import com.example.pacmanlike.gamelogic.GameEngine;

/**
 * The View class on which the whole thing is being drawn
 * */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder _surfaceHolder;
    Context _context;

    private DisplayThread _displayThread;


    public GameView(Context context, GameEngine gEngine) {
        super(context);

        _context = context;
        initView();
    }

    /**
     * Initiate the view components
     * */
    void initView() {
        SurfaceHolder holder = getHolder();
        holder.addCallback( this);

        _displayThread = new DisplayThread(holder, _context, this);
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
    {
        /*DO NOTHING*/
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        //Stop the display thread
        _displayThread.setIsRunning(false);
        AppConstants.stopThread(_displayThread);
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        //Starts the display thread
        if(!_displayThread.isRunning()) {
            _displayThread = new DisplayThread(getHolder(), _context, this);
            _displayThread.start();
        }
        else {
            _displayThread.start();
        }
    }
}