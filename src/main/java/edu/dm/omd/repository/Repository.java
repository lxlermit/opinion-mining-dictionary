package edu.dm.omd.repository;

import edu.dm.omd.util.FilesUtil;
import edu.dm.omd.util.PropertiesUtil;

import java.util.Set;

public enum Repository {

    INSTANCE;

    private final String positiveListFileName = PropertiesUtil.getProperty("positiveListFileName");
    private final String negativeListFileName = PropertiesUtil.getProperty("negativeListFileName");

    private final Set<String> positiveWords = FilesUtil.readFromFile(positiveListFileName);
    private final Set<String> negativeWords = FilesUtil.readFromFile(negativeListFileName);

    public boolean containsPositiveWord(String word) {
        return positiveWords.contains(word);
    }

    public boolean containsNegativeWord(String word) {
        return negativeWords.contains(word);
    }

    public void addPositiveWord(String word) {
        if(!this.containsPositiveWord(word)) {
            positiveWords.add(word);
        }
    }

    public void addNegativeWord(String word) {
        if(!this.containsNegativeWord(word)) {
            negativeWords.add(word);
        }
    }
}
