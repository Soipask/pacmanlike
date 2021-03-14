package com.example.pacmanlike.levelmakerstages;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.pacmanlike.activities.LevelMakerActivity;
import com.example.pacmanlike.view.MapSquare;

public interface StageInterface {
    public void onClickMap(MapSquare square, ImageButton button, int selected) throws Exception;
    public LevelMakerActivity.StageEnum nextStageClick(Button continueButton, Button ieButton) throws Exception;
    public LevelMakerActivity.StageEnum onImportExportClick(Button continueButton, Button ieButton, TextView textView);
}
