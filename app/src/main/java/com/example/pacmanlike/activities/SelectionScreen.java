package com.example.pacmanlike.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;

import com.example.pacmanlike.R;
import com.example.pacmanlike.gamemap.GameMap;
import com.example.pacmanlike.view.LevelParser;
import com.example.pacmanlike.view.LevelView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SelectionScreen extends AppCompatActivity {
    public static final String SELECTED_LEVEL = "com.example.pacmanlike.SELECTED_LEVEL";
    public static final String STORAGE_TYPE = "com.example.pacmanlike.SELECTED_LEVEL_STORAGE";
    public static final String ASSETS = "Assets";
    public static final String INTERNAL = "Internal";
    public static final String BASIC_MAP = "basicmap.csv";

    private final HashMap<Integer, String> mapDictionary = new HashMap<Integer, String>();
    public static final ArrayList<String> internalMaps = new ArrayList<String>();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_screen);

        // Find radio group and populate it with basic map
        RadioGroup rgp = (RadioGroup) findViewById(R.id.radioGroup);
        mapDictionary.put(R.id.radioButton, BASIC_MAP);
        internalMaps.add(BASIC_MAP);

        Context context = getApplicationContext();
        String[] files = context.fileList();
        ArrayList<String> mapNames = filterMaps(files);

        // Populate radio group with map names
        for (int i = 0; i < mapNames.size(); i++) {
            RadioButton rbn = new RadioButton(this);
            int id = View.generateViewId();
            String mapName = mapNames.get(i);
            mapDictionary.put(id, mapName);
            rbn.setId(id);
            rbn.setText(mapName);
            rgp.addView(rbn);
        }

        LevelParser parser = new LevelParser();
        showMapPreview(R.id.radioButton, parser);

        rgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                TableLayout table = (TableLayout) findViewById(R.id.table);
                table.removeAllViews();
                showMapPreview(checkedId, parser);
            }
        });
    }

    public void showMapPreview(int checkedId, LevelParser parser){
        try {
            parser.init(mapDictionary.get(checkedId), this);
            GameMap map = parser.parse();
            LevelView levelView = new LevelView(this, map);
            levelView.setTileSize(70);
            levelView.createLevel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createLevel(View view){
        Intent intent = new Intent(this, LevelMakerActivity.class);
        startActivity(intent);
    }

    public void startGame(View view){
        Intent intent = new Intent(this, GameScreen.class);

        // Get RadioGroup for selecting level and see what's checked
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        int level = radioGroup.getCheckedRadioButtonId();

        String levelPath = mapDictionary.get(level);

        // Select which storage type should be used as the basic map is stored in Assets
        // Custom maps are stored in internal storage
        intent.putExtra(SELECTED_LEVEL, levelPath);

        startActivity(intent);
    }

    private ArrayList<String> filterMaps (String[] files){
        ArrayList<String> maps = new ArrayList<String>();

        Collections.addAll(maps, files);

        return maps;
    }
}