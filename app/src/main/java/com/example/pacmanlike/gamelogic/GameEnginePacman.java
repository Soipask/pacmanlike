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
import com.example.pacmanlike.objects.Foods;
import com.example.pacmanlike.objects.Vector;
import com.example.pacmanlike.objects.ghosts.Ghost;
import com.example.pacmanlike.objects.ghosts.engine.GhostsEngine;
import com.example.pacmanlike.objects.ghosts.engine.GhostsEngineAdvanced;

public class GameEnginePacman extends GameEngine {

    static PacMan _pacman;
    private static int _pacSpeed , _ghostsSpeed, _pacStep;
    private static int _GHOSTSCORE = 200;
    private static GhostsEngine _ghostsEngine;
    private static Foods _foods;
    private Paint _paintPells, _paintPowerPells;

    public GameEnginePacman(Context context) {
        super(context);

        _paintPells = new Paint();
        _paintPells.setStyle(Paint.Style.FILL);
        _paintPells.setColor(Color.rgb(255,165,0));
        _paintPowerPells = new Paint();
        _paintPowerPells.setStyle(Paint.Style.FILL);
        _paintPowerPells.setColor(Color.YELLOW);

        _pacSpeed = 10;
        _ghostsSpeed = 8;
        _pacStep = 1;

        // Adds game objects
        _pacman = new PacMan(context, AppConstants.getGameMap().getStartingPacPosition());
        _foods = new Foods( AppConstants.getGameMap());
        _ghostsEngine = new GhostsEngineAdvanced(context, AppConstants.getGameMap().getHome().getCoordinates());
    }

    @Override
    public void update() {
        synchronized (_sync) {

            for (int step = 0; step < _pacSpeed; step++) {
                updataPacmanDirection();
                detectWallCollision();
                _ghostsEngine.update();

                _SCORE += _foods.update();
                movePacman(_pacStep);
                _SCORE += _foods.update();

                upadateTeleporation(_pacman);

                if(step < _ghostsSpeed){
                    _ghostsEngine.moveGhosts(_pacStep);
                }

                _ghostsEngine.updateTeleporation();

                interactionPacGhosts();

                isVictory();
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        AppConstants.getGameMap().draw(canvas, _paint);
        _foods.draw(canvas, _paintPells, _paintPowerPells);
        _ghostsEngine.draw(canvas, _paint);
        _pacman.draw(canvas, _paint);
        _arrowIdenticator.draw(canvas, _paint);
    }

    @Override
    public Object getHero() { return _pacman; }

    public PacMan getPacman() { return _pacman; }

    public GhostsEngine getGhostsEngine(){return _ghostsEngine; }

    private void interactionPacGhosts(){

        Vector _pacmanPosition = _pacman.getAbsolutePosition();
        int _blockSize = AppConstants.getBlockSize();

        for (Ghost g: _ghostsEngine.getGhosts()) {

            Vector gPosition = g.getAbsolutePosition();
            int distanceX  = _pacmanPosition.x - gPosition.x;
            int distanceY = _pacmanPosition.y - gPosition.y;

            int distance = (distanceX*distanceX) + (distanceY*distanceY);
            int radius = (_blockSize /4)*(_blockSize/4);

            if( distance < radius){

                if(g.isVulnerable()){
                    _SCORE += _GHOSTSCORE;
                    g.setVulnerable(false);
                    g.setRelativePosition(_ghostsEngine.getHome());

                }else {
                    _endGame = true;
                    _gameOver = true;
                }
            }
        }
    }

    private void detectWallCollision() {
        GameMap gameMap = AppConstants.getGameMap();
        Vector position = _pacman.getAbsolutePosition();

        if(AppConstants.testCenterTile(position)) {

            Tile tile = gameMap.getAbsoluteTile(position.x, position.y);

            if (!tile.getPossibleMoves().contains(_pacman.getDirection())) {
                if(_pacman.getDirection() != Direction.NONE) {
                    _pacman.setDirection(Direction.NONE);
                }
            }
        }
    }

    private void updataPacmanDirection() {
        GameMap gameMap = AppConstants.getGameMap();
        Vector position = _pacman.getAbsolutePosition();

        if(AppConstants.testCenterTile(position)) {

            Tile tile = gameMap.getAbsoluteTile(position.x, position.y);

            if (tile.getPossibleMoves().contains(_arrowIdenticator.getDirection())) {
                _pacman.setDirection(_arrowIdenticator.getDirection());
            }
        }
    }

    private void upadateTeleporation(DrawableObjects entity){
        GameMap gameMap = AppConstants.getGameMap();
        Vector position = entity.getAbsolutePosition();

        if(AppConstants.testCenterTile(position)) {

            Tile tile = gameMap.getAbsoluteTile(position.x, position.y);
            // int _blockSize = AppConstants.getBlockSize();

            if(tile.type.equals("LeftTeleport"))
            {
                Vector right = gameMap.getRightTeleportPosition();
                entity.setRelativePosition(right);
            }

            if(tile.type.equals("RightTeleport")){
                Vector left = gameMap.getLeftTeleportPosition();

                entity.setRelativePosition(left);
            }
        }
    }

    /**
     *
     * @param step
     */
    private void movePacman(int step) {
        Vector position = _pacman.getAbsolutePosition();
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

    public void isVictory() {
        if(_foods.getNumberOfPells() == 0){
            _endGame = true;
        }
    }
}
