package com.example.pacmanlike.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.pacmanlike.R;
import com.example.pacmanlike.main.AppConstants;
import com.example.pacmanlike.objects.Vector;

import java.util.HashMap;

public class LevelMakerActivity extends AppCompatActivity {
    private int _tileSize;
    private int _selected;
    private MapSquare[][] _map = new MapSquare[AppConstants.MAP_SIZE_X][AppConstants.MAP_SIZE_Y];
    private HashMap<Integer, MapSquare> _buttonDictionary = new HashMap<>();
    private HashMap<Integer, Character> _viewChars = new HashMap<>();
    private View _lastClicked;

    private HorizontalScrollView _oneTimeButtons, _repeatableButtons;

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
                        int id = v.getId();
                        MapSquare square = _buttonDictionary.get(id);

                        if (_selected != R.id.rotateButton){
                        //button.setBackground(ResourcesCompat.getDrawable(getResources(), _selected, null));
                            button.setBackground(_lastClicked.getBackground());

                            square.rotation = v.getRotation();
                            square.letter = _viewChars.get(_selected);
                        } else{
                            float newRotation = v.getRotation() + 90;
                            v.setRotation(newRotation);
                            square.rotation = newRotation;
                        }
                    }
                });
            }
        }
        tbl.requestLayout();

        _oneTimeButtons = (HorizontalScrollView) findViewById(R.id.oneTimeButtonsScrollView);
        _repeatableButtons = (HorizontalScrollView) findViewById(R.id.repeatableButtonsScrollView);
        _oneTimeButtons.setVisibility(View.INVISIBLE);

        // onclick listeners for oneTimeButtons
        setOnClickToButtons();

        // onChangeSelected listeners for repeatable ones
        setOnChangeSelectedToRadio();

        _lastClicked = findViewById(R.id.emptyButton);
        _selected = R.id.emptyButton;
        fillViewChars();
    }

    private void setOnClickToButtons(){
        LinearLayout layout = (LinearLayout) _oneTimeButtons.getChildAt(0);
        for(int i = 0; i < layout.getChildCount(); i++){
            View btn = layout.getChildAt(i);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _lastClicked = v;
                    _selected = v.getId();
                }
            });
        }
    }

    private void setOnChangeSelectedToRadio(){
        RadioGroup radioGroup = (RadioGroup) _repeatableButtons.getChildAt(0);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                _selected = checkedId;
                _lastClicked = findViewById(checkedId);
            }
        });
    }

    private void fillViewChars(){
        // repeatable
        _viewChars.put(R.id.emptyButton, 'X');
        _viewChars.put(R.id.crossroadButton, 'C');
        _viewChars.put(R.id.halfCrossroadButton, 'H');
        _viewChars.put(R.id.turnButton, 'T');
        _viewChars.put(R.id.straightButton, 'S');

        // one-time
        _viewChars.put(R.id.leftTeleportButton, 'L');
        _viewChars.put(R.id.rightTeleportButton, 'R');
        // door ...
        // _viewChars.put(R.id.doorButton, 'D');
    }

    public class MapSquare{
        public char letter = 'X';
        public float rotation = 0;
        Vector position;

        @Override
        public String toString() {
            return letter + String.valueOf(rotation);
        }
    }
}