package edu.dm.omd.repository;

import edu.dm.omd.util.FilesUtil;
import edu.dm.omd.util.PropertiesUtil;

import java.util.HashSet;
import java.util.Set;

public enum Repository {

    INSTANCE;

    private final String positiveListFileName = PropertiesUtil.getProperty("positiveListFilePath");
    private final String negativeListFileName = PropertiesUtil.getProperty("negativeListFilePath");
    private final String neutralListFileName = PropertiesUtil.getProperty("neutralListFilePath");

    private final Set<String> positiveWords = FilesUtil.readFromFile(positiveListFileName);
    private final Set<String> negativeWords = FilesUtil.readFromFile(negativeListFileName);
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
}
