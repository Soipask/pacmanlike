package com.example.pacmanlike;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.HashMap;

public class SelectionScreen extends AppCompatActivity {
    public static final String SELECTED_LEVEL = "com.example.pacmanlike.SELECTED_LEVEL";
    public static final String STORAGE_TYPE = "com.example.pacmanlike.SELECTED_LEVEL_STORAGE";
    public static final String ASSETS = "Assets";
    public static final String INTERNAL = "Internal";
    public static final String BASIC_MAP = "basicmap.csv";
    private HashMap<Integer, String> mapDictionary = new HashMap<Integer, String>();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_screen);

        RadioGroup rgp = (RadioGroup) findViewById(R.id.radioGroup);
        mapDictionary.put(R.id.radioButton, BASIC_MAP);

        Context context = getApplicationContext();
        String[] files = context.fileList();
        ArrayList<String> mapNames = filterMaps(files);

        for (int i = 0; i < mapNames.size(); i++) {
            RadioButton rbn = new RadioButton(this);
            int id = View.generateViewId();
            String mapName = mapNames.get(i);
            mapDictionary.put(id,mapName);
            rbn.setId(id);
            rbn.setText(mapName);
            rgp.addView(rbn);
        }
    }

    public void createLevel(View view){
        Intent intent = new Intent(this, LevelMakerActivity.class);
        startActivity(intent);
    }

    public void startGame(View view){
        Intent intent = new Intent(this, GameScreen.class);

        // TODO: Send some basic info to GameScreen through intent like
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        int level = radioGroup.getCheckedRadioButtonId();
        // look at dictionary which level has this id, then get this level's path and put it to intent

        String levelPath = mapDictionary.get(level);

        intent.putExtra(SELECTED_LEVEL, levelPath);
        if (level == R.id.radioButton){
            intent.putExtra(STORAGE_TYPE, ASSETS);
        }
        else{
            intent.putExtra(STORAGE_TYPE, INTERNAL);
        }

        startActivity(intent);
    }

    private ArrayList<String> filterMaps (String[] files){
        ArrayList<String> maps = new ArrayList<String>();

        for (int i = 0; i < files.length; i++){
            maps.add(files[i]);
        }

        return maps;
    }
}