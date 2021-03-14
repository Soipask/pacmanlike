package com.example.pacmanlike.levelmakerstages;

import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pacmanlike.R;
import com.example.pacmanlike.activities.LevelMakerActivity;
import com.example.pacmanlike.gamemap.GameMap;
import com.example.pacmanlike.main.AppConstants;
import com.example.pacmanlike.view.MapSquare;

import java.util.HashMap;

/**
 * Stage 3 of the level making process.
 * User chooses a tile where pacman starts.
 */
public class PacStage implements StageInterface {

    private final LevelMakerActivity _activity;
    private final GameMap _gameMap;

    private ImageButton _pacPosition;
    private final TextView _instructions;

    public PacStage(LevelMakerActivity activity, GameMap gameMap){
        _activity = activity;
        _gameMap = gameMap;

        _instructions = (TextView) _activity.findViewById(R.id.instructionView);
    }

    /**
     * When clicked on map during this stage,
     * the button clicked on is selected as a starting position for Pacman,
     * and shows his icon there.
     * If his icon was already there somewhere, removes it from there.
     * @param square MapSquare belonging to clicked on View
     * @param button Clicked on Button
     * @param selected int of selected Button
     */
    @Override
    public void onClickMap(MapSquare square, ImageButton button, int selected) throws Exception {
        if (square.letter == AppConstants.CHAR_EMPTY_TILE || square.letter == AppConstants.CHAR_HOME_TILE)
            return;

        _gameMap.setStartingPacPosition(square.position);
        Button btn = (Button) _activity.findViewById(R.id.continueButton);
        btn.setVisibility(View.VISIBLE);
        button.setImageResource(R.drawable.pacman_right2);

        if (_pacPosition != null){
            _pacPosition.setImageResource(0);
        }

        _pacPosition = button;
    }

    /**
     * Sets everything for the next stage:
     * Powers stage
     * @param continueButton Continue Button
     * @param ieButton Import-Export Button
     * @return Next stage
     */
    @Override
    public LevelMakerActivity.StageEnum nextStageClick(Button continueButton, Button ieButton) throws Exception {
        _instructions.setText(R.string.select_power_pos);
        continueButton.setText(R.string.validate_button);
        return LevelMakerActivity.StageEnum.POWERS;
    }

    @Override
    public LevelMakerActivity.StageEnum onImportExportClick(Button continueButton, Button ieButton, TextView textView) {
        // should not be called during this stage
        // if by any means is, do nothing
        return LevelMakerActivity.StageEnum.PAC;
    }


}
