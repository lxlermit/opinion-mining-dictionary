package edu.dm.omd.mining

import edu.dm.omd.domain.{Aspect, Word}
import edu.dm.omd.tt4j.PartOfSpeechTransformator

import scala.collection.mutable.ListBuffer

object PropertyFinder {

  private val HAS_PROPERTIES: String = " has the following properties: "

  def findProperties(sentences: String): Unit = {
    sentences
      .split("\\.")
      .foreach(sentence => {
        println(sentence)
        val wordList = PartOfSpeechTransformator.transformToWordList(sentence.split(" "))
        findPropertiesInSentence(wordList)
      })
  }

  private def findPropertiesInSentence(wordList: java.util.List[Word]): Unit = {
    var lastNoun: Word = null
    //var lastVerb: Word = null
    var previousAdjectives = ListBuffer.empty[Word]
    var properties = collection.mutable.Map.empty[Word, ListBuffer[String]]
    for(i <- 0 to (wordList.size() - 1)){
      val word = wordList.get(i)
      word.getPartOfSpeech.getAspect match {
        case Aspect.PROPERTY => previousAdjectives += word
        case Aspect.ACTION => //lastVerb = word
        case Aspect.ACTION_PROPERTY =>
          if(lastNoun != null) {
            var property = ""
            var verbFound = false
            if(i > 0 && wordList.get(i - 1).getPartOfSpeech.getAspect == Aspect.ACTION) {
              verbFound = true
              property += wordList.get(i - 1).getWord
            }
            property += " " + word.getWord
            if(i < wordList.size() - 1) {
              val nextWord: Word = wordList.get(i + 1)
              if(nextWord.getPartOfSpeech.getAspect == Aspect.ACTION) {
                verbFound = true
                property += " " + nextWord.getWord
              }
            }
            if(verbFound) properties(lastNoun) += property
          }
        case Aspect.OBJECT =>
          lastNoun = word
          properties += lastNoun -> ListBuffer.empty[String]
          if (previousAdjectives.nonEmpty) {
            properties(lastNoun) ++= previousAdjectives.map(word => word.getWord)
            previousAdjectives = ListBuffer.empty[Word]
          }
        case Aspect.OTHER =>
          if(word.getWord.equalsIgnoreCase("some")) {
            if((i < wordList.size() - 1 && wordList.get(i+1).getPartOfSpeech.getAspect != Aspect.OBJECT) || i >= wordList.size() + 1) {
              lastNoun = word
              properties += lastNoun -> ListBuffer.empty[String]
              if (previousAdjectives.nonEmpty) {
                properties(lastNoun) ++= previousAdjectives.map(word => word.getWord)
                previousAdjectives = ListBuffer.empty[Word]
              }
            }
          }
      }
    }
    if (lastNoun != null && previousAdjectives.nonEmpty) {
      println(lastNoun.getWord + HAS_PROPERTIES + foldAdjectives(previousAdjectives))
    }
    properties.foreach(entry => {
     if(entry._2.nonEmpty) {
       println(entry._1.getWord + HAS_PROPERTIES + foldList(entry._2))
     }
    })
  }

  private def foldAdjectives(previousAdjectives: ListBuffer[Word]): String = {
    previousAdjectives.foldLeft("")(
      (acc, adjective) => if (acc != "") {
        acc + ", " + adjective.getWord
      } else {
        adjective.getWord
      }
    )
  }

  private def foldList(properties: ListBuffer[String]): String = {
    properties.foldLeft("")(
      (acc, property) => if(acc != "") {
        acc + ", " + property
      } else {
        property
      }
    )
  }
}
