package com.ingteamsofindia.black_forest.Util;

import java.io.File;
import java.util.ArrayList;

public class FileSearch {

    /**
    * Search a directory and return a list of all " Directory " contained inside*/
    public static ArrayList<String> getDirectoryPaths(String directory){
        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] listFile = file.listFiles();
        for (int i = 0; i<listFile.length; i++){
            if (listFile[i].isDirectory()){
                pathArray.add(listFile[i].getAbsolutePath());
            }
        }
        return pathArray;
    }

    /**
     * Search a directory and return a list of all " Files " contained inside*/
    public static ArrayList<String>getFilePaths(String directory){
        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] listFile = file.listFiles();
        for (int i = 0; i<listFile.length; i++){
            if (listFile[i].isFile()){
                pathArray.add(listFile[i].getAbsolutePath());
            }
        }
        return pathArray;
    }
}
