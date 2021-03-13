package com.example.pacmanlike.gamemap;

import com.example.pacmanlike.objects.Vector;

public class Home {
    // Delete
    private static Home _instance;

    public static int SIZE_X = 3;
    public static int SIZE_Y = 1;

    // How does a home look like? This is signature in terms of making how does the tiles look like.
    // We can check them in parse
    public static String[] SIGNATURE = {"A1","A2","A3"};

    private Vector _coordinates;

    public Vector getCoordinates() { return _coordinates;}

    // Delete
    public static Home getInstance() { return _instance;}

    public Home(int x, int y) {
        _coordinates = new Vector(x,y);

        _instance = this;

    }
}
