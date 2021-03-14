package com.example.pacmanlike.levelmakerstages;

import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pacmanlike.R;
import com.example.pacmanlike.activities.LevelMakerActivity;
import com.example.pacmanlike.main.AppConstants;
import com.example.pacmanlike.view.MapSquare;

import java.util.HashMap;

/**
 * First stage of the level making process.
 * One-time buttons (teleports and ghosts home)
 * are being placed on the map.
 * Each of these tiles has to be placed somewhere
 * and can be placed only one time
 */
public class OneTimesStage implements StageInterface {
    private final LevelMakerActivity _activity;
    private final MapSquare[][] _mapSquares;
    private final HashMap<Integer, Integer> _buttonBackgroundResource;
    private final HashMap<Integer, Character> _viewChars;

    private final HorizontalScrollView _oneTimeButtons, _repeatableButtons;
    private final TextView _instructions;

    public OneTimesStage(LevelMakerActivity activity, MapSquare[][] map, HashMap<Integer, Integer> buttonBackgroundResource, HashMap<Integer, Character> viewChars){
        _activity = activity;
        _mapSquares = map;
        _buttonBackgroundResource = buttonBackgroundResource;
        _viewChars = viewChars;

        _oneTimeButtons = (HorizontalScrollView) _activity.findViewById(R.id.oneTimeButtonsScrollView);
        _repeatableButtons = (HorizontalScrollView) _activity.findViewById(R.id.repeatableButtonsScrollView);
        _instructions = (TextView) _activity.findViewById(R.id.instructionView);
    }

    /**
     * When clicked on map during this stage, the chosen tile becomes invisible.
     * When all the tiles become invisible, the next stage begins.
     * @param square MapSquare belonging to clicked on View
     * @param button Clicked on Button
     * @param selected int of selected Button
     */
    @Override
    public void onClickMap(MapSquare square, ImageButton button, int selected) throws Exception {
        if (!square.immovable){
            switch (selected){
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
                        _mapSquares[y][x].letter = 'A';
                        _mapSquares[y][x].argument = x - (square.position.x - 2);
                        _mapSquares[y][x].immovable = true;

                        _mapSquares[y][x].button.setBackgroundResource(R.drawable.empty);
                        _mapSquares[y][x].button.setImageResource(R.drawable.ghost_2_0);
                    }

                    break;
            }

            int backgroundId = _buttonBackgroundResource.get(selected);
            button.setBackgroundResource(backgroundId);

            square.argument = button.getRotation();
            square.letter = _viewChars.get(selected);

            _activity.findViewById(selected).setVisibility(View.INVISIBLE);

            if (areChildrenInvisible(_oneTimeButtons)){
                _activity.goToNextStage();
            }

            square.immovable = true;
        }
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
        _oneTimeButtons.setVisibility(View.INVISIBLE);
        _repeatableButtons.setVisibility(View.VISIBLE);
        continueButton.setVisibility(View.VISIBLE);
        ieButton.setVisibility(View.INVISIBLE);

        return LevelMakerActivity.StageEnum.REPEATABLE;
    }

    /**
     * Sets everything for map importing stage.
     * @param continueButton Continue Button
     * @param ieButton Import-Export Button
     * @param textView Multiline textView from importing/exporting the map
     * @return Next stage
     */
    @Override
    public LevelMakerActivity.StageEnum onImportExportClick(Button continueButton, Button ieButton, TextView textView) {
        // show multiline text to write
        textView.setVisibility(View.VISIBLE);

        // set continue to invisible
        continueButton.setVisibility(View.INVISIBLE);

        // set importexport to validate and instructions for import
        ieButton.setText(R.string.validate_button);
        _instructions.setText(R.string.instructions_import);

        // set stage to import
        return LevelMakerActivity.StageEnum.IMPORT_EXPORT;
    }

    private boolean canPlaceHomeHere(MapSquare square){
        int y = square.position.y + 1;
        for (int x = square.position.x - 1; x <= square.position.x + 1; x++ ){
            if (_mapSquares[y][x].immovable) return false;
        }

        return true;
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

}
