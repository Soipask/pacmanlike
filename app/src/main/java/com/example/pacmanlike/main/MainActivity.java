package com.example.pacmanlike.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.pacmanlike.R;
import com.example.pacmanlike.activities.SelectionScreen;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startSelection(View view) {
        Intent intent = new Intent(this, SelectionScreen.class);
        startActivity(intent);
    }

    public void exit(View view){
        super.finish();
    }
}