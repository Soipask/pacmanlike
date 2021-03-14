package com.example.pacmanlike.gamelogic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.pacmanlike.gamemap.GameMap;
import com.example.pacmanlike.gamemap.tiles.Tile;
import com.example.pacmanlike.main.AppConstants;
import com.example.pacmanlike.objects.Direction;
import com.example.pacmanlike.objects.DrawableObjects;
import com.example.pacmanlike.objects.PacMan;
import com.example.pacmanlike.objects.food.GameFoods;
import com.example.pacmanlike.objects.Vector;
import com.example.pacmanlike.objects.ghosts.Ghost;
import com.example.pacmanlike.objects.ghosts.engine.GhostsEngine;
import com.example.pacmanlike.objects.ghosts.engine.GhostsEngineAdvanced;

/**
 * Stores all object references that relevant for the game display
 * Calls objects business logic methods, and draw them to the given Canvas from DisplayThread
 * The individual parts are adapted to the game Pacman
 */
public class GameEnginePacman extends GameEngine {

    // game hero
    static PacMan _pacman;

    // maximal speed properties
    private static int _pacSpeed , _ghostsSpeed, _pacStep;

    // reward for eating the ghost
    private static int _GHOSTSCORE = 200;

    // ghost control
    private static GhostsEngine _ghostsEngine;

    // food association on the map
    private static GameFoods _foods;
    private Paint _paintPells, _paintPowerPells;

    /**
     * Stores all object references that relevant for the game display
     * Calls objects business logic methods, and draw them to the given Canvas from DisplayThread
     * In the constructor I will set the properties to the default pacman values
     * @param context Given context
     */
    public GameEnginePacman(Context context) {

        // parent constructor
        super(context);

        // painting properties
        _paintPells = new Paint();
        _paintPells.setStyle(Paint.Style.FILL);
        _paintPells.setColor(Color.rgb(255,165,0));
        _paintPowerPells = new Paint();
        _paintPowerPells.setStyle(Paint.Style.FILL);
        _paintPowerPells.setColor(Color.YELLOW);

        // Sets maximal speeds
        _pacSpeed = 10;
        _ghostsSpeed = 8;
        _pacStep = 1;

        // Adds game objects
        _pacman = new PacMan(context, AppConstants.getGameMap().getStartingPacPosition());
        _foods = new GameFoods( AppConstants.getGameMap());
        _ghostsEngine = new GhostsEngineAdvanced(context, AppConstants.getGameMap().getHome().getCoordinates());
    }

    /**
     * Updates all relevant objects business logic
     */
    @Override
    public void update() {

        // thread synchronization
        synchronized (_sync) {

            // divide the total motion by the partial shift into the smallest unit of step
            for (int step = 0; step < _pacSpeed; step++) {

                // set a new pacman movement if possible according to the user's choice
                updataPacmanDirection();

                // if the pacman hits the wall stop his movement
                detectWallCollision();

                // the logic of the ghosts changes ghosts direction based on new events in the game
                _ghostsEngine.update();


                // update scre after pacman eaten foods
                _SCORE += _foods.update();

                // the pacman moves in the selected direction one step
                movePacman(_pacStep);

                // update scre after pacman eaten foods
                _SCORE += _foods.update();

                // if the pacman is on the teleport, it will project it
                upadateTeleporation(_pacman);

                // the ghost moves in the selected direction one step
                if(step < _ghostsSpeed){
                    _ghostsEngine.moveGhosts(_pacStep);
                }

                // if the ghost is on the teleport, it will project it
                _ghostsEngine.updateTeleporation();

                // Interaction depending on the weakness of the ghost with the pacman
                interactionPacGhosts();

                // check victory
                isVictory();
            }
        }
    }

    /**
     * Draws all object on canvas.
     * @param canvas
     * 	canvas on which will be drawn the objects
     */
    @Override
    public void draw(Canvas canvas) {
        AppConstants.getGameMap().draw(canvas, _paint);
        _foods.draw(canvas, _paintPells, _paintPowerPells);
        _ghostsEngine.draw(canvas, _paint);
        _pacman.draw(canvas, _paint);
        _arrowIdenticator.draw(canvas, _paint);
    }

    /**
     * @return Player hero.
     */
    @Override
    public Object getHero() { return _pacman; }

    /**
     *
     * @return Pacman
     */
    public PacMan getPacman() { return _pacman; }

    /**
     * @return GhostEngine controler
     */
    public GhostsEngine getGhostsEngine(){return _ghostsEngine; }

    /**
     * Interaction of ghosts and pacman
     */
    private void interactionPacGhosts(){

        // pacman posotion
        Vector _pacmanPosition = _pacman.getAbsolutePosition();
        int _blockSize = AppConstants.getBlockSize();


        // for every ghost, check the collision with the pacman
        for (Ghost g: _ghostsEngine.getGhosts()) {

            // ghost position
            Vector gPosition = g.getAbsolutePosition();

            // axis distance
            int distanceX  = _pacmanPosition.x - gPosition.x;
            int distanceY = _pacmanPosition.y - gPosition.y;

            // ghost pcman distance
            int distance = (distanceX*distanceX) + (distanceY*distanceY);
            int radius = (_blockSize /4)*(_blockSize/4);

            // collision
            if( distance < radius){

                // if the ghost is vulnerable, then the pacman eats it
                if(g.isVulnerable()){
                    _SCORE += _GHOSTSCORE;
                    g.setVulnerable(false);
                    g.setRelativePosition(_ghostsEngine.getHome());

                }else {
                    // if the ghost is in normal mode, it will kill the pcman and end the game
                    _endGame = true;
                    _gameOver = true;
                }
            }
        }
    }

    /**
     * If the pacman is in the middle of the tile and cannot
     * move in his chosen direction, stop him
     */
    private void detectWallCollision() {
        GameMap gameMap = AppConstants.getGameMap();
        Vector position = _pacman.getAbsolutePosition();

        // if it is in the middle of the tile, check collision
        if(AppConstants.testCenterTile(position)) {

            Tile tile = gameMap.getAbsoluteTile(position.x, position.y);

            // detection collision
            if (!tile.getPossibleMoves().contains(_pacman.getDirection())) {
                if(_pacman.getDirection() != Direction.NONE) {
                    _pacman.setDirection(Direction.NONE);
                }
            }
        }
    }

    /**
     * Changes the direction of the pacman if it is
     * in the middle of the tile otherwise preserve the flood
     */
    private void updataPacmanDirection() {
        GameMap gameMap = AppConstants.getGameMap();
        Vector position = _pacman.getAbsolutePosition();

        // if it is in the middle of the tile, change direction
        if(AppConstants.testCenterTile(position)) {

            Tile tile = gameMap.getAbsoluteTile(position.x, position.y);

            if (tile.getPossibleMoves().contains(_arrowIdenticator.getDirection())) {
                _pacman.setDirection(_arrowIdenticator.getDirection());
            }
        }
    }

    /**
     * Move an object using a teleport
     * @param entity Object that will be teleporoted
     */
    private void upadateTeleporation(DrawableObjects entity){

        // game map
        GameMap gameMap = AppConstants.getGameMap();

        // position player
        Vector position = entity.getAbsolutePosition();

        // if it is in the middle of the teleport, move it to the opposite
        if(AppConstants.testCenterTile(position)) {

            Tile tile = gameMap.getAbsoluteTile(position.x, position.y);

            if(tile._type.equals("LeftTeleport"))
            {
                // opposite teleport position
                Vector right = gameMap.getRightTeleportPosition();
                entity.setRelativePosition(right);
            }

            if(tile._type.equals("RightTeleport")){

                // opposite teleport position
                Vector left = gameMap.getLeftTeleportPosition();
                entity.setRelativePosition(left);
            }
        }
    }

    /**
     * Moves the pcaman one step in the selected direction
     * @param step Size of step
     */
    private void movePacman(int step) {

        // pacman absolute position on screen
        Vector position = _pacman.getAbsolutePosition();

        // move the pcaman one step in the selected direction
        switch (_pacman.getDirection())
        {
            case UP:
                _pacman.setAbsolutePosition(new Vector(position.x, position.y - step));
                break;
            case DOWN:
                _pacman.setAbsolutePosition(new Vector(position.x, position.y + step));
                break;
            case LEFT:
                _pacman.setAbsolutePosition(new Vector(position.x - step, position.y));
                break;
            case RIGHT:;
                _pacman.setAbsolutePosition(new Vector(position.x + step, position.y));
                break;
            default:
                break;
        }
    }

    /**
     * Check victory
     * True if there is no food on the board
     */
    public void isVictory() {
        if(_foods.getNumberOfFoods() == 0){
            _endGame = true;
        }
    }
}
