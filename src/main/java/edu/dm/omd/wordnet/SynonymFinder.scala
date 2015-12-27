package edu.dm.omd.wordnet

import java.util

import edu.dm.omd.domain.Word
import edu.smu.tspell.wordnet.{Synset, WordNetDatabase}

object SynonymFinder {


  def getSynonyms(word: Word): java.util.List[String] = {
    val database = WordNetDatabase.getFileInstance
    val synsets = database.getSynsets(word.getWord)
    def isTheSameType(synset: Synset): Boolean = {
      word.getPartOfSpeech.getSynsetTypes.contains(synset.getType)
    }
    val list = new util.ArrayList[String]()
    synsets.foreach { synset: Synset =>
      if (isTheSameType(synset)) {
        synset.getWordForms.foreach( wordForm =>
          if(wordForm != word.getWord) {
            list.add(wordForm)
          }
        )
      }
    }
    list
  }

}
