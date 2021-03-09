package com.example.pacmanlike.objects;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.pacmanlike.R;
import com.example.pacmanlike.main.AppConstants;

public class ArrowIndicator  extends DrawalbeObjects {

    private int _arrowSize;

    public ArrowIndicator(Context context) {
        _frameIndex = 0;
        _totalframe = 14;
        _blockSize = AppConstants.getBlockSize();
        _direction = Direction.NONE;

        _arrowSize = _blockSize * 2;


        load(context);
    }

    @Override
    protected void load(Context context) {

        int[] left = {
                R.drawable.left_arrow_frame1,
                R.drawable.left_arrow_frame2,
                R.drawable.left_arrow_frame3,
                R.drawable.left_arrow_frame4,
                R.drawable.left_arrow_frame5,
                R.drawable.left_arrow_frame6,
                R.drawable.left_arrow_frame7,

        };

        int[] right = {
                R.drawable.right_arrow_frame1,
                R.drawable.right_arrow_frame2,
                R.drawable.right_arrow_frame3,
                R.drawable.right_arrow_frame4,
                R.drawable.right_arrow_frame5,
                R.drawable.right_arrow_frame6,
                R.drawable.right_arrow_frame7,
        };

        int[] up = {
                R.drawable.up_arrow_frame1,
                R.drawable.up_arrow_frame2,
                R.drawable.up_arrow_frame3,
                R.drawable.up_arrow_frame4,
                R.drawable.up_arrow_frame5,
                R.drawable.up_arrow_frame6,
                R.drawable.up_arrow_frame7,
        };

        int[] down = {
                R.drawable.down_arrow_frame1,
                R.drawable.down_arrow_frame2,
                R.drawable.down_arrow_frame3,
                R.drawable.down_arrow_frame4,
                R.drawable.down_arrow_frame5,
                R.drawable.down_arrow_frame6,
                R.drawable.down_arrow_frame7,
        };

        _sprites = new Bitmap[4][7];
        _sprites[0] = loadSprites(up, _arrowSize, context);
        _sprites[1] = loadSprites(down, _arrowSize, context);
        _sprites[2] = loadSprites(left, _arrowSize, context);
        _sprites[3] = loadSprites(right, _arrowSize, context);
    }


    @Override
    public void draw(Canvas canvas, Paint paint) {
        // ak je idex rovny tak zacinamu znovu 0. indexom
        if(_frameIndex == _totalframe) { _frameIndex = 0;}

        if(_sprites != null && _direction != Direction.NONE) {
            canvas.drawBitmap(_sprites[_direction.getValue()][_frameIndex / 2], AppConstants.SCREEN_WIDTH / 2 - _arrowSize / 2, AppConstants.SCREEN_HEIGHT - _arrowSize, paint);
        }

        _frameIndex++;
    }
}
