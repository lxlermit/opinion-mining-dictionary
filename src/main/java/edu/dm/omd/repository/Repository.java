package edu.dm.omd.repository;

import edu.dm.omd.util.FilesUtil;
import edu.dm.omd.util.PropertiesUtil;

import java.util.HashSet;
import java.util.Set;

public enum Repository {

    INSTANCE;

    private final String positiveListFilePath = PropertiesUtil.getProperty("positiveListFilePath");
    private final String negativeListFilePath = PropertiesUtil.getProperty("negativeListFilePath");
    private final String neutralListFilePath = PropertiesUtil.getProperty("neutralListFilePath");

    private final Set<String> positiveWords = FilesUtil.readFromFile(positiveListFilePath);
    private final Set<String> negativeWords = FilesUtil.readFromFile(negativeListFilePath);
    private final Set<String> neutralWords = new HashSet<>();
    private final Set<String> newPositiveWords = new HashSet<>();
    private final Set<String> newNegativeWords = new HashSet<>();

    public boolean containsPositiveWord(String word) {
        return positiveWords.contains(word);
    }

    public boolean containsNegativeWord(String word) {
        return negativeWords.contains(word);
    }

    public void addPositiveWord(String word) {
        newPositiveWords.add(word);
    }

    public void addNegativeWord(String word) {
        newNegativeWords.add(word);
    }

    public void addNeutralWord(String word) {
        neutralWords.add(word);
     }

    public void addWordsToFile() {
        FilesUtil.appendToFile(positiveListFilePath, newPositiveWords);
        FilesUtil.appendToFile(negativeListFilePath, newNegativeWords);
        FilesUtil.writeToFile(neutralListFilePath, neutralWords);
    }
}
