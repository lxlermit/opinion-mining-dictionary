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

    public static void appendToFile(String fileName, Set<String> data) {
        try(BufferedWriter bufferWritter = new BufferedWriter(new FileWriter(fileName))){
            for (String s : data) {
                writeToStream(s, bufferWritter);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private static void writeToStream(String data, BufferedWriter bufferWritter) throws IOException {
        bufferWritter.write(data);
        bufferWritter.newLine();
    }

    public static void writeToFile(String fileName, Set<String> data) {
        try(BufferedWriter bufferWritter = new BufferedWriter(new FileWriter(fileName))){
            for (String s : data) {
                writeToStream(s, bufferWritter);
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
