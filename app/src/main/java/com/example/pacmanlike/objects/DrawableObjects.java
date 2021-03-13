package com.example.pacmanlike.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * The mother absract class of all objects in the game that can be drawn.
 * It contains the basic properties of these drawable objects and methods
 * for working with them.
 */
public abstract class DrawableObjects {

    // object position in game map
    protected Vector _position;

    // current object's direction
    protected Direction _direction;

    // previous object's direction
    protected Direction _prevDirection;

    // size of game tile
    protected int _blockSize;

    // size of object (current)
    protected int _objectSize;

    // object drawable frames
    protected Bitmap[][] _frames;

    // current frame
    protected int _frameIndex;

    // macimal number of frames
    protected int _totalframe;

    // frame velocity
    protected int _frameCongruence;

    /**
     * Loads current game objects frames from resources.
     * @param context Given context
     */
    protected abstract void load(Context context);

    /**
     * Sets the location of the game object on the screen.
     * @param position Absolute pisition in game map.
     */
    public void setAbsolutePosition(Vector position) { _position = position; }

    /**
     * Returns absolute position of the game object on the game screen.
     * @return Absolute position.
     */
    public Vector getAbsolutePosition() { return  _position; }

    /**
     * Sets the location of the game object on the screen.
     * @param position Relative pisition in game map.
     */
    public void setRelativePosition(Vector position) {
        // map tile position to center absolute position in tile on screen
        _position = new Vector(position.x*_blockSize + _blockSize/2, position.y*_blockSize + _blockSize/ 2);
    }

    /**
     * Returns relative position of the game object on the game screen.
     * @return Relative position.
     */
    public Vector getRelativePosition(){
        // map tile position
        return  new Vector(_position.x / _blockSize, _position.y / _blockSize);
    }

    /**
     * Sets the new direction of the game object.
     * @param direction New objects direction.
     */
    public void setDirection(Direction direction) {
        _prevDirection = _direction;
        _direction = direction;
    }

    /**
     * Gets current object direction.
     * @return Current direction.
     */
    public Direction getDirection() { return _direction; }

    /**
     * Loads one frame from resources and returns the bitmap of frame.
     * @param path Frame path.
     * @param size Size of frame.
     * @param context Given Context.
     * @return Frame bitmap.
     */
    protected Bitmap loadFrame(int path, int size, Context context){
        return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), path), size, size, false);
    }

    /**
     * Loads the package frames from resources and returns them in the list.
     * @param paths Frames paths.
     * @param size Size of frame.
     * @param context Given Context.
     * @return List of bitmap frames.
     */
    protected Bitmap[] loadFrames(int[] paths, int size, Context context) {

        Bitmap[] tmpSprites = new Bitmap[paths.length];

        int i = 0;
        for (int path : paths) {
            tmpSprites[i] = loadFrame(path, size, context);
            i++;
        }

        return tmpSprites;
    }

    /**
     * Draws the object on canvas.
     * @param canvas Given canvas.
     * @param paint Paint.
     */
    public void draw(Canvas canvas, Paint paint) {

        // frame cycle
        if(_frameIndex == _totalframe) {
            _frameIndex = 0;
        }

        // if the direction of the object is none, then draw it in the previous direction
        if(_direction == Direction.NONE) {
            canvas.drawBitmap(_frames[_prevDirection.getValue()][_frameIndex / _frameCongruence], _position.x - _objectSize/2, _position.y - _objectSize / 2, paint);
        } else {
            canvas.drawBitmap(_frames[_direction.getValue()][_frameIndex / _frameCongruence], _position.x - _objectSize / 2, _position.y - _objectSize / 2, paint);
        }

        // next frame
        _frameIndex++;
    }
}
