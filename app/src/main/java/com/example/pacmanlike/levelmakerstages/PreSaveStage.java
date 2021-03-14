package com.example.pacmanlike.levelmakerstages;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pacmanlike.R;
import com.example.pacmanlike.activities.LevelMakerActivity;
import com.example.pacmanlike.gamemap.GameMap;
import com.example.pacmanlike.view.MapSquare;

/**
 * Stage 5 of the level making process.
 * User can either choose to save the map
 * or export the map.
 */
public class PreSaveStage implements StageInterface {

    private final LevelMakerActivity _activity;
    private final GameMap _gameMap;

    private final TextView _instructions, _mapName;

    public PreSaveStage(LevelMakerActivity activity, GameMap gameMap) {
        _activity = activity;
        _gameMap = gameMap;

        _instructions = (TextView) _activity.findViewById(R.id.instructionView);
        _mapName = (TextView) _activity.findViewById(R.id.map_name);
    }

    @Override
    public void onClickMap(MapSquare square, ImageButton button, int selected) throws Exception {
        // Should do nothing during this stage
    }

    /**
     * Sets everything for the next stage:
     * Choose name stage
     * @param continueButton Continue Button
     * @param ieButton Import-Export Button
     * @return Next stage
     */
    @Override
    public LevelMakerActivity.StageEnum nextStageClick(Button continueButton, Button ieButton) throws Exception {
        _instructions.setText(R.string.instructions_mapname);
        continueButton.setText(R.string.save_button);
        _activity.levelString = _gameMap.toString();
        _mapName.setVisibility(View.VISIBLE);
        ieButton.setVisibility(View.VISIBLE);
        ieButton.setText(R.string.export_button);

        return LevelMakerActivity.StageEnum.CHOOSE_NAME;
    }

    @Override
    public LevelMakerActivity.StageEnum onImportExportClick(Button continueButton, Button ieButton, TextView textView) {
        // should not be called during this stage
        // if by any means is, do nothing
        return LevelMakerActivity.StageEnum.PRE_SAVE;
    }
}
