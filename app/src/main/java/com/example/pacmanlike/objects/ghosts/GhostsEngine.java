package com.example.pacmanlike.objects.ghosts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.example.pacmanlike.gamemap.GameMap;
import com.example.pacmanlike.gamemap.tiles.Tile;
import com.example.pacmanlike.main.AppConstants;
import com.example.pacmanlike.objects.Direction;
import com.example.pacmanlike.objects.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GhostsEngine {

    private Random _rand;
    private static Vector _homePosition;

    private static int VULNEREBLE;
    private static int _vulnereble;

    private List<Ghost> _ghosts;

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

    public List<Ghost> getGhosts(){return _ghosts; }

    public void startVulnereble(){
        _vulnereble = VULNEREBLE;
        setVulnerable(true);

    }

    public Vector getHome() {return _homePosition; }

    public void setVulnerable(boolean value) {
        for (Ghost g: _ghosts) {
            g.setVulnerable(value);
        }
    }

    public void update(){

        if(_vulnereble == 0) {
            setVulnerable(false);
        }else {
            _vulnereble--;
        }

        GameMap gameMap = AppConstants.getGameMap();
        for (Ghost g: _ghosts) {

            Vector position = g.getAbsolutePosition();

            if(AppConstants.testCenterTile(position)){

                Tile tile = gameMap.getAbsoluteTile(position.x, position.y);

                 List<Direction> moves = tile.getPossibleMoves();

                 if(moves.size() == 1)  {
                     g.setDirection(moves.get(0));
                 } else {
                     // moves.remove(Direction.getOppositeDirection(g.getDirection()));
                     Direction dir = moves.get(_rand.nextInt(moves.size()));
                     g.setDirection(dir);
                 }
            }
        }
    }

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

    public void draw(Canvas canvas, Paint paint){

        for (Ghost g : _ghosts) {
            g.draw(canvas, paint);
        }
    }
}
