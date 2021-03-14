package com.example.pacmanlike.objects.food;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.pacmanlike.gamemap.GameMap;
import com.example.pacmanlike.gamemap.tiles.Tile;
import com.example.pacmanlike.main.AppConstants;
import com.example.pacmanlike.objects.Vector;

/**
 * Class representing the different types of objects (all foods on game map)
 * that are placed on the game map.
 * Different types of food otherwise affect the pacman.
 */
public class GameFoods {

    // number of pell in game
    int _numberOfFoods;

    //    // reward for eating the pells
    private static int _PELLSCORE = 20, _POWERPELLSCORE = 50;

    /**
     * Places the food on the map and set the basic parameters for interacting with it
     * @param map Current game map
     */
    public GameFoods(GameMap map) {

       // current number of foods
        _numberOfFoods = 0;

        // places the powerPell in their positions
        for (Vector powerPell :map.getPowerPelletsPosition()) {

            // powerPell posotion
            int x = powerPell.x;
            int y = powerPell.y;

            Tile tile = map.getTile(x,y);

            // the powerPell is not placed off the map and in the position of the home
            // These are the places where the pacman will not get and it would not be possible to end the game
            if(!tile._type.equals("Empty") && !tile._type.equals("Home") &&
                    !(x == map.getStartingPacPosition().x && y == map.getStartingPacPosition().y)){
                tile.setFood(Food.PowerPellet);
                _numberOfFoods++;
            }
        }


        // placing the pell in other possible positions in the game map
        for (int y = 0; y < AppConstants.MAP_SIZE_Y; y++) {
            for (int x = 0; x < AppConstants.MAP_SIZE_X; x++) {

                // for each tile
                Tile tile = map.getTile(x,y);

                // the pell is not placed off the map, in the position of the home and powerPell posotion
                // These are the places where the pacman will not get and it would not be possible to end the game
                if(!tile._type.equals("Empty") && !tile._type.equals("Home") && tile.getFood() != Food.PowerPellet &&
                        !(x == map.getStartingPacPosition().x && y == map.getStartingPacPosition().y)) {
                    tile.setFood(Food.Pellet);
                    _numberOfFoods++;
                }
            }
        }
    }


    /**
     * @return Number of foods in game
     */
    public int getNumberOfFoods() {return _numberOfFoods; }

    /**
     * Adjust the distribution and number of meals according to the interaction with the pacman
     * @return Score
     */
    public int update(){

        // current game map
        GameMap gameMap = AppConstants.getGameMap();

        // pacman position
        Vector position = AppConstants.getEngine().getPacman().getAbsolutePosition();

        // if it is in the middle of the tile, check food
        if(AppConstants.testCenterTile(position)) {

            // pacman position (tile)
            Tile tile = gameMap.getAbsoluteTile(position.x, position.y);


            if(tile.getFood() == Food.Pellet) {

                // if there is a pell on the tile
                tile.setFood(Food.None);
                _numberOfFoods--;
                return _PELLSCORE;

            } else if(tile.getFood() == Food.PowerPellet) {

                // if there is a powerPell on the tile, then set vulnerable ghosts
                AppConstants.getEngine().getGhostsEngine().startVulnereble();
                tile.setFood(Food.None);
                _numberOfFoods--;
                return _POWERPELLSCORE;
            }
        }
        return 0;
    }

    /**
     * Draws the object on canvas.
     * @param canvas Given canvas
     * @param _paintPells Paint for pells
     * @param _paintPowerPells Paint for powerPells
     */
    public void draw(Canvas canvas, Paint _paintPells, Paint _paintPowerPells) {

        // game map
        GameMap map = AppConstants.getGameMap();
        int _blockSize = AppConstants.getBlockSize();


        // for each tile of the map where the food is located draw it
        for (int y = 0; y < AppConstants.MAP_SIZE_Y; y++) {
            for (int x = 0; x < AppConstants.MAP_SIZE_X; x++) {

                // they plotted its display according to the type of food
                if(map.getTile(x,y).getFood() == Food.Pellet) {

                    // draw it in the middle of the tile
                    canvas.drawCircle(x * _blockSize + _blockSize / 2, y * _blockSize + _blockSize / 2, AppConstants.getBlockSize() * 0.1f, _paintPells);
                } else if(map.getTile(x,y).getFood() == Food.PowerPellet) {

                    // draw it in the middle of the tile
                    canvas.drawCircle(x * _blockSize + _blockSize / 2, y * _blockSize + _blockSize / 2, AppConstants.getBlockSize() * 0.2f, _paintPells);
                    canvas.drawCircle(x * _blockSize + _blockSize / 2, y * _blockSize + _blockSize / 2, AppConstants.getBlockSize() * 0.1f, _paintPowerPells);
                }
            }
        }
    }
}
