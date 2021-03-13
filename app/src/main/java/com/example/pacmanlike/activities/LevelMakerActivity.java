package com.example.pacmanlike.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

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
import com.example.pacmanlike.gamemap.TileFactory;
import com.example.pacmanlike.gamemap.tiles.Tile;
import com.example.pacmanlike.main.AppConstants;
import com.example.pacmanlike.objects.Direction;
import com.example.pacmanlike.objects.Vector;
import com.example.pacmanlike.view.ImportExportMap;
import com.example.pacmanlike.view.LevelParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class LevelMakerActivity extends AppCompatActivity {
    private int _tileSize;
    private int _selected;
    private MapSquare[][] _map = new MapSquare[AppConstants.MAP_SIZE_Y][AppConstants.MAP_SIZE_X];
    private HashMap<Integer, MapSquare> _buttonDictionary = new HashMap<>();
    private HashMap<Integer, Character> _viewChars = new HashMap<>();
    private HashMap<Integer, Integer> _buttonBackgroundResource = new HashMap<>();
    private HashSet<Integer> _oneTimeIds = new HashSet();

    private HorizontalScrollView _oneTimeButtons, _repeatableButtons;
    private TextView _instructions, _mapName;

    private Stage stage = Stage.ONE_TIMES;

    private GameMap _gameMap = new GameMap();

    private ImageButton _pacPosition;
    private ImageButton[] _powerPellets = new ImageButton[AppConstants.MAX_POWER_PELLETS];
    private ArrayList<Vector> _powerPelletVectors = new ArrayList<>();

    private String _levelString, _levelName;

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
                _map[i][j] = square;
                square.button = button;

                _buttonDictionary.put(id, square);
                row.addView(button, p);

                button.setBackgroundResource(R.drawable.emptygrey);
                button.setOnClickListener(v -> {
                    try {
                        onClickMap(v, button);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
        tbl.requestLayout();
    }

    private void onClickMap(View v, ImageButton button) throws Exception {
        int id = v.getId();
        MapSquare square = _buttonDictionary.get(id);

        switch (stage){
            case ONE_TIMES:
                oneTimesOnClickMap(v, square, button);
                break;
            case REPEATABLE:
                repeatableOnClickMap(v, square, button);
                break;
            case PAC:
                pacOnClickMap(square, button);
                break;
            case POWERS:
                powersOnClickMap(square, button);
                break;
        }
    }

    private void oneTimesOnClickMap(View v, MapSquare square, ImageButton button) throws Exception {
        if (!square.immovable){
            switch (_selected){
                case R.id.leftTeleportButton:
                    if (square.position.x == AppConstants.MAP_SIZE_X - 1) return;
                    break;
                case R.id.rightTeleportButton:
                    if (square.position.x == 0) return;
                    break;
                case R.id.doorButton:
                    if (square.position.y >= AppConstants.MAP_SIZE_Y - 1 ||
                            square.position.x == 0 ||
                            square.position.x == AppConstants.MAP_SIZE_X - 1 ||
                            !canPlaceHomeHere(square)
                    )
                        return;

                    // create 3 immutable home tiles under door tile
                    int y = square.position.y + 1;
                    for (int x = square.position.x - 1; x <= square.position.x + 1; x++ ){
                        _map[y][x].letter = 'A';
                        _map[y][x].argument = x - (square.position.x - 2);
                        _map[y][x].immovable = true;

                        _map[y][x].button.setBackgroundResource(R.drawable.empty);
                        _map[y][x].button.setImageResource(R.drawable.ghost_2_0);
                    }

                    break;
            }

            int backgroundId = _buttonBackgroundResource.get(_selected);
            button.setBackgroundResource(backgroundId);

            square.argument = v.getRotation();
            square.letter = _viewChars.get(_selected);

            findViewById(_selected).setVisibility(View.INVISIBLE);

            if (areChildrenInvisible(_oneTimeButtons)){
                goToNextStage();
            }

            _selected = R.id.emptyButton;
            square.immovable = true;
        }
    }

    private boolean canPlaceHomeHere(MapSquare square){
        int y = square.position.y + 1;
        for (int x = square.position.x - 1; x <= square.position.x + 1; x++ ){
            if (_map[y][x].immovable) return false;
        }

        return true;
    }

    private void repeatableOnClickMap(View v, MapSquare square, ImageButton button) throws Exception {
        if (_selected != R.id.rotateButton && !square.immovable){
            int backgroundId = _buttonBackgroundResource.get(_selected);
            button.setBackground(ResourcesCompat.getDrawable(getResources(), backgroundId, null));

            square.argument = v.getRotation();
            square.letter = _viewChars.get(_selected);

            if (_oneTimeIds.contains(_selected)){
                findViewById(_selected).setVisibility(View.INVISIBLE);

                if (areChildrenInvisible(_oneTimeButtons)){
                    goToNextStage();
                }

                _selected = R.id.emptyButton;
                square.immovable = true;
            }
        } else if (_selected == R.id.rotateButton){
            float newRotation = (v.getRotation() + 90) % 360;
            v.setRotation(newRotation);
            square.argument = newRotation;
        }
    }

    private boolean areChildrenInvisible(HorizontalScrollView scrollView){
        LinearLayout layout = (LinearLayout) _oneTimeButtons.getChildAt(0);
        for(int i = 0; i < layout.getChildCount(); i++){
            View btn = layout.getChildAt(i);
            if (btn.getVisibility() != View.INVISIBLE){
                return false;
            }
        }

        return true;
    }

    private void pacOnClickMap(MapSquare square, ImageButton button){
        if (square.letter == 'X' || square.letter == 'A') return;

        _gameMap.setStartingPacPosition(square.position);
        Button btn = (Button) findViewById(R.id.continueButton);
        btn.setVisibility(View.VISIBLE);
        button.setImageResource(R.drawable.pacman_right2);

        if (_pacPosition != null){
            _pacPosition.setImageResource(0);
        }

        _pacPosition = button;
    }

    private void powersOnClickMap(MapSquare square, ImageButton button){
        // if not position not in array, shift the whole array and keep the odd one
        if(_powerPelletVectors.contains(square.position) ||
            square.letter == 'X' || square.letter == 'A'
        )
            return;

        ImageButton theOddOne = _powerPellets[0];

        for (int i = 0; i < AppConstants.MAX_POWER_PELLETS - 1; i++){
            _powerPelletVectors.set(i, _powerPelletVectors.get(i + 1));
            _powerPellets[i] = _powerPellets[i+1];
        }

        _powerPelletVectors.set(AppConstants.MAX_POWER_PELLETS - 1, square.position);
        _powerPellets[AppConstants.MAX_POWER_PELLETS - 1] = button;

        // set image resource
        button.setImageResource(R.drawable.pacman_right);

        // delete image resource from the odd one if not null
        if (theOddOne != null){
            theOddOne.setImageResource(0);
        }
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

    private void goToNextStage() throws Exception {
        Button btn = (Button) findViewById(R.id.continueButton);
        Button exportButton = (Button) findViewById(R.id.importExportButton);
        switch (stage){
            case ONE_TIMES:
                stage = Stage.REPEATABLE;
                _oneTimeButtons.setVisibility(View.INVISIBLE);
                _repeatableButtons.setVisibility(View.VISIBLE);
                btn.setVisibility(View.VISIBLE);
                exportButton.setVisibility(View.INVISIBLE);
                break;
            case REPEATABLE:
                stage = Stage.PAC;
                _repeatableButtons.setVisibility(View.INVISIBLE);
                _instructions.setVisibility(View.VISIBLE);
                btn.setVisibility(View.INVISIBLE);
                break;
            case PAC:
                stage = Stage.POWERS;
                _instructions.setText(R.string.select_power_pos);
                btn.setText(R.string.validate_button);
                break;
            case POWERS:
                ArrayList<Vector> positions = new ArrayList<>();
                for (int i = 0; i < AppConstants.MAX_POWER_PELLETS; i++){
                    if (_powerPelletVectors.get(i) != null) positions.add(_powerPelletVectors.get(i));
                }
                _gameMap.setPowerPelletsPosition(positions);
                validation(btn);
                break;
            case PRE_SAVE:
                stage = Stage.CHOOSE_NAME;
                _instructions.setText(R.string.instructions_mapname);
                btn.setText(R.string.save_button);
                _levelString = _gameMap.toString();
                _mapName.setVisibility(View.VISIBLE);
                exportButton.setVisibility(View.VISIBLE);
                exportButton.setText(R.string.export_button);
                break;
            case CHOOSE_NAME:
                _levelName = _mapName.getText().toString();
                saveMap();
                stage = Stage.END;
                _instructions.setText(R.string.instructions_saved);
                btn.setText(R.string.return_button);
                _mapName.setVisibility(View.INVISIBLE);
                break;
            case RETURN:
                stage = Stage.REPEATABLE;
                _instructions.setVisibility(View.INVISIBLE);
                _instructions.setText(R.string.select_pac_pos);
                btn.setText(R.string.continue_button);
                _repeatableButtons.setVisibility(View.VISIBLE);
                break;
            case END:
                // go back to previous activity
                super.finish();
                break;
        }
    }

    private void validation(Button btn) throws Exception {
        // After clicking on button: Validation process
        // If maps not valid, display text and change to return state
        // else continue to save state
        buildMap();

        if (!isMapValid()){
            stage = Stage.RETURN;
            btn.setText(R.string.return_button);
            _instructions.setText(R.string.instructions_return);
            return;
        }

        stage = Stage.PRE_SAVE;
        btn.setText(R.string.continue_button);
        _instructions.setText(R.string.instructions_validated);
    }

    private boolean isMapValid(){
        Tile[][] map = _gameMap.getMap();
        for (int i = 0; i < AppConstants.MAP_SIZE_Y; i++){
            for (int j = 0; j < AppConstants.MAP_SIZE_X; j++){
                try{
                    if (!isTileValid(i , j, map))
                        return false;
                } catch (Exception e){
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isTileValid(int y, int x, Tile[][] map){
        // Tile is valid when from all the tiles you can go from here, you can get back
        Tile tile = map[y][x];
        boolean valid = true;
        if (tile.type.equals(AppConstants.LEFT_TELEPORT) ||
                tile.type.equals(AppConstants.RIGHT_TELEPORT) ||
                tile.type.equals(AppConstants.HOME_TILE)){
            return true;
        }

        List<Direction> moves = tile.getPossibleMoves();

        for (Direction dir : moves){
            switch (dir){
                case UP:
                    if (!map[y - 1][x].getPossibleMoves().contains(Direction.DOWN))
                        return false;
                    break;
                case DOWN:
                    if (!map[y + 1][x].getPossibleMoves().contains(Direction.UP))
                        return false;
                    break;
                case LEFT:
                    if (!map[y][x - 1].type.equals("LeftTeleport") &&
                            !map[y][x-1].getPossibleMoves().contains(Direction.RIGHT))
                        return false;
                    break;
                case RIGHT:
                    if (!map[y][x + 1].type.equals("RightTeleport") &&
                            !map[y][x+1].getPossibleMoves().contains(Direction.LEFT))
                        return false;
                    break;
            }
        }

        return valid;
    }

    private void buildMap() throws Exception {
        Tile[][] tileMap = new Tile[AppConstants.MAP_SIZE_Y][AppConstants.MAP_SIZE_X];

        for (int i = 0; i < AppConstants.MAP_SIZE_Y; i++){
            for (int j = 0; j < AppConstants.MAP_SIZE_X; j++){
                tileMap[i][j] = TileFactory.createTile(_map[i][j].toString());
            }
        }
        _gameMap.setMap(tileMap);
    }

    public void onContinueClick(View view) throws Exception {
        goToNextStage();
    }

    public void onImportExportClick(View view){
        Button btn = (Button) findViewById(R.id.continueButton);
        Button exportButton = (Button) findViewById(R.id.importExportButton);
        TextView textView = (TextView) findViewById(R.id.editTextTextMultiLine);

        switch (stage){
            case ONE_TIMES:
                // show multiline text to write
                textView.setVisibility(View.VISIBLE);

                // set continue to invisible
                btn.setVisibility(View.INVISIBLE);
                // set importexport to validate
                exportButton.setText(R.string.validate_button);
                _instructions.setText(R.string.instructions_import);
                // set stage to import
                stage = Stage.IMPORT;
                break;
            case IMPORT:
                String mapCode = textView.getText().toString();
                _levelString = ImportExportMap.importMap(mapCode);

                // try parse to see if it works
                try{
                    LevelParser parser = new LevelParser();
                    parser.initImportControl(_levelString);
                    GameMap map = parser.parse();
                } catch (Exception e){
                    // if parse failed, give user a second chance
                    _instructions.setText(R.string.instructions_failed_import);
                    break;
                }

                stage = Stage.CHOOSE_NAME;
                _instructions.setText(R.string.instructions_mapname);
                _instructions.setVisibility(View.VISIBLE);
                btn.setText(R.string.save_button);
                _mapName.setVisibility(View.VISIBLE);
                btn.setVisibility(View.VISIBLE);
                exportButton.setVisibility(View.INVISIBLE);
                _oneTimeButtons.setVisibility(View.INVISIBLE);
                break;
            case CHOOSE_NAME:
                String exportString = ImportExportMap.exportMap(_levelString);
                ImportExportMap.copyToClipboard(this, exportString);
                _instructions.setText(R.string.clipboard_copied);

                textView.setVisibility(View.VISIBLE);
                textView.setText(exportString);
                exportButton.setVisibility(View.INVISIBLE);

                break;
        }
    }

    private void saveMap() throws IOException {
        File parent = getApplicationContext().getFilesDir();

        File file = new File(parent, _levelName + AppConstants.CSV_EXTENSION);
        FileWriter writer = new FileWriter(file);
        writer.write(_levelString);
        writer.flush();
        writer.close();

        // Save highscore file for easier work
        File highscoreFile = new File(parent, _levelName + AppConstants.HIGHSCORE_EXTENSION);
        FileWriter hscwriter = new FileWriter(highscoreFile);
        hscwriter.write("0");
        hscwriter.flush();
        hscwriter.close();
    }

    public class MapSquare{
        public char letter = 'X';
        public float argument = 0;
        Vector position;
        public boolean immovable = false;
        ImageButton button;

        @Override
        public String toString() {
            return letter + String.valueOf((int) argument);
        }
    }

    public enum Stage {
        ONE_TIMES,
        REPEATABLE,
        PAC,
        POWERS,
        CHOOSE_NAME,
        PRE_SAVE,
        RETURN,
        IMPORT,
        END
    }
}