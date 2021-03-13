package com.example.pacmanlike.objects.ghosts.engine;

import android.content.Context;

import com.example.pacmanlike.gamemap.BFS;
import com.example.pacmanlike.gamemap.GameMap;
import com.example.pacmanlike.main.AppConstants;
import com.example.pacmanlike.objects.Direction;
import com.example.pacmanlike.objects.PacMan;
import com.example.pacmanlike.objects.Vector;
import com.example.pacmanlike.objects.ghosts.Ghost;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for controlling spirits and for coordinating them.
 * In this engine, ghosts move randomly or determine direction using bfs.
 */
public class GhostsEngineAdvanced extends GhostsEngine {

    // Search algorithm
    private BFS _bfs;

    // random moving ghost
    private int[] _randomGhost;

    // initial ghost
    private List<Integer> _ghostIDs;

    // number of random moving ghost
    private int _numberOfRandomGhosts;

    /**
     * Constructor for ghost control class.
     * Control based on bfs.
     * Initial properties in the specified context.
     * @param context GameView.
     * @param homePosition Ghost starting position.
     */
    public GhostsEngineAdvanced(Context context, Vector homePosition) {
        super(context, homePosition);

        // search algorithm (BFS)
        _bfs = new BFS(AppConstants.getGameMap());

        // number of random moving ghosts
        _numberOfRandomGhosts = 3;

        // IDs for selections
        _ghostIDs = new ArrayList<Integer>() {{
            add(0);
            add(1);
            add(2);
            add(3);
        }};

        // random moving ghost IDs
        _randomGhost = new int[_numberOfRandomGhosts];

        // selects from ghost IDs that will move randomly
        for(int i = 0; i < _numberOfRandomGhosts; i++){

            // selects ghost
            int index = _rand.nextInt(_ghostIDs.size());
            int id = _ghostIDs.get(index);

            // remove from bfs ghost
            _ghostIDs.remove((Object) id);

            // add
            _randomGhost[i] = id;

        }
    }

    /**
     * Starts vulnerabilities for all spirits
     */
    @Override
    public void startVulnereble(){
        super.startVulnereble();
    }

    /**
     * Changse the direction of ghost according to the navigation bfs algorithm.
     * @param g
     */
    private void updateBfS(Ghost g){

        // pacman relative position
        PacMan p = AppConstants.getEngine().getPacman();
        Vector relativeTarget = p.getRelativePosition();

        // ghost relative position
        Vector relativeGhost = g.getRelativePosition();

        // finds shortest path ghost-pacman
        // path cotains directions
        List<Direction> directions = _bfs.findPath(relativeGhost, relativeTarget);

        // if path == 0, then same position
        if(directions.size() == 0) {
            g.setDirection(Direction.NONE);
        } else {
            g.setDirection(directions.get(directions.size() - 1));
        }
    }

    /**
     * Changes the direction of ghosts according to the navigation algorithm.
     */
    @Override
    public void update(){
        GameMap gameMap = AppConstants.getGameMap();

        // Sets vulnerability
        if(_vulnerebleTicks == 0) {
            setVulnerable(false);
        } else {
            _vulnerebleTicks--;
        }

        // Random ghost
        for (Integer i: _randomGhost) {
            super.updateOne(gameMap, _ghosts.get(i));
        }

        // bfs ghost
        for (Integer i : _ghostIDs) {

            Ghost g = _ghosts.get(i);

            // ghost position
            Vector position = g.getAbsolutePosition();

            // if there is a tile in the center it can change direction
            if(AppConstants.testCenterTile(position)) {

                // vulnerable ghost moves randomly
                if(g.isVulnerable()) {
                    super.updateOne(gameMap, _ghosts.get(i));
                } else {

                    // bfs moves
                    updateBfS(g);
                }
            }
        }
    }
}
