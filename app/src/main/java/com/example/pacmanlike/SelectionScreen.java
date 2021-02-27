package com.example.pacmanlike;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SelectionScreen extends AppCompatActivity {
    public static final String SELECTED_LEVEL = "com.example.pacmanlike.SELECTED_LEVEL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_screen);
    }

    public void createLevel(View view){
        Intent intent = new Intent(this, LevelMaker.class);
        startActivity(intent);
    }

    public void startGame(View view){
        Intent intent = new Intent(this, GameScreen.class);

        // TODO: Send some basic info to GameScreen through intent like
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        int level = radioGroup.getCheckedRadioButtonId();
        // look at dictionary which level has this id, then get this level's path and put it to intent
        String levelPath = "basicmap";
        intent.putExtra(SELECTED_LEVEL, levelPath);

        startActivity(intent);
    }
}