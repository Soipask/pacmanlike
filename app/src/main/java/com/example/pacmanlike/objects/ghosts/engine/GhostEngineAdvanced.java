package com.example.pacmanlike.objects.ghosts.engine;

import android.content.Context;

import com.example.pacmanlike.gamemap.GameMap;
import com.example.pacmanlike.gamemap.tiles.Tile;
import com.example.pacmanlike.main.AppConstants;
import com.example.pacmanlike.objects.Direction;
import com.example.pacmanlike.objects.Vector;
import com.example.pacmanlike.objects.ghosts.Ghost;

import java.util.ArrayList;
import java.util.List;

public class GhostEngineAdvanced extends GhostsEngine {


    private int[] _randomGhost;
    private List<Integer> _ghostIDs;

    private int _numberOfRandomGhosts;

    public GhostEngineAdvanced(Context context, Vector homePosition) {
        super(context, homePosition);
        _numberOfRandomGhosts = 2;
        _ghostIDs = new ArrayList<Integer>() {{
            add(0);
            add(1);
            add(2);
            add(3);
        }};

        _randomGhost = new int[_numberOfRandomGhosts];

        for(int i = 0; i < _numberOfRandomGhosts; i++){

            int id = _ghostIDs.get(_rand.nextInt(_ghostIDs.size()));
            _ghostIDs.remove(id);

            _randomGhost[i] = id;

        }
    }

    @Override
    public void startVulnereble(){
        super.startVulnereble();

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



        }
    }
}
