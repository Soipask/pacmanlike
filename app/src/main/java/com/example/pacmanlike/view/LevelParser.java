package com.example.pacmanlike.view;

import android.content.Context;

import com.example.pacmanlike.gamemap.Home;
import com.example.pacmanlike.activities.SelectionScreen;

import com.example.pacmanlike.gamemap.TileFactory;
import com.example.pacmanlike.main.AppConstants;

import com.example.pacmanlike.objects.Vector;
import com.example.pacmanlike.gamemap.GameMap;
import com.example.pacmanlike.gamemap.tiles.Tile;

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

    /**
     * Initialize parser with name of the file to be parsed and its context
     * @param fileName File to be parsed
     * @param context Current context
     */
    public void init(String fileName, Context context) throws Exception {
        String storageType = AppConstants.STORAGE_INTERNAL;
        if (SelectionScreen._internalMaps.contains(fileName)) {
            storageType = AppConstants.STORAGE_ASSETS;
        }

        _reader = null;
        if (storageType.equals(AppConstants.STORAGE_ASSETS)) {
            InputStream stream = context.getAssets().open(fileName);
            _reader = new Scanner(stream);
        } else if (storageType.equals(AppConstants.STORAGE_INTERNAL)) {
            File file = new File(context.getApplicationContext().getFilesDir(), fileName);
            _reader = new Scanner(file);
        } else{
            throw new Exception("Wrong storage type");
        }
    }

    /**
     * Initializes parser for parsing right from String variable.
     * Used when initializing parser for import validation.
     * @param level
     */
    public void initImportControl(String level){
        _reader = new Scanner(level);
    }

    /**
     * Parses the file using the initialized Scanner
     * @return Finished GameMap
     * @throws Exception
     */
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
                        map.setHome(new Home(x,y));
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

    /**
     * Parses head of the file.
     * @param map Instance of a GameMap
     * @param line Whole first line of the file
     * @throws Exception
     */
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

    /**
     * Parses initial position of the pacman.
     * @param map Instance of GameMap the parser's using
     */
    private void parsePacPosition(GameMap map){
        // PAC=x;y (where (x,y) are the starting coordinates of pacman)
        String pac = _dictionary.get(AppConstants.PAC_STARTING_KEYWORD);
        String[] pacCoords = pac.split(AppConstants.COORDS_DELIMITER);
        map.setStartingPacPosition(
                new Vector(Integer.parseInt(pacCoords[0]),Integer.parseInt(pacCoords[1]))
        );
    }

    /**
     * Parses positions of the power pellets
     * @param map Instance of GameMap the parser's using
     */
    private void parsePowerPelletPositions(GameMap map){
        // POWER=x1;y1.x2;y2.x3;y3.x4;y4
        // (where (xi,yi) are coordinates of power pellets in the level)
        String value = _dictionary.get(AppConstants.POWER_STARTING_KEYWORD);
        ArrayList<Vector> powerPelletPositions = new ArrayList<>();

        if(value != null) {
            String[] allPellets = value.split(AppConstants.MORE_DATA_DELIMITER);

            for (String pellet : allPellets) {
                String[] coords = pellet.split(AppConstants.COORDS_DELIMITER);
                powerPelletPositions.add(new Vector(Integer.parseInt(coords[0]), Integer.parseInt(coords[1])));
            }
        }
        map.setPowerPelletsPosition(powerPelletPositions);
    }

    /**
     * Checks if home's signature is the same as parsed home.
     * This ensures that this map is correct.
     * @param map Instance of GameMap the parser's using
     * @throws Exception When the home is in an incorrect format.
     */
    public void checkHomeCoords(GameMap map) throws Exception {
        boolean check = true;
        Vector homeCoords = map.getHome().getCoordinates();
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
