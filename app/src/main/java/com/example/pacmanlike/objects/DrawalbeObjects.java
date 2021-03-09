package com.example.pacmanlike.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class DrawalbeObjects {

    protected int _frameIndex;
    protected int _totalframe;

    protected int _blockSize;
    protected int _objectSize;


    protected Direction _direction;
    protected Direction _prevDirection;
    protected Bitmap[][] _sprites;

    protected Vector _position;

    public void setDirection(Direction direction) {
        _prevDirection = _direction;
        _direction = direction;
    }

    public Direction getDirection() { return _direction; }

    protected Bitmap loadSprite(int path, int size, Context context){
        return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), path), size, size, false);
    }

    protected Bitmap[] loadSprites(int[] paths, int size, Context context) {

        Bitmap[] tmpSprites = new Bitmap[paths.length];
        int i = 0;

        for (int path : paths) {
            tmpSprites[i] = loadSprite(path, size, context);
            i++;
        }

        return tmpSprites;
    }

    protected abstract void load(Context context);

    public void draw(Canvas canvas, Paint paint) {

        if(_frameIndex == _totalframe) {
            _frameIndex = 0;
        }

        if(_direction == Direction.NONE) {
            canvas.drawBitmap(_sprites[_prevDirection.getValue()][_frameIndex / 5], _position.x - _objectSize/2, _position.y - _objectSize / 2, paint);
        } else {
            canvas.drawBitmap(_sprites[_direction.getValue()][_frameIndex / 5], _position.x - _objectSize / 2, _position.y - _objectSize / 2, paint);
        }

        _frameIndex++;
    }
}
