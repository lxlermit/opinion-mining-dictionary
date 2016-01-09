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
        String wordString = word.getWord().toLowerCase();
        if(Repository.INSTANCE.containsPositiveWord(wordString)) {
            word.setSemanticOrientation(SemanticOrientation.POSITIVE);
        } else if(Repository.INSTANCE.containsNegativeWord(wordString)) {
            word.setSemanticOrientation(SemanticOrientation.NEGATIVE);
        } else {
            List<String> synonyms = SynonymFinder.getSynonyms(word);
            for (String synonym : synonyms) {
                if (Repository.INSTANCE.containsPositiveWord(synonym)) {
                    word.setSemanticOrientation(SemanticOrientation.POSITIVE);
                    Repository.INSTANCE.addPositiveWord(word.getWord());
                    return;
                } else if (Repository.INSTANCE.containsNegativeWord(synonym)) {
                    word.setSemanticOrientation(SemanticOrientation.NEGATIVE);
                    Repository.INSTANCE.addNegativeWord(word.getWord());
                    return;
                }
            }
        }
        if(word.getSemanticOrientation() == SemanticOrientation.UNKNOWN) {
            Repository.INSTANCE.addNeutralWord(word.getWord());
        }
     }
}
