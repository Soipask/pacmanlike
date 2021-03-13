package com.example.pacmanlike.objects.ghosts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.pacmanlike.main.AppConstants;
import com.example.pacmanlike.objects.Direction;
import com.example.pacmanlike.objects.DrawableObjects;
import com.example.pacmanlike.objects.Vector;

/**
 * This class represents the character of the ghost.
 * It stores basic information about the location and
 * characteristics of this character.
 */
public class Ghost extends DrawableObjects {

    //ghost ID
    private int _id;

    // Ghost vulnerability
    private boolean _vulnerable;

    /**
     * Creates a ghost at the specified position
     * with initial properties in the specified context.
     * @param context GameView.
     * @param startingPosition Ghost position.
     * @param ghostID Ghost ID.
     */
    public  Ghost(Context context, Vector startingPosition, int ghostID) {

        // ghost properties
        _id = ghostID;
        _vulnerable=  false;

        // frames
        _frameIndex = 0;
        _totalframe = 56;
        _frameCongruence = 8;

        _blockSize = AppConstants.getBlockSize();

        // ghost direction
        _direction = Direction.NONE;
        _prevDirection = Direction.UP;

        // starting position
        setRelativePosition(startingPosition);

        double tmp =  0.6 * AppConstants.getBlockSize();
        _objectSize = (int) tmp;

        // loads frames
        load(context);
    }

    /**
     * Sets ghost vulnerability.
     * @param value Vulnerability.
     */
    public void  setVulnerable(Boolean value) {_vulnerable = value; }

    /**
     * Return true if the ghost is vulnerable.
     * @return Vulnerability.
     */
    public Boolean isVulnerable() { return _vulnerable; }

    /**
     *  Loads ghost sprites from resources.
     * @param context Given context
     */
    @Override
    protected void load(Context context) {
        int[][] _spriteId = GhostID.getSpriteID(_id);

        _frames = new Bitmap[2][7];
        _frames[0] = loadFrames(_spriteId[0], _objectSize, context);
        _frames[1] = loadFrames(_spriteId[1], _objectSize, context);
    }

    /**
     * Draws the ghost object on canvas.
     * @param canvas Given canvas.
     * @param paint Paint.
     */
    @Override
    public void draw(Canvas canvas, Paint paint) {

        // frame cycle
        if(_frameIndex == _totalframe)
            _frameIndex = 0;


        // TODO: Blinking
        if(_vulnerable) {
            canvas.drawBitmap(_frames[1][_frameIndex / _frameCongruence], _position.x - _objectSize / 2, _position.y - _objectSize / 2, paint);
        }else {
            canvas.drawBitmap(_frames[0][_frameIndex / _frameCongruence], _position.x - _objectSize / 2, _position.y - _objectSize / 2, paint);
        }
        _frameIndex++;
    }

}
