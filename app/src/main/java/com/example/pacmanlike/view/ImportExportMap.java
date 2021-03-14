package com.example.pacmanlike.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.example.pacmanlike.main.AppConstants;

/**
 * Handles exporting a map from easily parsable csv
 * to continuous text and importing it back again
 */
public class ImportExportMap {

    /**
     * Translates the exported code of the map to the proper csv parsable by game parser.
     * @param level Code of the map got by exporting a map
     * @return Proper, parsable csv
     */
    public static String importMap(String level){
        String newLines = level.replace(AppConstants.EXPORT_NEW_LINE, AppConstants.CHAR_CSV_NEWLINE);
        String delimitersAlso = newLines.replace(AppConstants.EXPORT_CSV_DELIMITER, AppConstants.CHAR_CSV_DELIMITER);
        String moreData = delimitersAlso.replace(AppConstants.EXPORT_MORE_DATA, AppConstants.CHAR_MORE_DATA);
        String coordsAlso = moreData.replace(AppConstants.EXPORT_COORDS, AppConstants.CHAR_COORDS);
        String importString = coordsAlso.replace(AppConstants.EXPORT_KEY_VALUE, AppConstants.CHAR_KEY_VALUE);

        return importString;
    }

    /**
     * Translates the csv of the map to the continuous string.
     * @param level Csv code of the map
     * @return Export code of the map
     */
    public static String exportMap(String level){
        String noNewLines = level.replace(AppConstants.CHAR_CSV_NEWLINE, AppConstants.EXPORT_NEW_LINE);
        String noDelimiters = noNewLines.replace(AppConstants.CHAR_CSV_DELIMITER, AppConstants.EXPORT_CSV_DELIMITER);
        String noMoreData = noDelimiters.replace(AppConstants.CHAR_MORE_DATA, AppConstants.EXPORT_MORE_DATA);
        String noCoords = noMoreData.replace(AppConstants.CHAR_COORDS, AppConstants.EXPORT_COORDS);
        String exportString = noCoords.replace(AppConstants.CHAR_KEY_VALUE,AppConstants.EXPORT_KEY_VALUE);

        return exportString;
    }

    /**
     * Copies the given export text to clipboard.
     * @param context Context of the application
     * @param string Text to be copied
     */
    public static void copyToClipboard(Context context, String string){
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(AppConstants.CLIPBOARD_LABEL, string);
        clipboard.setPrimaryClip(clip);
    }
}
