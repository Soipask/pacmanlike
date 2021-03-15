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

    private final ImageButton[] _powerPellets = new ImageButton[AppConstants.MAX_POWER_PELLETS];
    private final ArrayList<Vector> _powerPelletVectors = new ArrayList<>();

    public String levelString, levelName;

    // last stage doesn't need its own place as it doesn't have implementation of StageInterface
    // after clicking it just returns
    private final StageInterface[] _stages = new StageInterface[StageEnum.count - 1];

    /**
     * Initializes everything needed, especially tiles to the middle of the screen
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_maker);

        calculateTileSize();

        _oneTimeButtons = (HorizontalScrollView) findViewById(R.id.oneTimeButtonsScrollView);
        _repeatableButtons = (HorizontalScrollView) findViewById(R.id.repeatableButtonsScrollView);
        _instructions = (TextView) findViewById(R.id.instructionView);
        _mapName = (TextView) findViewById(R.id.map_name);

        // onclick listeners for oneTimeButtons
        setOnClickToButtons();

        // onChangeSelected listeners for repeatable ones
        setOnChangeSelectedToRadio();

        // Sets dictionaries and arrays
        fillViewChars();
        fillBackgroundDictionary();
        setOneTimeIds();

        setStages();

        init();
    }

    /**
     * Takes screen height and width and calculates which is the optimal tile size
     */
    private void calculateTileSize(){
        int maxHeightSize = (AppConstants.SCREEN_HEIGHT - AppConstants.BOTTOM_BAR_SIZE) /
                (AppConstants.MAP_SIZE_Y + AppConstants.LM_BLOCKSIZE_PADDING);
        int maxWidthSize = AppConstants.SCREEN_WIDTH /
                (AppConstants.MAP_SIZE_X + AppConstants.LM_BLOCKSIZE_PADDING);

        _tileSize = Math.min(maxHeightSize, maxWidthSize);
    }

    /**
     * Sets onClick to one time buttons
     */
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

    /**
     * Sets onClick to radio group in repeatable buttons section
     */
    private void setOnChangeSelectedToRadio(){
        RadioGroup radioGroup = (RadioGroup) _repeatableButtons.getChildAt(0);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                _selected = checkedId;
            }
        });
    }

    /**
     * Initializes Stage 1 - ONE_TIMES
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void init(){
        // Draws tiles
        prepareTiles();

        // Sets visibilities on bottom bars and buttons
        _oneTimeButtons.setVisibility(View.VISIBLE);
        _repeatableButtons.setVisibility(View.INVISIBLE);
        _instructions.setVisibility(View.INVISIBLE);
        _mapName.setVisibility(View.INVISIBLE);

        Button btn = (Button) findViewById(R.id.continueButton);
        TextView textView = (TextView) findViewById(R.id.editTextTextMultiLine);
        btn.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);

        // We would like to have this list operating more as an array for simplicity
        for (int i = 0; i < 4; i++){
            _powerPelletVectors.add(null);
        }

        _selected = R.id.emptyButton;
    }

    /**
     * Draws empty tiles in the middle of the map
     */
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

    /**
     * Selects what happens with the help of stages.
     * Acts as onClick for tiles representing maps.
     * <br>
     * <br>
     * These buttons can be clicked on whenever, but only in certain
     * stages it does something:
     * <ul>
     *     <li> ONE_TIMES (Stage 1): Tile gets background that was selected
     *      in the bottom bar and that button gets invisible.
     *     </li>
     *     <li> REPEATABLE (Stage 2): Tile gets background that was selected
     *          in the bottom bar's radio group or gets rotated if Rotate was selected.
     *     </li>
     *     <li> PAC (Stage 3): Sets pacman as image resource for the selected tile.
     *          If on tile was already selected, pacman gets deleted from the old tile.
     *     </li>
     *     <li> POWERS (Stage 4): Sets up to 4 power pellets (whole pacman circle for the simplicity)
     *          into the tiles that were clicked on, if user clicked on 5+ tiles, the last one's
     *          image gets deleted, so there are only 4 newest tiles.
     *     </li>
     * </ul>
     * @param button Tile of the map that was clicked on
     */
    private void onClickMap(ImageButton button) throws Exception {
        int id = button.getId();
        MapSquare square = _buttonDictionary.get(id);

        _stages[stage.getValue()].onClickMap(square, button, _selected);

        if (stage == StageEnum.ONE_TIMES){
            _selected = R.id.emptyButton;
        }
    }

    /**
     * Fills dictionary where key is id of a button (image or radio)
     * and value is the char representing it in MapSquare.
     */
    private void fillViewChars(){
        // repeatable
        _viewChars.put(R.id.emptyButton, AppConstants.CHAR_EMPTY);
        _viewChars.put(R.id.crossroadButton, AppConstants.CHAR_CROSSROAD);
        _viewChars.put(R.id.halfCrossroadButton, AppConstants.CHAR_HALFXROAD);
        _viewChars.put(R.id.turnButton, AppConstants.CHAR_TURN);
        _viewChars.put(R.id.straightButton, AppConstants.CHAR_STRAIGHT);

        // one-time
        _viewChars.put(R.id.leftTeleportButton, AppConstants.CHAR_LEFT_TELEPORT);
        _viewChars.put(R.id.rightTeleportButton, AppConstants.CHAR_RIGHT_TELEPORT);
        _viewChars.put(R.id.doorButton, AppConstants.CHAR_DOOR);
    }

    /**
     * Fills dictionary where keys are ids of a button and values are ids of its background
     */
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

    /**
     * Fills list of one time buttons for
     */
    private void setOneTimeIds(){
        _oneTimeIds.add(R.id.leftTeleportButton);
        _oneTimeIds.add(R.id.rightTeleportButton);
        _oneTimeIds.add(R.id.doorButton);
    }

    /**
     * Sets stages in the dictionary for easier work
     */
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

    /**
     * <p>Shifts to next stage, every stage except IMPORT has a role here.
     * Usually serves as a continue onClick call (except first stage)</p>
     * <br>
     * <ul>
     *     <li>
     *         ONE_TIMES (Stage 1), REPEATABLE (Stage 2) and PAC (Stage 3): Changes the layout to prepare for next stage and shifts to it
     *     </li>
     *     <li>
     *         POWERS (Stage 4): Validates the map and either goes back to RETURN stage (validation failed), or goes to the next stage
     *     </li>
     *     <li>
     *         PRE_SAVE (Stage 5): Changes the layout to prepare for next stage and shifts to it
     *     </li>
     *     <li>
     *         CHOOSE_NAME (Stage 6): Saves the map with the given name
     *         (if it contains only alphanumeric characters, otherwise writes info and stays on this stage)
     *         and shifts to the END stage
     *     </li>
     *     <li>
     *         RETURN: Prepares to go back to Stage 2.
     *     </li>
     *     <li>
     *         END: Closes the activity
     *     </li>
     * </ul>
     */
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

    /**
     * Click on Continue button
     * @param view View that has been clicked on
     */
    public void onContinueClick(View view) throws Exception {
        goToNextStage();
    }

    /**
     * <p>Click on Import/Export button.
     * This button is visible only in the following stages:</p>
     * <br>
     * <ul>
     *     <li>
     *         ONE_TIMES (Stage 1): Prepares for import ->
     *         mainly sets editable multiline textbox visible
     *     </li>
     *     <li>
     *         IMPORT: Validates the import string for map.
     *         If it is a correct map, goes right to CHOOSE_NAME stage
     *     </li>
     *     <li>
     *         CHOOSE_NAME: Prepares for export ->
     *         mainly shows export string and copies it to clipboard
     *     </li>
     * </ul>
     * @param view
     */
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