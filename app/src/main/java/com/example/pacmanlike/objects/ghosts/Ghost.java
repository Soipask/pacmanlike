package com.example.pacmanlike.objects.ghosts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.pacmanlike.main.AppConstants;
import com.example.pacmanlike.objects.Direction;
import com.example.pacmanlike.objects.DrawableObjects;
import com.example.pacmanlike.objects.Vector;

public class Ghost extends DrawableObjects {

    private int _id;
    private boolean _vulnerable;


    public  Ghost(Context context, Vector startingPosition, int ghostID) {

        _id = ghostID;
        _vulnerable=  false;

        _frameIndex = 0;
        _totalframe = 56;
        _frameCongruence = 8;


        _blockSize = AppConstants.getBlockSize();

        _direction = Direction.NONE;
        _prevDirection = Direction.UP;

        setRelativePosition(startingPosition);

        double tmp =  0.6 * AppConstants.getBlockSize();
        _objectSize = (int) tmp;

        load(context);
    }


    public void  setVulnerable(Boolean value) {_vulnerable = value; }

    public Boolean isVulnerable() { return _vulnerable; }

    @Override
    protected void load(Context context) {
        int[][] _spriteId = GhostID.getSpriteID(_id);

        _frames = new Bitmap[2][7];
        _frames[0] = loadFrames(_spriteId[0], _objectSize, context);
        _frames[1] = loadFrames(_spriteId[1], _objectSize, context);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        if(_frameIndex == _totalframe)
            _frameIndex = 0;

        if(_vulnerable) {
            canvas.drawBitmap(_frames[1][_frameIndex / _frameCongruence], _position.x - _objectSize / 2, _position.y - _objectSize / 2, paint);
        }else {
            canvas.drawBitmap(_frames[0][_frameIndex / _frameCongruence], _position.x - _objectSize / 2, _position.y - _objectSize / 2, paint);
        }
        _frameIndex++;
    }

}
