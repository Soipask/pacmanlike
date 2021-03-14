package com.example.pacmanlike.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.pacmanlike.R;
import com.example.pacmanlike.gamemap.GameMap;
import com.example.pacmanlike.view.ImportExportMap;
import com.example.pacmanlike.main.AppConstants;
import com.example.pacmanlike.view.LevelParser;
import com.example.pacmanlike.view.LevelView;

import java.util.ArrayList;
import java.util.HashMap;

public class SelectionScreen extends AppCompatActivity {
    public static final String BASIC_MAP = "basicmap.csv";
    public static final String NARROW_MAP = "narrowmap.csv";

    private final HashMap<Integer, String> _mapDictionary = new HashMap<Integer, String>();
    public static final ArrayList<String> _internalMaps = new ArrayList<String>();

    private Button _exportBtn;
    private TextView _textView;
    private int _currentCheckedId;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_screen);

        // Find radio group and populate it with basic maps
        RadioGroup rgp = (RadioGroup) findViewById(R.id.radioGroup);
        _mapDictionary.put(R.id.basicMapRadio, BASIC_MAP);
        _internalMaps.add(BASIC_MAP);
        _mapDictionary.put(R.id.narrowMapRadio, NARROW_MAP);
        _internalMaps.add(NARROW_MAP);

        Context context = getApplicationContext();
        String[] files = context.fileList();
        ArrayList<String> mapNames = filterMaps(files);

        // Populate radio group with map names
        for (int i = 0; i < mapNames.size(); i++) {
            RadioButton rbn = new RadioButton(this);
            int id = View.generateViewId();
            String mapName = mapNames.get(i);
            _mapDictionary.put(id, mapName);
            rbn.setId(id);
            rbn.setText(mapName);
            rgp.addView(rbn);
        }

        LevelParser parser = new LevelParser();
        showMapPreview(R.id.basicMapRadio, parser);

        rgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                TableLayout table = (TableLayout) findViewById(R.id.table);
                table.removeAllViews();
                showMapPreview(checkedId, parser);
                showExportButton(checkedId);
                _currentCheckedId = checkedId;
            }
        });

        _exportBtn = (Button) findViewById(R.id.exportButton);
        _exportBtn.setVisibility(View.INVISIBLE);

        _textView = (TextView) findViewById(R.id.selectionTextView);
        _textView.setVisibility(View.INVISIBLE);
    }

    public void showMapPreview(int checkedId, LevelParser parser){
        try {
            parser.init(_mapDictionary.get(checkedId), this);
            GameMap map = parser.parse();
            LevelView levelView = new LevelView(this, map);
            levelView.setTileSize(70);
            levelView.createLevel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showExportButton(int checkedId){
        // Export button is only shown when custom made map is selected
        if (_internalMaps.contains(_mapDictionary.get(checkedId))){
            _exportBtn.setVisibility(View.INVISIBLE);
        } else _exportBtn.setVisibility(View.VISIBLE);
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

        String levelPath = _mapDictionary.get(level);

        // Select which storage type should be used as the basic map is stored in Assets
        // Custom maps are stored in internal storage
        intent.putExtra(AppConstants.SELECTED_LEVEL, levelPath);

        startActivity(intent);
    }

    public void exportMap(View view) throws Exception {
        LevelParser parser = new LevelParser();
        parser.init(_mapDictionary.get(_currentCheckedId), this);
        GameMap map = parser.parse();

        String levelString = map.toString();
        String exportString = ImportExportMap.exportMap(levelString);
        ImportExportMap.copyToClipboard(this, exportString);

        _textView.setVisibility(View.VISIBLE);
    }

    private ArrayList<String> filterMaps (String[] files){
        ArrayList<String> maps = new ArrayList<String>();

        for(int i = 0; i < files.length; i++){
            if (files[i].endsWith(".csv")) maps.add(files[i]);
        }

        return maps;
    }
}