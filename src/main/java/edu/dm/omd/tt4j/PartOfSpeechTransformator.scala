package edu.dm.omd.tt4j

import edu.dm.omd.domain.{Word, PartOfSpeech}
import org.annolab.tt4j.{TokenHandler, TreeTaggerWrapper}
import collection.JavaConversions._

import scala.collection.mutable.ListBuffer

object PartOfSpeechTransformator {
  private val MODEL_FILE:String = "/home/arnold/Programs/DM/treetagger/models/english-utf8.par"

  def transformToWordList(sentence: Array[String]): java.util.List[Word] = {
    var words = ListBuffer.empty[Word]
    val treeTaggerWrapper = new TreeTaggerWrapper[String]()
    try {
      treeTaggerWrapper.setModel(PartOfSpeechTransformator.MODEL_FILE)
      treeTaggerWrapper setHandler new TokenHandler[String]() {
        def token(token: String, partOfSpeech: String, lemma: String) {
          val p = new PartOfSpeech(partOfSpeech)
          words += new Word(token, p)
          //if(!p.getSynsetTypes.isEmpty)
            System.out.println(token + " " + p.getAspect + " "+p.getPosTag)
        }
      }
      treeTaggerWrapper.process(sentence);
    }
    finally {
      treeTaggerWrapper.destroy()
    }
    words.toList
  }
}