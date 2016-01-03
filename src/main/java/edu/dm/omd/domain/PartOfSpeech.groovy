package edu.dm.omd.domain

import edu.smu.tspell.wordnet.SynsetType

class PartOfSpeech {

    String posTag
    List<SynsetType> synsetTypes = new ArrayList<>()
    Aspect aspect

    public PartOfSpeech(String posTag) {
        this.posTag = posTag
        if(posTag.startsWith("JJ")) {
            synsetTypes.add(SynsetType.ADJECTIVE)
            synsetTypes.add(SynsetType.ADJECTIVE_SATELLITE)
            aspect = Aspect.PROPERTY
        } else if(posTag.startsWith("V")) {
            synsetTypes.add(SynsetType.VERB)
            aspect = Aspect.ACTION
        } else if(posTag.startsWith("N") || posTag.startsWith("PP")) {
            synsetTypes.add(SynsetType.NOUN)
            aspect = Aspect.OBJECT
        } else if(posTag.startsWith("RB")) {
            synsetTypes.add(SynsetType.ADVERB)
            aspect = Aspect.ACTION_PROPERTY
        } else {
            aspect = Aspect.OTHER
        }
    }


}
