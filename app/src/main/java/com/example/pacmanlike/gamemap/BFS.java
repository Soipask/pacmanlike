package com.example.pacmanlike.gamemap;

import android.widget.ArrayAdapter;

import com.example.pacmanlike.gamemap.tiles.Tile;
import com.example.pacmanlike.main.AppConstants;
import com.example.pacmanlike.objects.Direction;
import com.example.pacmanlike.objects.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BFS {

    /**
     * Represents the vertex for the BFS search algorithm
     */
    private class Node{

        // Vertex position
        Vector position;

        // Parent's node
        Node parent;

        // Direction from parent to node
        Direction direction;

        /**
         * Constructor for BFS node
         * @param position Node position in map
         * @param parent Node parent
         * @param direction Direction from parent to node
         */
        public Node(Vector position, Node parent, Direction direction){
            this.position = position;
            this.parent = parent;
            this.direction = direction;
        }
    }

    // game map
    GameMap gameMap;

    /**
     * Constructor for the BFS search algorithm.
     * The map parameter represents the graph being searched.
     * @param map Gqme map
     */
    public BFS(GameMap map){
        this.gameMap = map;
    }

    /**
     * Return true if exists path between start and target.
     * @param start Start position.
     * @param target Target position.
     * @return true if there is a path.
     */
    public boolean existsPath(Vector start, Vector target){
        List<Direction> tmp = findPath(start, target);

        if(tmp.size() > 0){
            return true;
        }

        return false;
    }


    /**
     * Returns true if the target is achievable.
     * @param target Target position.
     * @param node Start position.
     * @return true if the target is achievable.
     */
    public boolean achievedTarget(Vector target, Node node){

        if(target.x == node.position.x && target.y == node.position.y){
            return true;
        }

        return false;
    }

    /**
     * Returns the sequence of steps from position to goal.
     * Serves for the reconstruction of the road in BFS.
     * @param node Last node.
     * @return List of direction.
     */
    public List<Direction> getPath(Node node) {
        List<Direction> tmp = new ArrayList<Direction>();

        // coming back
        while (node.parent != null){

            tmp.add(node.direction);
            node = node.parent;
        }

        return tmp;
    }


    /**
     * BFS algorithm.
     * @param start Start position.
     * @param target Target Position.
     * @return Path from start to target.
     */
    public List<Direction> findPath(Vector start, Vector target){

        List<Direction> path = new ArrayList<Direction>();


        // Mark all the vertices as not visited
        boolean[][] visited = new boolean[AppConstants.MAP_SIZE_Y][AppConstants.MAP_SIZE_X];

        for(int y = 0; y< AppConstants.MAP_SIZE_Y; y++){
            for(int x = 0; x < AppConstants.MAP_SIZE_X; x++) {
                visited[y][x] = false;
            }
        }

        // Create a queue for BFS
        LinkedList<Node> queue = new LinkedList();

        // sets start vertex as visited
        visited[start.y][start.x] = true;

        // add to queue
        queue.add(new Node(start, null, null));

        Node s;

        // BFS
        while (queue.size() != 0) {

            // pop vertex
            s = queue.poll();
            int x = s.position.x;
            int y = s.position.y;

            // gets children
            List<Direction> possibleMoves = gameMap.getTile(x, y).getPossibleMoves();
            for (Direction d: possibleMoves) {

                // according to the directions where you can get, choose the shelf following this
                switch (d){
                    case LEFT:
                        if(!visited[y][x - 1]){

                            // set visited
                            visited[y][x - 1] = true;

                            // add to queue
                            Node node = new Node(new Vector(x - 1, y), s, Direction.LEFT);
                            queue.add(node);

                            // target test
                            if(achievedTarget(target, node)){
                                path = getPath(node);
                            }
                        }
                        break;
                    case RIGHT:
                        if(!visited[y][x + 1]){

                            // set visited
                            visited[y][x + 1] = true;

                            // add to queue
                            Node node = new Node(new Vector(x + 1, y), s, Direction.RIGHT);
                            queue.add(node);

                            // target test
                            if(achievedTarget(target, node)){
                                path = getPath(node);
                            }
                        }
                        break;
                    case DOWN:
                        if(!visited[y + 1][x]){

                            // set visited
                            visited[y + 1][x] = true;

                            // add to queue
                            Node node = new Node(new Vector(x, y + 1), s, Direction.DOWN);
                            queue.add(node);

                            // target test
                            if(achievedTarget(target, node)){
                                path = getPath(node);
                            }

                        }
                        break;
                    case UP:
                        if(!visited[y - 1][x]){

                            // set visited
                            visited[y - 1][x] = true;

                            // add to queue
                            Node node = new Node(new Vector(x, y - 1), s, Direction.UP);
                            queue.add(node);

                            // target test
                            if(achievedTarget(target, node)){
                                path = getPath(node);
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        return  path;
    }
}
