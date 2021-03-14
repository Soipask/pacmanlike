package com.example.pacmanlike.levelmakerstages;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pacmanlike.R;
import com.example.pacmanlike.activities.LevelMakerActivity;
import com.example.pacmanlike.gamemap.GameMap;
import com.example.pacmanlike.gamemap.TileFactory;
import com.example.pacmanlike.gamemap.tiles.Tile;
import com.example.pacmanlike.main.AppConstants;
import com.example.pacmanlike.objects.Direction;
import com.example.pacmanlike.objects.Vector;
import com.example.pacmanlike.view.MapSquare;

import java.util.ArrayList;
import java.util.List;

/**
 * Stage 4 of the level making process.
 * User can select up to 4 tiles
 * where power pellets will spawn.
 * At the end of the stage,
 * the map will be validated.
 */
public class PowersStage implements StageInterface {
    private final LevelMakerActivity _activity;
    private final GameMap _gameMap;
    private final ImageButton[] _powerPellets;
    private final ArrayList<Vector> _powerPelletVectors;
    private final MapSquare[][] _mapSquares;

    private final TextView _instructions;

    public PowersStage(LevelMakerActivity activity, ImageButton[] powerPellets, ArrayList<Vector> powerPelletVectors, GameMap gameMap, MapSquare[][] mapSquares){
        _activity = activity;
        _powerPellets = powerPellets;
        _powerPelletVectors = powerPelletVectors;
        _gameMap = gameMap;
        _mapSquares = mapSquares;

        _instructions = (TextView) _activity.findViewById(R.id.instructionView);
    }

    /**
     * When clicked on map during this stage,
     * the button clicked on is selected as a power pellet position,
     * and shows its icon there.
     * If there already were maximum amount of power pellets placed, it removes the last one.
     * @param square MapSquare belonging to clicked on View
     * @param button Clicked on Button
     * @param selected int of selected Button
     */
    @Override
    public void onClickMap(MapSquare square, ImageButton button, int selected) throws Exception {
        // if not position not in array, shift the whole array and keep the odd one
        if(_powerPelletVectors.contains(square.position) ||
                square.letter == AppConstants.CHAR_EMPTY_TILE ||
                square.letter == AppConstants.CHAR_HOME_TILE
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

    /**
     * Sets everything for the next stage:
     * Pre-save stage
     * If the map is not valid, it changes state to return.
     * @param continueButton Continue Button
     * @param ieButton Import-Export Button
     * @return Next stage
     */
    @Override
    public LevelMakerActivity.StageEnum nextStageClick(Button continueButton, Button ieButton) throws Exception {
        ArrayList<Vector> positions = new ArrayList<>();
        for (int i = 0; i < AppConstants.MAX_POWER_PELLETS; i++){
            if (_powerPelletVectors.get(i) != null) positions.add(_powerPelletVectors.get(i));
        }
        _gameMap.setPowerPelletsPosition(positions);

        // After clicking on button: Validation process
        // If maps not valid, display text and change to return state
        // else continue to save state
        buildMap();

        if (!isMapValid()){
            continueButton.setText(R.string.return_button);
            _instructions.setText(R.string.instructions_return);
            return LevelMakerActivity.StageEnum.RETURN;
        }

        continueButton.setText(R.string.continue_button);
        _instructions.setText(R.string.instructions_validated);


        return LevelMakerActivity.StageEnum.PRE_SAVE;
    }

    private void buildMap() throws Exception {
        Tile[][] tileMap = new Tile[AppConstants.MAP_SIZE_Y][AppConstants.MAP_SIZE_X];

        for (int i = 0; i < AppConstants.MAP_SIZE_Y; i++){
            for (int j = 0; j < AppConstants.MAP_SIZE_X; j++){
                tileMap[i][j] = TileFactory.createTile(_mapSquares[i][j].toString());
            }
        }
        _gameMap.setMap(tileMap);
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
        if (tile._type.equals(AppConstants.LEFT_TELEPORT) ||
                tile._type.equals(AppConstants.RIGHT_TELEPORT) ||
                tile._type.equals(AppConstants.HOME_TILE)){
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
                    if (!map[y][x - 1]._type.equals("LeftTeleport") &&
                            !map[y][x-1].getPossibleMoves().contains(Direction.RIGHT))
                        return false;
                    break;
                case RIGHT:
                    if (!map[y][x + 1]._type.equals("RightTeleport") &&
                            !map[y][x+1].getPossibleMoves().contains(Direction.LEFT))
                        return false;
                    break;
            }
        }

        return valid;
    }


    @Override
    public LevelMakerActivity.StageEnum onImportExportClick(Button continueButton, Button ieButton, TextView textView) {
        // should not be called during this stage
        // if by any means is, do nothing
        return LevelMakerActivity.StageEnum.POWERS;
    }
}
