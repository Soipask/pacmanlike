package com.example.pacmanlike.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pacmanlike.R;
import com.example.pacmanlike.main.AppConstants;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class GameOverActivity extends AppCompatActivity {

    /**
     * Sets all textviews on needed values.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        Intent intent = getIntent();

        // Setting victory text
        if(!AppConstants.getEngine().isGameOver()) {
            TextView victory = (TextView) findViewById(R.id.game_over_box);
            victory.setText(R.string.victory_text);
        }

        // Setting score text
        Integer score = AppConstants.getEngine().getScore();
        TextView scoreView = (TextView) findViewById(R.id.score_box);

        scoreView.setText(getResources().getString(R.string.score_text) + ' ' + String.valueOf(score));

        // Setting and writing highscore text
        String levelName = intent.getStringExtra(AppConstants.LEVEL_NAME);
        File file = new File(getApplicationContext().getFilesDir(), levelName + AppConstants.HIGHSCORE_EXTENSION);
        boolean isHighscore = false;
        try {
            Scanner reader = new Scanner(file);
            String highscoreString = reader.nextLine();
            int highscore = Integer.parseInt(highscoreString);

            if (highscore < score)
                isHighscore = true;
        } catch (FileNotFoundException e) {
            isHighscore = true;
        } catch (NumberFormatException e) {
            isHighscore = true;
        }

        if (isHighscore){
            FileWriter writer = null;
            try {
                writer = new FileWriter(file);
                writer.write(String.valueOf(score));
                writer.flush();
                writer.close();
            } catch (IOException e) {
                // empty catch, just do nothing
            }
        } else {
            TextView hscbox = (TextView) findViewById(R.id.highscore_box);
            hscbox.setVisibility(View.INVISIBLE);
        }

    }

    public void onRetryClick(View view){

        Intent intent = new Intent(this, SelectionScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}