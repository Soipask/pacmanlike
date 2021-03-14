package com.example.pacmanlike.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pacmanlike.gamemap.GameMap;
import com.example.pacmanlike.R;
import com.example.pacmanlike.main.AppConstants;

public class LevelView {
    AppCompatActivity activity;
    GameMap map;
    private int _tileSize = 100;

    public LevelView(AppCompatActivity activity, GameMap map) {
        this.activity = activity;
        this.map = map;
    }


    public void setTileSize(int size) { _tileSize = size; }

    public LevelView(GameMap map) {
        this.map = map;
    }

    public void createLevel() {

        // Get table
        // RadioGroup rgp = (RadioGroup) findViewById(R.id.radioGroup);
        TableLayout tbl = (TableLayout) activity.findViewById(R.id.table);
        tbl.getLayoutParams().height = LevelParser.MAP_SIZE_Y * _tileSize;
        tbl.getLayoutParams().width = LevelParser.MAP_SIZE_X * _tileSize;

        // Give each table row exactly LevelParser.MAP_SIZE_X children
        for (int i = 0; i < LevelParser.MAP_SIZE_Y; i++) {
            TableRow row = new TableRow(activity);
            tbl.addView(row);

            for (int j = 0; j < LevelParser.MAP_SIZE_X; j++) {
                ImageView image = new ImageView(activity);
                row.addView(image, new TableRow.LayoutParams(0,
                        TableRow.LayoutParams.WRAP_CONTENT, 1f));

                image.setBackgroundResource(map.getMap()[i][j]._drawableId);
                image.setRotation(map.getMap()[i][j]._rotation);
                image.getLayoutParams().height = _tileSize;
            }
        }

        // Look if there really are LevelParser.MAP_SIZE_Y rows
    }

    public Bitmap createLevelBitmap(Context context) {


        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap bmp = Bitmap.createBitmap(AppConstants.getBlockSize() * AppConstants.MAP_SIZE_X, AppConstants.getBlockSize() * AppConstants.MAP_SIZE_Y, conf); // this creates a MUTABLE bitmap

        // Give each table row exactly LevelParser.MAP_SIZE_X children
        for (int i = 0; i < LevelParser.MAP_SIZE_Y; i++) {

            for (int j = 0; j < LevelParser.MAP_SIZE_X; j++) {

                Bitmap tmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), map.getMap()[i][j]._drawableId), AppConstants.getBlockSize(), AppConstants.getBlockSize(), false);

                Matrix matrix = new Matrix();
                matrix.postRotate(map.getMap()[i][j]._rotation);

                Bitmap rotatedBitmap = Bitmap.createBitmap(tmp, 0, 0, tmp.getWidth(), tmp.getHeight(), matrix, true);


                for (int x = 0; x < AppConstants.getBlockSize(); x++) {
                    for (int y = 0; y < AppConstants.getBlockSize(); y++) {

                        bmp.setPixel(AppConstants.getBlockSize() * j + x, AppConstants.getBlockSize() * i + y, rotatedBitmap.getPixel(x, y));
                    }
                }
            }
        }
        return  bmp;
    }
}




