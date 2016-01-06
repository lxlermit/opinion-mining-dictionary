package edu.dm.omd.mining;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SentencePreprocessor {

    private static final String PREPROCESS_REGEX = ".*[a-zA-Z0-9].*";

    public static String[] processSentences(String[] text) {
        List<String> strings = new ArrayList<>();
        for (String string : text) {
            String toAdd = string;
            if(toAdd.endsWith(",")) {
                toAdd = toAdd.substring(0, toAdd.length() - 1);
            }
            if(Pattern.matches(PREPROCESS_REGEX, toAdd)) {
                strings.add(toAdd);
            }
        }
        return strings.toArray(new String[strings.size()]);
    }
}
