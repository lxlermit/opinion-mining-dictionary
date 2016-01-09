package edu.dm.omd.domain

class Word {

    String word
    PartOfSpeech partOfSpeech

    public Word(String word, PartOfSpeech partOfSpeech) {
        this.word = word
        this.partOfSpeech = partOfSpeech
    }


    @Override
    public String toString() {
        return word;
    }
}
