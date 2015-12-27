package edu.dm.omd.wordnet

import edu.smu.tspell.wordnet.{Synset, SynsetType, WordNetDatabase}
import collection.JavaConversions._

object SynonymFinder {


  def getSynonyms(word: String): Unit = {
    val database = WordNetDatabase.getFileInstance
    val synsets = database.getSynsets(word)
    def isAdjectiveOrAdverbSynset(synset: Synset): Boolean = {
      synset.getType == SynsetType.ADJECTIVE || synset.getType == SynsetType.ADJECTIVE_SATELLITE || synset.getType == SynsetType.ADVERB
    }
//    synsets.reduce((list: Array[String], synset) =>
//      if(isAdjectiveOrAdverbSynset(synset)) {
//        list ++ synset.getWordForms
//      } else {
//         list
//      }
//    )

  }

}
