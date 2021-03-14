package com.example.pacmanlike.objects.ghosts.engine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.pacmanlike.gamemap.GameMap;
import com.example.pacmanlike.gamemap.tiles.Tile;
import com.example.pacmanlike.main.AppConstants;
import com.example.pacmanlike.objects.Direction;
import com.example.pacmanlike.objects.Vector;
import com.example.pacmanlike.objects.ghosts.Ghost;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class for controlling ghosts and for coordinating them.
 * In this engine, ghosts move randomly or determine direction using random moving.
 */
public class GhostsEngine {

    // radomization
    protected Random _rand;

    // ghosts starting position
    protected static Vector _homePosition;

    // ghost vulnerebility
    protected static int _vulnerebleDuration;
    protected static int _vulnerebleTicks;

    // ghosts
    protected List<Ghost> _ghosts;


    /**
     * Constructor for ghost control class.
     * Control based on random.
     * Initial properties in the specified context.
     * @param context GameView.
     * @param homePosition Ghost starting position.
     */
    public GhostsEngine(Context context, Vector homePosition) {

        _vulnerebleDuration = 1500;
        _rand = new Random();
        _homePosition = homePosition;

        // Creates ghosts
        _ghosts = new ArrayList<Ghost>();

        _ghosts.add(new Ghost(context, _homePosition, 0));
        _ghosts.add(new Ghost(context, _homePosition, 1));
        _ghosts.add(new Ghost(context, _homePosition, 2));
        _ghosts.add(new Ghost(context, _homePosition, 3));
    }

    /**
     * Gets the ghosts.
     * @return List of ghosts.
     */
    public List<Ghost> getGhosts(){return _ghosts; }

    /**
     * Starts vulnerabilities for all spirits
     */
    public void startVulnereble(){
        _vulnerebleTicks = _vulnerebleDuration;
        setVulnerable(true);
    }

    /**
     * Returns ghost home postion.
     * @return Home position.
     */
    public Vector getHome() {return _homePosition; }

    /**
     * Sets ghosts vulnerability.
     * @param value Vulnerability.
     */
    public void setVulnerable(boolean value) {
        for (Ghost g: _ghosts) {
            g.setVulnerable(value);
        }
    }

    /**
     * Changes the direction of ghost according to the random direction.
     * @param gameMap Gmae map
     * @param g Ghost
     */
    protected void updateOne(GameMap gameMap, Ghost g){

        // ghost position
        Vector position = g.getAbsolutePosition();

        // if there is a tile in the center it can change direction
        if(AppConstants.testCenterTile(position)){

            // possible moves on tile
            Tile tile = gameMap.getAbsoluteTile(position.x, position.y);
            List<Direction> moves = tile.getPossibleMoves();

            // randomly choose move
            if(moves.size() == 1)  {
                g.setDirection(moves.get(0));
            } else {

                Direction dir = moves.get(_rand.nextInt(moves.size()));
                g.setDirection(dir);
            }
        }
    }

    /**
     * Changes the direction of ghosts according to the navigation algorithm (random).
     */
    public void update(){
        GameMap gameMap = AppConstants.getGameMap();

        // Sets vulnerability
        if(_vulnerebleTicks == 0) {
            setVulnerable(false);
        } else {
            _vulnerebleTicks--;
        }


        // updates randomly one ghost
        for (Ghost g: _ghosts) {
            updateOne(gameMap, g);
        }
    }

    /**
     * Moves the ghost to the opposite teleport if ther is on teleport tile.
     */
    public void updateTeleporation(){

        // game map
        GameMap gameMap = AppConstants.getGameMap();

        for (Ghost g: _ghosts) {

            // ghost position
            Vector position = g.getAbsolutePosition();

            // test center
            if(AppConstants.testCenterTile(position)) {

                // tile position
                Tile tile = gameMap.getAbsoluteTile(position.x, position.y);


                if(tile._type.equals("LeftTeleport"))
                {
                    // moves to right teleport
                    Vector right = gameMap.getRightTeleportPosition();
                    g.setRelativePosition(right);
                }

                if(tile._type.equals("RightTeleport")){

                    // moves to left teleport
                    Vector left = gameMap.getLeftTeleportPosition();

                    g.setRelativePosition(left);
                }
            }
        }
    }

    /**
     * It moves the ghosts in their directions and updates their position.
     * @param step Size of step.
     */
    public void moveGhosts(int step){
        for (Ghost g: _ghosts) {

            // ghost position
            Vector position = g.getAbsolutePosition();

            // move in the selected direction
            switch (g.getDirection()) {
                case UP:
                    g.setAbsolutePosition(new Vector(position.x, position.y - step));
                    break;
                case DOWN:
                    g.setAbsolutePosition(new Vector(position.x, position.y + step));
                    break;
                case LEFT:
                    g.setAbsolutePosition(new Vector(position.x - step, position.y));
                    break;
                case RIGHT: ;
                    g.setAbsolutePosition(new Vector(position.x + step, position.y));
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Draws the all ghosts on canvas.
     * @param canvas Given canvas.
     * @param paint Paint.
     */
    public void draw(Canvas canvas, Paint paint){

        for (Ghost g : _ghosts) {
            g.draw(canvas, paint);
        }
    }
}
