package com.example.pacmanlike.levelmakerstages;

import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pacmanlike.R;
import com.example.pacmanlike.activities.LevelMakerActivity;
import com.example.pacmanlike.view.MapSquare;

/**
 * Stage of the level making process.
 * If the map is not valid,
 * this stage is active.
 * User can go back to Repeatable Stage
 * and fix the map.
 */
public class ReturnStage implements StageInterface {

    private final LevelMakerActivity _activity;

    private final HorizontalScrollView _repeatableButtons;
    private final TextView _instructions;

    public ReturnStage(LevelMakerActivity activity) {
        _activity = activity;

        _instructions = (TextView) _activity.findViewById(R.id.instructionView);
        _repeatableButtons = (HorizontalScrollView) _activity.findViewById(R.id.repeatableButtonsScrollView);
    }

    @Override
    public void onClickMap(MapSquare square, ImageButton button, int selected) throws Exception {
        // Should do nothing during this stage
    }

    /**
     * Sets everything for the next stage:
     * Repeatable stage
     * @param continueButton Continue Button
     * @param ieButton Import-Export Button
     * @return Next stage
     */
    @Override
    public LevelMakerActivity.StageEnum nextStageClick(Button continueButton, Button ieButton) throws Exception {
        _instructions.setVisibility(View.INVISIBLE);
        _instructions.setText(R.string.select_pac_pos);
        continueButton.setText(R.string.continue_button);
        _repeatableButtons.setVisibility(View.VISIBLE);

        return LevelMakerActivity.StageEnum.REPEATABLE;
    }

    @Override
    public LevelMakerActivity.StageEnum onImportExportClick(Button continueButton, Button ieButton, TextView textView) {
        // should not be called during this stage
        // if by any means is, do nothing
        return LevelMakerActivity.StageEnum.REPEATABLE;
    }
}
