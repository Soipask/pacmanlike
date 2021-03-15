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
import com.example.pacmanlike.main.AppConstants;

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
            case AppConstants.CHAR_STRAIGHT : tile = new StraightTile(argument); break;
            case AppConstants.CHAR_TURN : tile = new TurnTile(argument); break;
            case AppConstants.CHAR_HALFXROAD : tile = new HalfcrossroadTile(argument); break;
            case AppConstants.CHAR_CROSSROAD : tile = new CrossroadTile(); break;
            case AppConstants.CHAR_RIGHT_TELEPORT : tile = new RightTeleportTile(); break;
            case AppConstants.CHAR_LEFT_TELEPORT : tile = new LeftTeleportTile(); break;
            case AppConstants.CHAR_EMPTY : tile = new EmptyTile(); break;
            case AppConstants.CHAR_HOME : tile = new HomeTile(argument); break;
            case AppConstants.CHAR_DOOR : tile = new DoorTile(); break;
            default: throw new Exception("Wrong tile format.");
        }
        return tile;
    }

    public static Tile createTile(String tileName) throws Exception{
        return createTile(tileName.charAt(0),tileName.substring(1));
    }
}
