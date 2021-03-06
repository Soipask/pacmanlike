package com.example.pacmanlike;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.io.File;
import java.io.InputStream;
import java.util.Scanner;

public class GameScreen extends AppCompatActivity {
    GameMap map;
    LevelParser parser = new LevelParser();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

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
        } catch (Exception e) {
            e.printStackTrace();
            super.finish();
            String x = e.getMessage();
        }

        LevelView levelView = new LevelView(this, map);
        levelView.CreateLevel();
    }
}