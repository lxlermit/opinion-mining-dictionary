package edu.dm.omd.wordnet

import java.util

import edu.smu.tspell.wordnet.{Synset, SynsetType, WordNetDatabase}
import collection.JavaConversions._

object SynonymFinder {


  def getSynonyms(word: String): java.util.List[String] = {
    val database = WordNetDatabase.getFileInstance
    val synsets = database.getSynsets(word)
    def isAdjectiveOrAdverbSynset(synset: Synset): Boolean = {
      synset.getType == SynsetType.ADJECTIVE || synset.getType == SynsetType.ADJECTIVE_SATELLITE || synset.getType == SynsetType.ADVERB
    }
    val list = new util.ArrayList[String]()
    synsets.foreach { synset: Synset =>
      if (isAdjectiveOrAdverbSynset(synset)) {
        synset.getWordForms.foreach( wordForm =>
          if(wordForm != word) {
            list.add(wordForm)
          }
        )
      }
    }
    list
  }

}
