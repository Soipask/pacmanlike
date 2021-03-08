package com.example.pacmanlike;

import android.app.Activity;
import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class LevelParser {
    public static final int MAP_SIZE_X = 7;
    public static final int MAP_SIZE_Y = 9;
    File mapFile;
    Scanner reader;

    public void Init(String fileName, Context context) throws Exception {
        String storageType = SelectionScreen.INTERNAL;
        if (SelectionScreen.internalMaps.contains(fileName)) {
            storageType = SelectionScreen.ASSETS;
        }

        reader = null;
        if (storageType.equals(SelectionScreen.ASSETS)) {
            InputStream stream = context.getAssets().open(fileName);
            reader = new Scanner(stream);
        } else if (storageType.equals(SelectionScreen.INTERNAL)) {
            File file = new File(context.getApplicationContext().getFilesDir(), fileName);
            reader = new Scanner(file);
        } else{
            throw new Exception("Wrong storage type");
        }
    }

    public GameMap Parse() throws Exception {
        GameMap map = new GameMap();

        // At first, parse header (where's home, where's starting pac position...)
        if (reader.hasNextLine()){
            ParseHead(map, reader.nextLine());
        }

        // Create a map with set size
        map.map = new Tile[MAP_SIZE_Y][MAP_SIZE_X];

        // Parse whole map line by line, element by element
        int y = 0;
        while (reader.hasNextLine()) {
            String data = reader.nextLine();

            // Loop parse lines
            String[] line = data.split(",");
            for(int i = 0; i < line.length; i++){
                map.map[y][i] = ParseTile(line[i]);
            }

            y++;
        }

        // If there wasn't enough lines, there's a problem
        if (y != MAP_SIZE_Y){
            throw new Exception("Wrong number of rows in the map.");
        }
        reader.close();

        // At last, check if home can be at home coordinates
        // (check if home signature matches what's in the map at these coordinates)
        CheckHomeCoords(map);

        return map;
    }

    public void ParseHead(GameMap map, String line) throws Exception {
        String[] head = line.split(",");
        // First, there has to be HOME=x,y (where (x,y) are first coordinates of home)
        if (!head[0].startsWith("HOME")){
            throw new Exception("Wrong format of the map file!");
        }
        String homeString = head[0].split("=")[1];

        String[] homeCoords = homeString.split(";");
        new Home(Integer.parseInt(homeCoords[0]), Integer.parseInt(homeCoords[1]));

        // Then PAC=x,y (where (x,y) are the starting coordinates of pacman)
        if (!head[1].startsWith("PAC")){
            throw new Exception("Wrong format of the map file!");
        }
        String pac = head[1].split("=")[1];

        String[] pacCoords = pac.split(";");
        map.startingPacPosition = new Vector(Integer.parseInt(pacCoords[0]),Integer.parseInt(pacCoords[1]));
    }

    public Tile ParseTile(String tileName) throws Exception {
        // Tile name is always a letter, there can be a rotation number after (like "S90" or "C")
        Tile tile;

        switch (tileName.charAt(0)){
            case 'S' : tile = new StraightTile(tileName.substring(1)); break;
            case 'T' : tile = new TurnTile(tileName.substring(1)); break;
            case 'H' : tile = new HalfcrossroadTile(tileName.substring(1)); break;
            case 'C' : tile = new CrossroadTile(); break;
            case 'R' : tile = new RightTeleportTile(); break;
            case 'L' : tile = new LeftTeleportTile(); break;
            case 'X' : tile = new EmptyTile(); break;
            case 'A' : tile = new HomeTile(tileName.substring(1)); break;
            default: throw new Exception("Wrong tile format.");
        }
        return tile;
    }

    public void CheckHomeCoords(GameMap map) throws Exception {
        boolean check = true;
        for (int i = 0; i < Home.SIZE_Y; i++){
            for (int j = 0; j < Home.SIZE_X; j++){
                int y = Home.instance.firstCoords.y + i;
                int x = Home.instance.firstCoords.x + j;
                if (!map.map[y][x].toString().equals(Home.SIGNATURE[i][j])){
                    check = false;
                }
            }
        }

        if (!check) {
            throw new Exception("Bad home format");
        }
    }
}
