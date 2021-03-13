package com.example.pacmanlike.objects;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.pacmanlike.R;
import com.example.pacmanlike.main.AppConstants;

/**
 * This class represents the character of the pacman.
 * It stores basic information about the location and
 * characteristics of this character.
 */
public class PacMan extends DrawableObjects {

    /**
     * Creates a pecman at the specified position
     * with initial properties in the specified context.
     * @param context GameView context.
     * @param startingPosition Initial position.
     */
    public  PacMan(Context context, Vector startingPosition) {

        // current frame
        _frameIndex = 0;

        // maximal index of frames
        _totalframe = 20;

        // frame velocity
        _frameCongruence = 5;

        // size of tile in game map
        _blockSize = AppConstants.getBlockSize();

        // current pacman direction
        _direction = Direction.NONE;

        // previous pacman direction
        _prevDirection = Direction.UP;

        // sets relative position tu absolute position
        setRelativePosition(startingPosition);

        // size of pacman
        double tmp =  0.6 * AppConstants.getBlockSize();
        _objectSize = (int) tmp;

        // load pacman frames
        load(context);
    }

    /**
     * Loads pacman frames from resources.
     * @param context Given context.
     */
    @Override
    protected void load(Context context) {

        // right frames path
        int[] right = {
                R.drawable.pacman_right1,
                R.drawable.pacman_right2,
                R.drawable.pacman_right3,
                R.drawable.pacman_right,
        };

        // left frames path
        int[] left = {
                R.drawable.pacman_left1,
                R.drawable.pacman_left2,
                R.drawable.pacman_left3,
                R.drawable.pacman_left,
        };

        // up frames path
        int[] up = {
                R.drawable.pacman_up1,
                R.drawable.pacman_up2,
                R.drawable.pacman_up3,
                R.drawable.pacman_up,
        };

        // down frames path
        int[] down = {
                R.drawable.pacman_down1,
                R.drawable.pacman_down2,
                R.drawable.pacman_down3,
                R.drawable.pacman_down,
        };

        // load frames from resources
        _frames = new Bitmap[4][4];

        // UP
        _frames[0] = loadFrames(up, _objectSize,context);

        // DOWN
        _frames[1] = loadFrames(down,_objectSize,context);

        //LEFT
        _frames[2] = loadFrames(left, _objectSize,context);

        // RIGHT
        _frames[3] = loadFrames(right, _objectSize, context);
    }
}
