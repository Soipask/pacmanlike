package com.example.pacmanlike.levelmakerstages;

import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pacmanlike.R;
import com.example.pacmanlike.activities.LevelMakerActivity;
import com.example.pacmanlike.gamemap.GameMap;
import com.example.pacmanlike.view.ImportExportMap;
import com.example.pacmanlike.view.LevelParser;
import com.example.pacmanlike.view.MapSquare;

/**
 * Stage of the level making process.
 * Process goes to this stage
 * when using ImportExportButton.
 *
 * User is shown textbox, where he can paste the export map code
 * when clicking on Import map... button in ONE_TIMES Stage
 * or copy the export map code he created
 * when clicking on Export map button in PRE_SAVE Stage.
 */
public class ImportExportStage implements StageInterface {
    public final LevelMakerActivity _activity;

    private final HorizontalScrollView _oneTimeButtons;
    private TextView _instructions, _mapName;

    public ImportExportStage(LevelMakerActivity activity){
        _activity = activity;

        _instructions = (TextView) _activity.findViewById(R.id.instructionView);
        _oneTimeButtons = (HorizontalScrollView) _activity.findViewById(R.id.oneTimeButtonsScrollView);
        _mapName = (TextView) _activity.findViewById(R.id.map_name);
    }

    @Override
    public void onClickMap(MapSquare square, ImageButton button, int selected) throws Exception {
        // Should do nothing during this stage
    }

    @Override
    public LevelMakerActivity.StageEnum nextStageClick(Button continueButton, Button ieButton) throws Exception {
        // should not be called during this stage
        throw new Exception("Wrong stage called");
    }

    /**
     * Evaluates if the written text in the textView can be used as a new map.
     * If not - writes warning and keeps the stage.
     * If yes - changes stage to name choosing stage for this map.
     * @param continueButton Continue Button
     * @param ieButton Import-Export Button
     * @param textView Multiline textView from importing/exporting the map
     * @return Next stage
     */
    @Override
    public LevelMakerActivity.StageEnum onImportExportClick(Button continueButton, Button ieButton, TextView textView) {
        String mapCode = textView.getText().toString();
        _activity.levelString = ImportExportMap.importMap(mapCode);

        // try parse to see if it works
        try{
            LevelParser parser = new LevelParser();
            parser.initImportControl(_activity.levelString);
            GameMap map = parser.parse();
        } catch (Exception e){
            // if parse failed, give user a second chance
            _instructions.setText(R.string.instructions_failed_import);
            return LevelMakerActivity.StageEnum.IMPORT_EXPORT;
        }

        // set correct visibilities
        _instructions.setText(R.string.instructions_mapname);
        _instructions.setVisibility(View.VISIBLE);
        continueButton.setText(R.string.save_button);
        _mapName.setVisibility(View.VISIBLE);
        continueButton.setVisibility(View.VISIBLE);
        ieButton.setVisibility(View.INVISIBLE);
        _oneTimeButtons.setVisibility(View.INVISIBLE);

        return LevelMakerActivity.StageEnum.CHOOSE_NAME;
    }


}
