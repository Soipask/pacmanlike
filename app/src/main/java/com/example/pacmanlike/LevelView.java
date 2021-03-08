package com.example.pacmanlike;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

public class LevelView {
    AppCompatActivity activity;
    GameMap map;
    int tileSize = 100;

    public LevelView(AppCompatActivity activity, GameMap map){
        this.activity = activity;
        this.map = map;
    }

    public void CreateLevel(){
        // Get table
        // RadioGroup rgp = (RadioGroup) findViewById(R.id.radioGroup);
        TableLayout tbl = (TableLayout) activity.findViewById(R.id.table);
        tbl.getLayoutParams().height = LevelParser.MAP_SIZE_Y  * tileSize;
        tbl.getLayoutParams().width = LevelParser.MAP_SIZE_X * tileSize;

        // Give each table row exactly LevelParser.MAP_SIZE_X children
        for (int i = 0; i < LevelParser.MAP_SIZE_Y; i++){
            TableRow row = new TableRow(activity);
            tbl.addView(row);

            for (int j = 0; j < LevelParser.MAP_SIZE_X; j++){
                ImageView image = new ImageView(activity);
                row.addView(image, new TableRow.LayoutParams(0,
                        TableRow.LayoutParams.WRAP_CONTENT, 1f));

                image.setBackgroundResource(map.map[i][j].drawableId);
                image.setRotation(map.map[i][j].rotation);
                image.getLayoutParams().height = tileSize;
            }
        }

        // Look if there really are LevelParser.MAP_SIZE_Y rows

    }
}
