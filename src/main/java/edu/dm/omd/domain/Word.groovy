package edu.dm.omd.domain

class Word {

    String word
    PartOfSpeech partOfSpeech
    SemanticOrientation semanticOrientation

    public Word(String word, PartOfSpeech partOfSpeech) {
        this.word = word
        this.partOfSpeech = partOfSpeech
        this.semanticOrientation = SemanticOrientation.UNKNOWN
    }


    @Override
    public String toString() {
        return "Word{" +
                "word='" + word + '\'' +
                ", aspect=" + partOfSpeech.aspect +
                '}';
    }
}
