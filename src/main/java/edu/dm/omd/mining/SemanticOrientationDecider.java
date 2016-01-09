package edu.dm.omd.mining;

import edu.dm.omd.domain.Property;
import edu.dm.omd.domain.SemanticOrientation;
import edu.dm.omd.repository.Repository;
import edu.dm.omd.util.FilesUtil;
import edu.dm.omd.util.PropertiesUtil;
import edu.dm.omd.wordnet.SynonymFinder;

import java.util.List;

public class SemanticOrientationDecider {

    public static void decideSemanticOrientation(Property property) {
        String wordString = property.getWord().toLowerCase();
        if(Repository.INSTANCE.containsPositiveWord(wordString)) {
            property.setSemanticOrientation(SemanticOrientation.POSITIVE);
        } else if(Repository.INSTANCE.containsNegativeWord(wordString)) {
            property.setSemanticOrientation(SemanticOrientation.NEGATIVE);
        } else {
            List<String> synonyms = SynonymFinder.getSynonyms(property);
            for (String synonym : synonyms) {
                if (Repository.INSTANCE.containsPositiveWord(synonym)) {
                    property.setSemanticOrientation(SemanticOrientation.POSITIVE);
                    return;
                } else if (Repository.INSTANCE.containsNegativeWord(synonym)) {
                    property.setSemanticOrientation(SemanticOrientation.NEGATIVE);
                    return;
                }
            }
        }
        if(property.getSemanticOrientation() == SemanticOrientation.UNKNOWN) {
            FilesUtil.addToFile(PropertiesUtil.getProperty("neutralListFilePath"), wordString);
        }
     }
}
