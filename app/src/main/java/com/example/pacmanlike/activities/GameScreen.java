package com.example.pacmanlike.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.example.pacmanlike.main.AppConstants;
import com.example.pacmanlike.gamemap.GameMap;
import com.example.pacmanlike.view.GameView;
import com.example.pacmanlike.view.LevelParser;

public class GameScreen extends AppCompatActivity {
    GameMap map;
    LevelParser parser = new LevelParser();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // Comment
        // New View

        Intent intent = getIntent();
        String levelPath = intent.getStringExtra(SelectionScreen.SELECTED_LEVEL);

        try {

            parser.init(levelPath, this);
            map = parser.parse();

            // Initialization of the AppConstants class
            AppConstants.initialization(this.getApplicationContext(), map);

            SurfaceView view = new GameView(this, AppConstants.getEngine());
            setContentView(view);

        } catch (Exception e) {
            e.printStackTrace();
            super.finish();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        super.onTouchEvent(event);
        int action = event.getAction();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
            {
                OnActionDown(event);
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                OnActionUp(event);
                break;
            }
            default:break;
        }
        return false;
    }

    /*activates on touch up event*/
    private void OnActionUp(MotionEvent event)
    {
        int x = (int)event.getX();
        int y = (int)event.getY();

        AppConstants.getEngine().setSwipeDirection(x, y);
    }
    /*activates on touch down event*/
    private void OnActionDown(MotionEvent event)
    {
        int x = (int)event.getX();
        int y = (int)event.getY();

        AppConstants.getEngine().setLastTouch(x, y);
    }
}