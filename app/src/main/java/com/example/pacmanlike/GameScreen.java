package com.example.pacmanlike;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;

import com.example.pacmanlike.gamelogic.AppConstants;
import com.example.pacmanlike.view.GameView;

import java.io.File;
import java.io.InputStream;
import java.util.Scanner;

public class GameScreen extends AppCompatActivity {
    GameMap map;
    LevelParser parser = new LevelParser();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // Comment
        // New View
        // setContentView(R.layout.activity_game_screen);

        Intent intent = getIntent();
        String levelPath = intent.getStringExtra(SelectionScreen.SELECTED_LEVEL);
        String storageType = intent.getStringExtra(SelectionScreen.STORAGE_TYPE);

        try {
            Scanner reader = null;
            if (storageType.equals(SelectionScreen.ASSETS)) {
                InputStream stream = getAssets().open(levelPath);
                reader = new Scanner(stream);
            }
            else if (storageType.equals(SelectionScreen.INTERNAL)){
                File file = new File(getApplicationContext().getFilesDir(), levelPath);
                reader = new Scanner(file);
            }
            else{
                throw new Exception("Wrong storage type");
            }
            map = parser.Parse(reader);

            // Initialization of the AppConstants class
            AppConstants.Initialization(this.getApplicationContext(), map);

            SurfaceView view = new GameView(this, AppConstants.getEngine());
            setContentView(view);

        } catch (Exception e) {
            e.printStackTrace();
            super.finish();
            String x = e.getMessage();
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