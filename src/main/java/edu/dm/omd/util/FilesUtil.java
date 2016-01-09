package edu.dm.omd.util;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class FilesUtil {

    public static Set<String> readFromFile(String fileName) {
        Set<String> set = new HashSet<>();
        String currentLine;
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName))))
        {
            while ((currentLine = br.readLine()) != null) {
                set.add(currentLine.toLowerCase());
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return set;
    }

    public static void addToFile(String fileName, String data) {
        try{
            FileWriter fileWritter = new FileWriter(fileName, true);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            bufferWritter.newLine();
            bufferWritter.write(data);
            bufferWritter.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
