package com.example.pacmanlike;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class LevelParser {
    int MAP_SIZE_X = 7;
    int MAP_SIZE_Y = 9;
    File mapFile;

    public LevelParser(){
    }

    public GameMap Parse(InputStream stream) throws Exception {
        GameMap map = new GameMap();

        /* try {*/
        Scanner reader = new Scanner(stream);
        if (reader.hasNextLine()){
            ParseHead(map, reader.nextLine());
        }

        map.map = new Tile[MAP_SIZE_Y][MAP_SIZE_X];

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
        if (y != MAP_SIZE_Y){
            throw new Exception("Wrong number of rows in the map.");
        }
        reader.close();
        /**
         * } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
         **/
        CheckHomeCoords(map);

        return map;
    }

    public void ParseHead(GameMap map, String line) throws Exception {
        String[] head = line.split(",");
        if (!head[0].startsWith("HOME")){
            throw new Exception("Wrong format of the map file!");
        }
        String homeString = head[0].split("=")[1];

        if (!head[1].startsWith("PAC")){
            throw new Exception("Wrong format of the map file!");
        }
        String pac = head[1].split("=")[1];

        String[] homeCoords = homeString.split(";");
        new Home(Integer.parseInt(homeCoords[0]), Integer.parseInt(homeCoords[1]));

        String[] pacCoords = pac.split(";");
        map.startingPacPosition = new Vector(Integer.parseInt(pacCoords[0]),Integer.parseInt(pacCoords[1]));
    }

    public Tile ParseTile(String tileName) throws Exception {
        // Tile name is always a letter, there can be a rotation number after (like "S90")
        Tile tile;

        switch (tileName.charAt(0)){
            case 'S' : tile = new StraightTile(tileName.substring(1)); break;
            case 'T' : tile = new TurnTile(tileName.substring(1)); break;
            case 'H' : tile = new HalfcrossroadTile(tileName.substring(1)); break;
            case 'C' : tile = new CrossroadTile(); break;
            case 'R' : tile = new RightTeleportTile(); break;
            case 'L' : tile = new LeftTeleportTile(); break;
            case 'X' : tile = new EmptyTile(); break;
            default: throw new Exception("Wrong tile format.");
        }
        return tile;
    }

    public void CheckHomeCoords(GameMap map){
        boolean check = true;
        for (int i = 0; i < Home.SIZE_Y; i++){
            for (int j = 0; j < Home.SIZE_X; j++){
                int y = Home.instance.firstCoords.y + i;
                int x = Home.instance.firstCoords.x + j;
                if (map.map[y][x].toString() != Home.SIGNATURE[i][j]){
                    check = false;
                }
            }
        }
    }
}
