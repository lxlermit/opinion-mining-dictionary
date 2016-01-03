package edu.dm.omd.collector;

/**
 * Created by User on 03-01-16.
 */
public class Preprocessor {

    public static String processText(String text) {
        String newText = "";
        for (int i = 0; i < text.length() ; i++) {
            Character c = text.charAt(i);
            if(c.equals(',')) {
                newText += " " + c;
            } else {
                newText += c;
            }
        }
        return newText;
    }
}
