package com.example.pacmanlike;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;

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

        try {
            parser.Init(levelPath, this);
            map = parser.Parse();
        } catch (Exception e) {
            e.printStackTrace();
            super.finish();
        }

        LevelView levelView = new LevelView(this, map);
        levelView.CreateLevel();

    }
}