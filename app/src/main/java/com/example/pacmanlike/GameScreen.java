package com.example.pacmanlike;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.io.InputStream;

public class GameScreen extends AppCompatActivity {
    GameMap map;
    LevelParser parser = new LevelParser();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        Intent intent = getIntent();
        String levelPath = intent.getStringExtra(SelectionScreen.SELECTED_LEVEL);

        try {
            InputStream stream = getAssets().open(levelPath);
            map = parser.Parse(stream);
        } catch (Exception e) {
            e.printStackTrace();
            super.finish();
            String x = e.getMessage();
        }
    }
}