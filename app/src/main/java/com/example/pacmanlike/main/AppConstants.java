package com.example.pacmanlike.main;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.example.pacmanlike.gamelogic.GameEnginePacman;
import com.example.pacmanlike.gamemap.GameMap;
import com.example.pacmanlike.objects.Vector;
import com.example.pacmanlike.view.LevelView;

/**
 * Class containing the constants and functions needed to run the game.
 */
public class AppConstants {

    // game
    static GameEnginePacman _engine;
    static GameMap _gameMap;

    // map size
    public static int MAP_SIZE_X = 7, MAP_SIZE_Y = 9;

    // tile size
    private static int _blockSize;

    // screen dimensions
    public static int SCREEN_WIDTH,
            SCREEN_HEIGHT;

    public static final String LEVEL_NAME = "com.example.pacmanlike.LEVEL_NAME";
    public static final String SELECTED_LEVEL = "com.example.pacmanlike.SELECTED_LEVEL";
    public static final String STORAGE_ASSETS = "Assets", STORAGE_INTERNAL = "Internal";

    // Game constants
    public static String PAC_STARTING_KEYWORD = "PAC", POWER_STARTING_KEYWORD = "POWER";
    public static String CSV_DELIMITER = ",", KEY_VALUE_DELIMITER = "=", COORDS_DELIMITER = ";", MORE_DATA_DELIMITER = "/";
    public static char CHAR_CSV_DELIMITER = ',', CHAR_KEY_VALUE = '=', CHAR_COORDS = ';', CHAR_MORE_DATA = '/';
    public static char CHAR_CSV_NEWLINE = '\n';
    public static String LEFT_TELEPORT = "LeftTeleport", RIGHT_TELEPORT = "RightTeleport", HOME_TILE = "Home";
    public static String CSV_EXTENSION = ".csv", HIGHSCORE_EXTENSION = ".hsc";
    public static String CLIPBOARD_LABEL = "Export a map";
    public static char CHAR_EMPTY_TILE = 'X', CHAR_HOME_TILE = 'A';

    // Export and import
    public static char EXPORT_CSV_DELIMITER = 'x', EXPORT_KEY_VALUE ='k', EXPORT_COORDS = 'c', EXPORT_MORE_DATA = 'm';
    public static char EXPORT_NEW_LINE = 'n';

    public static int MAX_POWER_PELLETS = 4;


    /**
     * Initiates the application constants
     * */
    public static void initialization(Context context, GameMap map) {
        _gameMap = map;
        setScreenSize(context);
        _engine = new GameEnginePacman(context);
        LevelView levelView = new LevelView(map);
        map.setBackground(levelView.createLevelBitmap(context));
    }

    /**
     * Sets the size of one block according to the size of the device display
     */
    private static void setBlockSize() {
        int height = AppConstants.SCREEN_HEIGHT / AppConstants.MAP_SIZE_Y;
        int width = AppConstants.SCREEN_WIDTH / AppConstants.MAP_SIZE_X;

        _blockSize = height;

        if(height > width) {
            _blockSize = width;
        }
    }

    /**
     * Sets screen size constants accordingly to device screen size
     * */
    private static void setScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        AppConstants.SCREEN_WIDTH = width;
        AppConstants.SCREEN_HEIGHT = height;

        setBlockSize();
    }

    /**
     * Returns the size of one block
     * @return block size
     */
    public static int getBlockSize() { return _blockSize; }

    /**
     * @return GameMap instance
     * */
    public static GameMap getGameMap() { return _gameMap; }


    /**
     * @return GameEngine instance
     * */
    public static GameEnginePacman getEngine() { return _engine; }

    public static boolean testCenterTile(Vector position) {


        int centerX = position.x % _blockSize;
        int centerY = position.y % _blockSize;

        if(centerX == _blockSize/2 && centerY == _blockSize /2) {
            return true;
        }else {
            return false;
        }
    }
    /**
     * Stops the given thread
     * @param thread
     * 			thread to stop
     * */
    public static void stopThread(Thread thread) {
        boolean retry = true;
        while (retry) {
            try {
                thread.join();
                retry = false;
            }
            catch (InterruptedException e) {}
        }
    }
}