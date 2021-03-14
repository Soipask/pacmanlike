package com.example.pacmanlike.levelmakerstages;

import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.example.pacmanlike.R;
import com.example.pacmanlike.activities.LevelMakerActivity;
import com.example.pacmanlike.view.MapSquare;

import java.util.HashMap;

/**
 * Stage 2 of the level making process.
 * Repeatable tiles are being placed on a map.
 * They can be rotated using Rotate item in menu.
 */
public class RepeatableStage implements StageInterface {

    private final LevelMakerActivity _activity;
    private final HashMap<Integer, Integer> _buttonBackgroundResource;
    private final HashMap<Integer, Character> _viewChars;

    private final HorizontalScrollView _repeatableButtons;
    private final TextView _instructions;

    public RepeatableStage(LevelMakerActivity activity, HashMap<Integer, Integer> buttonBackgroundResource, HashMap<Integer, Character> viewChars){
        _activity = activity;
        _buttonBackgroundResource = buttonBackgroundResource;
        _viewChars = viewChars;

        _repeatableButtons = (HorizontalScrollView) _activity.findViewById(R.id.repeatableButtonsScrollView);
        _instructions = (TextView) _activity.findViewById(R.id.instructionView);
    }

    /**
     * When clicked on map during this stage, the button gets
     * the same background as selected button in RadioGroup.
     * @param square MapSquare belonging to clicked on View
     * @param button Clicked on Button
     * @param selected int of selected Button
     */
    @Override
    public void onClickMap(MapSquare square, ImageButton button, int selected) throws Exception {
        if (selected != R.id.rotateButton && !square.immovable){
            int backgroundId = _buttonBackgroundResource.get(selected);
            button.setBackground(ResourcesCompat.getDrawable(_activity.getResources(), backgroundId, null));

            square.argument = button.getRotation();
            square.letter = _viewChars.get(selected);

        } else if (selected == R.id.rotateButton){
            float newRotation = (button.getRotation() + 90) % 360;
            button.setRotation(newRotation);
            square.argument = newRotation;
        }
    }

    /**
     * Sets everything for the next stage:
     * Pac stage
     * @param continueButton Continue Button
     * @param ieButton Import-Export Button
     * @return Next stage
     */
    @Override
    public LevelMakerActivity.StageEnum nextStageClick(Button continueButton, Button ieButton) throws Exception {
        _repeatableButtons.setVisibility(View.INVISIBLE);
        _instructions.setVisibility(View.VISIBLE);
        continueButton.setVisibility(View.INVISIBLE);
        return LevelMakerActivity.StageEnum.PAC;
    }

    @Override
    public LevelMakerActivity.StageEnum onImportExportClick(Button continueButton, Button ieButton, TextView textView) {
        // should not be called during this stage
        // if by any means is, do nothing
        return LevelMakerActivity.StageEnum.REPEATABLE;
    }

}
