package com.example.pacmanlike.view;

import android.widget.ImageButton;

import com.example.pacmanlike.objects.Vector;

/**
 * Data class for storing information about level making tiles
 */
public class MapSquare{
    public char letter = 'X';
    public float argument = 0;
    public Vector position;
    public boolean immovable = false;
    public ImageButton button;

    @Override
    public String toString() {
        return letter + String.valueOf((int) argument);
    }
}
