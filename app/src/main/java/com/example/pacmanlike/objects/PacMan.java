package com.example.pacmanlike.objects;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.pacmanlike.R;
import com.example.pacmanlike.main.AppConstants;

public class PacMan extends  DrawalbeObjects{


    public  PacMan(Context context, Vector startingPosition) {

        _frameIndex = 0;
        _totalframe = 20;
        _blockSize = AppConstants.getBlockSize();

        _direction = Direction.NONE;
        _prevDirection = Direction.UP;

        _position = new Vector(startingPosition.x*_blockSize + _blockSize/2, startingPosition.y*_blockSize + _blockSize/2);

        double tmp =  0.6 * AppConstants.getBlockSize();
        _objectSize = (int) tmp;

        load(context);
    }


    @Override
    protected void load(Context context) {
        int[] right = {
                R.drawable.pacman_right1,
                R.drawable.pacman_right2,
                R.drawable.pacman_right3,
                R.drawable.pacman_right,
        };

        int[] left = {
                R.drawable.pacman_left1,
                R.drawable.pacman_left2,
                R.drawable.pacman_left3,
                R.drawable.pacman_left,
        };

        int[] up = {
                R.drawable.pacman_up1,
                R.drawable.pacman_up2,
                R.drawable.pacman_up3,
                R.drawable.pacman_up,
        };

        int[] down = {
                R.drawable.pacman_down1,
                R.drawable.pacman_down2,
                R.drawable.pacman_down3,
                R.drawable.pacman_down,
        };


        _sprites = new Bitmap[4][4];
        _sprites[0] = loadSprites(up, _objectSize,context);
        _sprites[1] = loadSprites(down,_objectSize,context);
        _sprites[2] = loadSprites(left, _objectSize,context);
        _sprites[3] = loadSprites(right, _objectSize, context);
    }
}
