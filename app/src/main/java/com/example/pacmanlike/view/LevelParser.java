package com.example.pacmanlike.view;

import android.content.Context;

import com.example.pacmanlike.gamemap.Home;
import com.example.pacmanlike.activities.SelectionScreen;

import com.example.pacmanlike.gamemap.TileFactory;
import com.example.pacmanlike.gamemap.tiles.DoorTile;
import com.example.pacmanlike.main.AppConstants;

import com.example.pacmanlike.objects.Vector;
import com.example.pacmanlike.gamemap.GameMap;
import com.example.pacmanlike.gamemap.tiles.CrossroadTile;
import com.example.pacmanlike.gamemap.tiles.EmptyTile;
import com.example.pacmanlike.gamemap.tiles.HalfcrossroadTile;
import com.example.pacmanlike.gamemap.tiles.HomeTile;
import com.example.pacmanlike.gamemap.tiles.LeftTeleportTile;
import com.example.pacmanlike.gamemap.tiles.RightTeleportTile;
import com.example.pacmanlike.gamemap.tiles.StraightTile;
import com.example.pacmanlike.gamemap.tiles.Tile;
import com.example.pacmanlike.gamemap.tiles.TurnTile;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class LevelParser {
    public static final int MAP_SIZE_X = 7;
    public static final int MAP_SIZE_Y = 9;
    private Scanner _reader;
    private HashMap<String, String> _dictionary = new HashMap<>();

    public void init(String fileName, Context context) throws Exception {
        String storageType = SelectionScreen.INTERNAL;
        if (SelectionScreen.internalMaps.contains(fileName)) {
            storageType = SelectionScreen.ASSETS;
        }

        _reader = null;
        if (storageType.equals(SelectionScreen.ASSETS)) {
            InputStream stream = context.getAssets().open(fileName);
            _reader = new Scanner(stream);
        } else if (storageType.equals(SelectionScreen.INTERNAL)) {
            File file = new File(context.getApplicationContext().getFilesDir(), fileName);
            _reader = new Scanner(file);
        } else{
            throw new Exception("Wrong storage type");
        }
    }

    public GameMap parse() throws Exception {
        GameMap map = new GameMap();

        // At first, parse header (where's home, where's starting pac position...)
        if (_reader.hasNextLine()){
            parseHead(map, _reader.nextLine());
        }

        // Create a map with set size
        map.setMap(new Tile[MAP_SIZE_Y][MAP_SIZE_X]);

        // Parse whole map line by line, element by element
        int y = 0;
        while (_reader.hasNextLine()) {
            String data = _reader.nextLine();

            // Loop parse lines
            String[] line = data.split(",");

            for(int x = 0; x < line.length; x++){
                map.getMap()[y][x] = map.getMap()[y][x] = TileFactory.createTile(line[x]);

                // Some final things...
                switch (line[x]){
                    case "A2":
                        new Home(x,y);
                        break;
                    case "L" :
                        map.setLeftTeleportPosition(new Vector(x,y));
                        break;
                    case "R" :
                        map.setRightTeleportPosition(new Vector(x,y));
                        break;
                }

            }

            y++;
        }

        // If there wasn't enough lines, there's a problem
        if (y != MAP_SIZE_Y){
            throw new Exception("Wrong number of rows in the map.");
        }
        _reader.close();

        // At last, check if home can be at home coordinates
        // (check if home signature matches what's in the map at these coordinates)
        checkHomeCoords(map);

        return map;
    }

    public void parseHead(GameMap map, String line) throws Exception {
        String[] head = line.split(AppConstants.CSV_DELIMITER);

        // Putting arguments to dictionary
        for (String arg : head) {
            String[] pair = arg.split(AppConstants.KEY_VALUE_DELIMITER);
            _dictionary.put(pair[0], pair[1]);
        }

        parsePacPosition(map);
        parsePowerPelletPositions(map);
    }

    private void parsePacPosition(GameMap map){
        // PAC=x;y (where (x,y) are the starting coordinates of pacman)
        String pac = _dictionary.get(AppConstants.PAC_STARTING_KEYWORD);
        String[] pacCoords = pac.split(AppConstants.COORDS_DELIMITER);
        map.setStartingPacPosition(
                new Vector(Integer.parseInt(pacCoords[0]),Integer.parseInt(pacCoords[1]))
        );
    }

    private void parsePowerPelletPositions(GameMap map){
        // POWER=x1;y1.x2;y2.x3;y3.x4;y4
        // (where (xi,yi) are coordinates of power pellets in the level)
        String value = _dictionary.get(AppConstants.POWER_STARTING_KEYWORD);
        String[] allPellets = value.split(AppConstants.MORE_DATA_DELIMITER);
        ArrayList<Vector> powerPelletPositions = new ArrayList<>();

        for (String pellet : allPellets){
            String[] coords = pellet.split(AppConstants.COORDS_DELIMITER);
            powerPelletPositions.add( new Vector(Integer.parseInt(coords[0]), Integer.parseInt(coords[1])) );
        }

        map.setPowerPelletsPosition(powerPelletPositions);
    }

    public Tile parseTile(String tileName) throws Exception {
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
            case 'D' : tile = new DoorTile(); break;
            default: throw new Exception("Wrong tile format.");
        }
        return tile;
    }

    public void checkHomeCoords(GameMap map) throws Exception {
        boolean check = true;
        Vector homeCoords = Home.getInstance().getCoordinates();
        int y = homeCoords.y;
        for (int i = 0; i < Home.SIZE_X; i++){
            int x = homeCoords.x + i - 1;
            if (!map.getTile(x, y).toString().equals(Home.SIGNATURE[i])){
                check = false;
            }
        }

        if (!check) {
            throw new Exception("Bad home format");
        }
    }
}
