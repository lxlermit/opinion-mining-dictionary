package edu.dm.omd.mining;

import edu.dm.omd.domain.SemanticOrientation;
import edu.dm.omd.domain.Word;
import edu.dm.omd.repository.Repository;
import edu.dm.omd.util.FilesUtil;
import edu.dm.omd.util.PropertiesUtil;
import edu.dm.omd.wordnet.SynonymFinder;

import java.util.List;

public class SemanticOrientationDecider {

    public static void decideSemanticOrientation(Word word) {
        if(Repository.INSTANCE.containsPositiveWord(word.getWord())) {
            word.setSemanticOrientation(SemanticOrientation.POSITIVE);
        } else if(Repository.INSTANCE.containsNegativeWord(word.getWord())) {
            word.setSemanticOrientation(SemanticOrientation.NEGATIVE);
        } else {
            List<String> synonyms = SynonymFinder.getSynonyms(word);
            for (String synonym : synonyms) {
                if (Repository.INSTANCE.containsPositiveWord(synonym)) {
                    word.setSemanticOrientation(SemanticOrientation.POSITIVE);
                    return;
                } else if (Repository.INSTANCE.containsNegativeWord(synonym)) {
                    word.setSemanticOrientation(SemanticOrientation.NEGATIVE);
                    return;
                }
            }
        }
        if(word.getSemanticOrientation().equals("UNKNOWN")) {
            FilesUtil.addToFile(PropertiesUtil.getProperty("neutralFilePath"), word.getWord());
        }
     }
}
