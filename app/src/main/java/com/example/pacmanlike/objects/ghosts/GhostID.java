package com.example.pacmanlike.objects.ghosts;

import com.example.pacmanlike.R;

public class GhostID {

    public static int[][] getSpriteID(int id){
        int[][] tmp = new int[2][7];

        // vulnerable sprites
        tmp[1] = new int[]{
                R.drawable.ghost_vulnerable_0,
                R.drawable.ghost_vulnerable_1,
                R.drawable.ghost_vulnerable_2,
                R.drawable.ghost_vulnerable_3,
                R.drawable.ghost_vulnerable_4,
                R.drawable.ghost_vulnerable_5,
                R.drawable.ghost_vulnerable_6,
                R.drawable.ghost_vulnerable_7,
        };

        int[] ghost;

        switch (id){
            case 0:
                ghost = new int[]{
                        R.drawable.ghost_0_0,
                        R.drawable.ghost_0_1,
                        R.drawable.ghost_0_2,
                        R.drawable.ghost_0_3,
                        R.drawable.ghost_0_4,
                        R.drawable.ghost_0_5,
                        R.drawable.ghost_0_6,
                        R.drawable.ghost_0_7,
                };
                break;
            case 1:
                ghost = new int[]{
                        R.drawable.ghost_1_0,
                        R.drawable.ghost_1_1,
                        R.drawable.ghost_1_2,
                        R.drawable.ghost_1_3,
                        R.drawable.ghost_1_4,
                        R.drawable.ghost_1_5,
                        R.drawable.ghost_1_6,
                        R.drawable.ghost_1_7,
                };
                break;
            case 2:
                ghost = new int[]{
                        R.drawable.ghost_2_0,
                        R.drawable.ghost_2_1,
                        R.drawable.ghost_2_2,
                        R.drawable.ghost_2_3,
                        R.drawable.ghost_2_4,
                        R.drawable.ghost_2_5,
                        R.drawable.ghost_2_6,
                        R.drawable.ghost_2_7,
                };
                break;
            default:
                ghost = new int[]{
                        R.drawable.ghost_3_0,
                        R.drawable.ghost_3_1,
                        R.drawable.ghost_3_2,
                        R.drawable.ghost_3_3,
                        R.drawable.ghost_3_4,
                        R.drawable.ghost_3_5,
                        R.drawable.ghost_3_6,
                        R.drawable.ghost_3_7,
                };
                break;
        }
        tmp[0] = ghost;
        return  tmp;
    }
}
