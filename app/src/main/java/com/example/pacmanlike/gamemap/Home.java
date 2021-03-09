package com.example.pacmanlike.gamemap;

import com.example.pacmanlike.objects.Vector;

public class Home {
    // Singleton
    public static Home instance;

    public static int SIZE_X = 3;
    public static int SIZE_Y = 2;

    // How does a home look like? This is signature in terms of making how does the tiles look like.
    // We can check them in parse
    public static String[][] SIGNATURE = {{"S0","S0","S0"},{"A1","A2","A3"}};

    private Vector _firstCoords;
    Vector lastCoords() {return new Vector(_firstCoords.x + SIZE_X, _firstCoords.y + SIZE_Y);}


    public Vector getFirstCoords() { return _firstCoords;}

    public Home(int x, int y) throws Exception {
        _firstCoords = new Vector(x,y);

        if (instance == null) {
            instance = this;
        }
    }
}
