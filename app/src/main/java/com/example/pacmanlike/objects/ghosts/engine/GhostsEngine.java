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
 *
 */
public class GhostsEngine {

    protected Random _rand;
    protected static Vector _homePosition;

    protected static int VULNEREBLE;
    protected static int _vulnereble;

    protected List<Ghost> _ghosts;

    /**
     *
     * @param context
     * @param homePosition
     */
    public GhostsEngine(Context context, Vector homePosition) {

        VULNEREBLE = 1500;

        _rand = new Random();

        _homePosition = homePosition;
        _ghosts = new ArrayList<Ghost>();

        _ghosts.add(new Ghost(context, _homePosition, 0));
        _ghosts.add(new Ghost(context, _homePosition, 1));
        _ghosts.add(new Ghost(context, _homePosition, 2));
        _ghosts.add(new Ghost(context, _homePosition, 3));

    }

    /**
     *
     * @return
     */
    public List<Ghost> getGhosts(){return _ghosts; }

    /**
     *
     */
    public void startVulnereble(){
        _vulnereble = VULNEREBLE;
        setVulnerable(true);

    }

    /**
     *
     * @return
     */
    public Vector getHome() {return _homePosition; }

    /**
     *
     * @param value
     */
    public void setVulnerable(boolean value) {
        for (Ghost g: _ghosts) {
            g.setVulnerable(value);
        }
    }

    /**
     *
     * @param gameMap
     * @param g
     */
    protected void updateOne(GameMap gameMap, Ghost g){
        Vector position = g.getAbsolutePosition();

        if(AppConstants.testCenterTile(position)){

            Tile tile = gameMap.getAbsoluteTile(position.x, position.y);

            List<Direction> moves = tile.getPossibleMoves();

            if(moves.size() == 1)  {
                g.setDirection(moves.get(0));
            } else {

                Direction dir = moves.get(_rand.nextInt(moves.size()));
                g.setDirection(dir);
            }
        }
    }

    /**
     *
     */
    public void update(){
        GameMap gameMap = AppConstants.getGameMap();

        if(_vulnereble == 0) {
            setVulnerable(false);
        } else {
            _vulnereble--;
        }


        for (Ghost g: _ghosts) {
            updateOne(gameMap, g);
        }
    }

    /**
     *
     */
    public void updateTeleporation(){
        GameMap gameMap = AppConstants.getGameMap();

        for (Ghost g: _ghosts) {
            Vector position = g.getAbsolutePosition();

            if(AppConstants.testCenterTile(position)) {

                Tile tile = gameMap.getAbsoluteTile(position.x, position.y);
                if(tile.type.equals("LeftTeleport"))
                {
                    Vector right = gameMap.getRightTeleportPosition();
                    g.setRelativePosition(right);
                }

                if(tile.type.equals("RightTeleport")){
                    Vector left = gameMap.getLeftTeleportPosition();

                    g.setRelativePosition(left);
                }
            }
        }
    }

    /**
     *
     * @param step
     */
    public void moveGhosts(int step){
        for (Ghost g: _ghosts) {
            Vector position = g.getAbsolutePosition();

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
     *
     * @param canvas
     * @param paint
     */
    public void draw(Canvas canvas, Paint paint){

        for (Ghost g : _ghosts) {
            g.draw(canvas, paint);
        }
    }
}
