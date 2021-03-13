package com.example.pacmanlike.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.pacmanlike.R;
import com.example.pacmanlike.main.AppConstants;

public class GameOverActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        Intent intent = getIntent();

        Integer score = AppConstants.getEngine().getScore();


        if(AppConstants.getEngine().isGameOver()){
            // GameOver

        } else {
            // Victory
        }

        TextView scoreView = (TextView) findViewById(R.id.score_box);

        scoreView.setText(getResources().getString(R.string.score_text) + ' ' + String.valueOf(score));
    }

    public void onRetryClick(View view){

        Intent intent = new Intent(this, SelectionScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}