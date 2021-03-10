package com.example.pacmanlike.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.pacmanlike.R;
import com.example.pacmanlike.main.AppConstants;
import com.example.pacmanlike.view.LevelParser;
import com.example.pacmanlike.objects.Vector;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LevelMakerActivity extends AppCompatActivity {
    private int _tileSize;
    private char _selected;
    private MapSquare[][] _map = new MapSquare[AppConstants.MAP_SIZE_X][AppConstants.MAP_SIZE_Y];
    private HashMap<Integer, MapSquare> _buttonDictionary = new HashMap<Integer, MapSquare>();
    private View _lastClicked;

    private View _oneTimeButtons, _repeatableButtons;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_maker);

        TableLayout tbl = (TableLayout) findViewById(R.id.newLevel);
        /*
        _tileSize = Math.min (
                (AppConstants.SCREEN_HEIGHT - 300) / AppConstants.MAP_SIZE_Y,
                (AppConstants.SCREEN_WIDTH - 64) / AppConstants.MAP_SIZE_X
        );
        */
        _tileSize = 125;

        //tbl.getLayoutParams().height = LevelParser.MAP_SIZE_Y * _tileSize;
        //tbl.getLayoutParams().width = LevelParser.MAP_SIZE_X * _tileSize;

        for (int i = 0; i < AppConstants.MAP_SIZE_Y; i++) {
            TableRow row = new TableRow(this);

            TableLayout.LayoutParams params = new TableLayout.LayoutParams();
            params.topMargin = 10;

            tbl.addView(row, params);

            for (int j = 0; j < AppConstants.MAP_SIZE_X; j++) {
                ImageButton button = new ImageButton(this);

                TableRow.LayoutParams p = new TableRow.LayoutParams();
                p.rightMargin = 10;
                p.width = _tileSize;
                p.height = _tileSize;

                int id = View.generateViewId();
                button.setId(id);

                MapSquare square = new MapSquare();
                square.position = new Vector(j, i);

                _buttonDictionary.put(id,square);
                row.addView(button,p);

                button.setBackgroundResource(R.drawable.empty);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        button.setBackground(_lastClicked.getBackground());

                        int id = v.getId();
                        MapSquare square = _buttonDictionary.get(id);
                        square.letter = _selected;
                        square.rotation = v.getRotation();
                    }
                });
            }
        }
        tbl.requestLayout();

        _oneTimeButtons = findViewById(R.id.oneTimeButtonsScrollView);
        _oneTimeButtons.setVisibility(View.INVISIBLE);

        // onclick listeners for oneTimeButtons
        // onChangeSelected listeners for repeatable ones
        // all need to change _lastChecked

        _lastClicked = findViewById(R.id.emptyButton);
    }

    public class MapSquare{
        public char letter = 'E';
        public float rotation = 0;
        Vector position;

        @Override
        public String toString() {
            return letter + String.valueOf(rotation);
        }
    }
}