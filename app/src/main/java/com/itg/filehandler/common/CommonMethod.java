package com.itg.filehandler.common;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.io.File;
import java.io.FileFilter;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Comparator;



public class CommonMethod {
    public static final String PATH = "path";
    public static final int FILE = 1;
    public static final int FOLDER= 2;

    public static File[] getListOfDir(File mainPath) {
        FileFilter directoryFilter = new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory();
            }
        };

        //if(mainPath.exists() && mainPath.length()>0){
        //Lista de directorios
        File[] tempDirectoryList = mainPath.listFiles();

        if (tempDirectoryList == null || tempDirectoryList.length <= 0) {
            return tempDirectoryList;
        }

        if (tempDirectoryList.length > 1) {
            Arrays.sort(tempDirectoryList, new Comparator<File>() {
                @Override
                public int compare(File object1, File object2) {
                    return object1.getName().compareTo(object2.getName());
                }
            });
        }
        return tempDirectoryList;
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor != null ? cursor.getColumnIndexOrThrow("_data") : 0;
                String path="";
                if (cursor != null && cursor.moveToFirst()) {
                    path= cursor.getString(column_index);
                }
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Exception e) {
                // Eat it
                return "err";
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

}
