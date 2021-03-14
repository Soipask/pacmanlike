package com.example.pacmanlike.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.pacmanlike.R;
import com.example.pacmanlike.gamemap.GameMap;
import com.example.pacmanlike.levelmakerstages.ChooseNameStage;
import com.example.pacmanlike.levelmakerstages.ImportExportStage;
import com.example.pacmanlike.levelmakerstages.OneTimesStage;
import com.example.pacmanlike.levelmakerstages.PacStage;
import com.example.pacmanlike.levelmakerstages.PowersStage;
import com.example.pacmanlike.levelmakerstages.PreSaveStage;
import com.example.pacmanlike.levelmakerstages.RepeatableStage;
import com.example.pacmanlike.levelmakerstages.ReturnStage;
import com.example.pacmanlike.levelmakerstages.StageInterface;
import com.example.pacmanlike.main.AppConstants;
import com.example.pacmanlike.objects.Vector;
import com.example.pacmanlike.view.MapSquare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class LevelMakerActivity extends AppCompatActivity {
    private int _tileSize;
    private int _selected;
    private final MapSquare[][] _mapSquares = new MapSquare[AppConstants.MAP_SIZE_Y][AppConstants.MAP_SIZE_X];
    private final HashMap<Integer, MapSquare> _buttonDictionary = new HashMap<>();
    private final HashMap<Integer, Character> _viewChars = new HashMap<>();
    private final HashMap<Integer, Integer> _buttonBackgroundResource = new HashMap<>();
    private final HashSet<Integer> _oneTimeIds = new HashSet();

    private HorizontalScrollView _oneTimeButtons, _repeatableButtons;
    private TextView _instructions, _mapName;

    private StageEnum stage = StageEnum.ONE_TIMES;

    private final GameMap _gameMap = new GameMap();

    private ImageButton _pacPosition;
    private final ImageButton[] _powerPellets = new ImageButton[AppConstants.MAX_POWER_PELLETS];
    private final ArrayList<Vector> _powerPelletVectors = new ArrayList<>();

    public String levelString, levelName;

    // last stage doesn't need its own place as it doesn't have implementation of StageInterface
    // after clicking it just returns
    private StageInterface[] _stages = new StageInterface[StageEnum.count - 1];

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_maker);

        _tileSize = 125;

        _oneTimeButtons = (HorizontalScrollView) findViewById(R.id.oneTimeButtonsScrollView);
        _repeatableButtons = (HorizontalScrollView) findViewById(R.id.repeatableButtonsScrollView);
        _instructions = (TextView) findViewById(R.id.instructionView);
        _mapName = (TextView) findViewById(R.id.map_name);

        // onclick listeners for oneTimeButtons
        setOnClickToButtons();

        // onChangeSelected listeners for repeatable ones
        setOnChangeSelectedToRadio();

        init();
    }

    private void setOnClickToButtons(){
        LinearLayout layout = (LinearLayout) _oneTimeButtons.getChildAt(0);
        for(int i = 0; i < layout.getChildCount(); i++){
            View btn = layout.getChildAt(i);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void init(){
        prepareTiles();

        _oneTimeButtons.setVisibility(View.VISIBLE);
        _repeatableButtons.setVisibility(View.INVISIBLE);
        _instructions.setVisibility(View.INVISIBLE);
        _mapName.setVisibility(View.INVISIBLE);

        Button btn = (Button) findViewById(R.id.continueButton);
        TextView textView = (TextView) findViewById(R.id.editTextTextMultiLine);
        btn.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);

        _selected = R.id.emptyButton;
        fillViewChars();
        fillBackgroundDictionary();
        setOneTimeIds();

        // We would like to have this list operating more as an array for simplicity
        for (int i = 0; i < 4; i++){
            _powerPelletVectors.add(null);
        }

        setStages();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void prepareTiles(){
        TableLayout tbl = (TableLayout) findViewById(R.id.newLevel);

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
                _mapSquares[i][j] = square;
                square.button = button;

                _buttonDictionary.put(id, square);
                row.addView(button, p);

                button.setBackgroundResource(R.drawable.emptygrey);
                button.setOnClickListener(v -> {
                    try {
                        onClickMap(button);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
        tbl.requestLayout();
    }

    private void onClickMap(ImageButton button) throws Exception {
        int id = button.getId();
        MapSquare square = _buttonDictionary.get(id);

        _stages[stage.getValue()].onClickMap(square, button, _selected);

        if (stage == StageEnum.ONE_TIMES){
            _selected = R.id.emptyButton;
        }
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
        _viewChars.put(R.id.doorButton, 'D');
    }

    private void fillBackgroundDictionary(){
        // repeatable
        _buttonBackgroundResource.put(R.id.emptyButton, R.drawable.empty);
        _buttonBackgroundResource.put(R.id.crossroadButton, R.drawable.crossroad);
        _buttonBackgroundResource.put(R.id.halfCrossroadButton, R.drawable.halfcrossroad);
        _buttonBackgroundResource.put(R.id.turnButton, R.drawable.turn);
        _buttonBackgroundResource.put(R.id.straightButton, R.drawable.straight);

        // one time
        _buttonBackgroundResource.put(R.id.leftTeleportButton, R.drawable.leftteleport);
        _buttonBackgroundResource.put(R.id.rightTeleportButton, R.drawable.rightteleport);
        _buttonBackgroundResource.put(R.id.doorButton, R.drawable.door);
    }

    private void setOneTimeIds(){
        _oneTimeIds.add(R.id.leftTeleportButton);
        _oneTimeIds.add(R.id.rightTeleportButton);
        _oneTimeIds.add(R.id.doorButton);
    }

    private void setStages(){
        _stages[StageEnum.ONE_TIMES.getValue()] = new OneTimesStage(this, _mapSquares, _buttonBackgroundResource, _viewChars);
        _stages[StageEnum.REPEATABLE.getValue()] = new RepeatableStage(this, _buttonBackgroundResource, _viewChars);
        _stages[StageEnum.PAC.getValue()] = new PacStage(this, _gameMap);
        _stages[StageEnum.POWERS.getValue()] = new PowersStage(this, _powerPellets, _powerPelletVectors, _gameMap, _mapSquares);
        _stages[StageEnum.PRE_SAVE.getValue()] = new PreSaveStage(this, _gameMap);
        _stages[StageEnum.CHOOSE_NAME.getValue()] = new ChooseNameStage(this);
        _stages[StageEnum.RETURN.getValue()] = new ReturnStage(this);
        _stages[StageEnum.IMPORT_EXPORT.getValue()] = new ImportExportStage(this);
    }

    public void goToNextStage() throws Exception {
        Button continueButton = (Button) findViewById(R.id.continueButton);
        Button ieButton = (Button) findViewById(R.id.importExportButton);

        if (stage == StageEnum.END)
        {
            // go back to previous activity
            super.finish();
        } else{
            stage = _stages[stage.getValue()].nextStageClick(continueButton, ieButton);
        }
    }

    public void onContinueClick(View view) throws Exception {
        goToNextStage();
    }

    public void onImportExportClick(View view){
        Button btn = (Button) findViewById(R.id.continueButton);
        Button exportButton = (Button) findViewById(R.id.importExportButton);
        TextView textView = (TextView) findViewById(R.id.editTextTextMultiLine);

        stage = _stages[stage.getValue()].onImportExportClick(btn, exportButton, textView);
    }

    public enum StageEnum {
        ONE_TIMES(0),
        REPEATABLE(1),
        PAC(2),
        POWERS(3),
        PRE_SAVE(4),
        CHOOSE_NAME(5),
        RETURN(6),
        IMPORT_EXPORT(7),
        END(8);

        // Iteration value
        private int value;

        // Index
        StageEnum(int value) { this.value = value; }

        // StageEnum to integer
        public int getValue() { return value; }

        public static int count = 9;
    }
}