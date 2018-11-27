package org.altumtek.filemanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FileManager {
    private static String files[] = {
            "Adventures of Tintin" ,
            "Jack and Jill" ,
            "Glee" ,
            "The Vampire Diarie" ,
            "King Arthur" ,
            "Windows XP" ,
            "Harry Potter" ,
            "Kung Fu Panda" ,
            "Lady Gaga" ,
            "Twilight" ,
            "Windows 8" ,
            "Mission Impossible" ,
            "Turn Up The Music" ,
            "Super Mario" ,
            "American Pickers" ,
            "Microsoft Office 2010" ,
            "Happy Feet" ,
            "Modern Family" ,
            "American Idol" ,
            "Hacking for Dummies"
    };
    private static FileManager fileManager;

    private List<String> myFiles = new ArrayList<>();

    private FileManager(){
        Random r= new Random();
        for(int i = 0; i < 5; i++) {
            myFiles.add(files[r.nextInt(files.length)].toLowerCase());
        }
    }

    public static FileManager getIntance(){
        if (fileManager == null) {
            fileManager = new FileManager();
        }
        return fileManager;
    }

    public List<String> getMatchingFiles(String name) {
        String keyword = name.toLowerCase();
        List<String> searchResult = new ArrayList<>();
        for(String file: myFiles) {
            if(file.contains(keyword)) {
                searchResult.add(file);
            }
        }
        return searchResult;
    }

    public List<String> getMyFiles() {
        return myFiles;
    }
}
