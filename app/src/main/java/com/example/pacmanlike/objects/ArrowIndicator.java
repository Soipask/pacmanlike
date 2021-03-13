package com.example.pacmanlike.objects;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.pacmanlike.R;
import com.example.pacmanlike.main.AppConstants;

/**
 * Class used to manipulate the indicators of the selected direction by the user.
 */
public class ArrowIndicator  extends DrawableObjects {

    /**
     *  Constructor for the ArrowIndicator.
     *  Sets propertiest at initial value.
     * @param context Given context.
     */
    public ArrowIndicator(Context context) {

        // frame
        _frameIndex = 0;
        _totalframe = 14;
        _frameCongruence = 2;

        _blockSize = AppConstants.getBlockSize();

        // initial direction
        // NONE direction dont display
        _direction = Direction.NONE;

        // arrow size
        _objectSize = _blockSize * 2;

        // load arrow sprites
        load(context);
    }
    /**
     * Loads arrow sprites from resources.
     * @param context Given context.
     */
    @Override
    protected void load(Context context) {

        // left sprites path
        int[] left = {
                R.drawable.left_arrow_frame1,
                R.drawable.left_arrow_frame2,
                R.drawable.left_arrow_frame3,
                R.drawable.left_arrow_frame4,
                R.drawable.left_arrow_frame5,
                R.drawable.left_arrow_frame6,
                R.drawable.left_arrow_frame7,

        };

        // right sprites path
        int[] right = {
                R.drawable.right_arrow_frame1,
                R.drawable.right_arrow_frame2,
                R.drawable.right_arrow_frame3,
                R.drawable.right_arrow_frame4,
                R.drawable.right_arrow_frame5,
                R.drawable.right_arrow_frame6,
                R.drawable.right_arrow_frame7,
        };

        // up sprites path
        int[] up = {
                R.drawable.up_arrow_frame1,
                R.drawable.up_arrow_frame2,
                R.drawable.up_arrow_frame3,
                R.drawable.up_arrow_frame4,
                R.drawable.up_arrow_frame5,
                R.drawable.up_arrow_frame6,
                R.drawable.up_arrow_frame7,
        };

        // down sprites path
        int[] down = {
                R.drawable.down_arrow_frame1,
                R.drawable.down_arrow_frame2,
                R.drawable.down_arrow_frame3,
                R.drawable.down_arrow_frame4,
                R.drawable.down_arrow_frame5,
                R.drawable.down_arrow_frame6,
                R.drawable.down_arrow_frame7,
        };

        // load sprites from resources
        _frames = new Bitmap[4][7];

        // UP
        _frames[0] = loadFrames(up, _objectSize, context);

        // DOWN
        _frames[1] = loadFrames(down, _objectSize, context);

        // LEFT
        _frames[2] = loadFrames(left, _objectSize, context);

        // RIGHT
        _frames[3] = loadFrames(right, _objectSize, context);
    }

    /**
     * Draws the arrow object on canvas.
     * @param canvas Given canvas.
     * @param paint Paint.
     */
    @Override
    public void draw(Canvas canvas, Paint paint) {

        // frame cycle
        if(_frameIndex == _totalframe) { _frameIndex = 0;}

        if(_frames != null && _direction != Direction.NONE) {

            // center bottom
            canvas.drawBitmap(_frames[_direction.getValue()][_frameIndex / _frameCongruence], AppConstants.SCREEN_WIDTH / 2 - _objectSize / 2, AppConstants.SCREEN_HEIGHT - _objectSize, paint);
        }

        _frameIndex++;
    }
}
