package com.example.pacmanlike.objects.ghosts.engine;

import android.content.Context;

import com.example.pacmanlike.gamemap.BFS;
import com.example.pacmanlike.gamemap.GameMap;
import com.example.pacmanlike.gamemap.tiles.Tile;
import com.example.pacmanlike.main.AppConstants;
import com.example.pacmanlike.objects.Direction;
import com.example.pacmanlike.objects.PacMan;
import com.example.pacmanlike.objects.Vector;
import com.example.pacmanlike.objects.ghosts.Ghost;

import java.util.ArrayList;
import java.util.List;

public class GhostsEngineAdvanced extends GhostsEngine {

    private BFS _bfs;
    private int[] _randomGhost;
    private List<Integer> _ghostIDs;

    private int _numberOfRandomGhosts;

    public GhostsEngineAdvanced(Context context, Vector homePosition) {
        super(context, homePosition);

        _bfs = new BFS(AppConstants.getGameMap());
        _numberOfRandomGhosts = 3;

        _ghostIDs = new ArrayList<Integer>() {{
            add(0);
            add(1);
            add(2);
            add(3);
        }};

        _randomGhost = new int[_numberOfRandomGhosts];

        for(int i = 0; i < _numberOfRandomGhosts; i++){
            int index = _rand.nextInt(_ghostIDs.size());
            int id = _ghostIDs.get(index);
            _ghostIDs.remove((Object) id);
            _randomGhost[i] = id;

        }
    }

    @Override
    public void startVulnereble(){
        super.startVulnereble();

    }

    private void updateBfS(Ghost g){

        PacMan p = AppConstants.getEngine().getPacman();
        Vector relativeTarget = p.getRelativePosition();

        if (g.isVulnerable()) {
            relativeTarget = new Vector(AppConstants.MAP_SIZE_X - relativeTarget.x, AppConstants.MAP_SIZE_Y - relativeTarget.y);
        }


        Vector relativeGhost = g.getRelativePosition();
        List<Direction> directions = _bfs.findPath(relativeGhost, relativeTarget);

        if(directions.size() == 0) {
            g.setDirection(Direction.NONE);
        } else {
            g.setDirection(directions.get(directions.size() - 1));
        }
    }

    @Override
    public void update(){
        GameMap gameMap = AppConstants.getGameMap();

        if(_vulnereble == 0) {
            setVulnerable(false);
        } else {
            _vulnereble--;
        }

        for (Integer i: _randomGhost) {
            super.updateOne(gameMap, _ghosts.get(i));
        }


        for (Integer i : _ghostIDs) {
            Ghost g = _ghosts.get(i);

            Vector position = g.getAbsolutePosition();

            if(AppConstants.testCenterTile(position)) {
                    updateBfS(g);
            }
        }
    }
}
