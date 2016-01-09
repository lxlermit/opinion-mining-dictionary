package edu.dm.omd.domain

class Property extends Word{

    SemanticOrientation semanticOrientation

    Property(String word, PartOfSpeech partOfSpeech) {
        super(word, partOfSpeech)
        this.semanticOrientation = SemanticOrientation.UNKNOWN
    }

    @Override
    String toString() {
        return word + " (" + semanticOrientation.toString() + ")"
    }
}
