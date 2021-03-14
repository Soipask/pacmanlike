package com.example.pacmanlike.gamemap;

import com.example.pacmanlike.gamemap.tiles.CrossroadTile;
import com.example.pacmanlike.gamemap.tiles.DoorTile;
import com.example.pacmanlike.gamemap.tiles.EmptyTile;
import com.example.pacmanlike.gamemap.tiles.HalfcrossroadTile;
import com.example.pacmanlike.gamemap.tiles.HomeTile;
import com.example.pacmanlike.gamemap.tiles.LeftTeleportTile;
import com.example.pacmanlike.gamemap.tiles.RightTeleportTile;
import com.example.pacmanlike.gamemap.tiles.StraightTile;
import com.example.pacmanlike.gamemap.tiles.Tile;
import com.example.pacmanlike.gamemap.tiles.TurnTile;

public class TileFactory {

    /**
     * Creates a tile based on the letter and argument.
     * @param tileName Name of the tile in parser
     * @param argument Argument of the tile in parser (usually rotation)
     * @return The right constructed Tile
     * @throws Exception When unused char is in tileName
     */
    public static Tile createTile(char tileName, String argument) throws Exception {
        Tile tile;

        switch (tileName){
            case 'S' : tile = new StraightTile(argument); break;
            case 'T' : tile = new TurnTile(argument); break;
            case 'H' : tile = new HalfcrossroadTile(argument); break;
            case 'C' : tile = new CrossroadTile(); break;
            case 'R' : tile = new RightTeleportTile(); break;
            case 'L' : tile = new LeftTeleportTile(); break;
            case 'X' : tile = new EmptyTile(); break;
            case 'A' : tile = new HomeTile(argument); break;
            case 'D' : tile = new DoorTile(); break;
            default: throw new Exception("Wrong tile format.");
        }
        return tile;
    }

    public static Tile createTile(String tileName) throws Exception{
        return createTile(tileName.charAt(0),tileName.substring(1));
    }
}
