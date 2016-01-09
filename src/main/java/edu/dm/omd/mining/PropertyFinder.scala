package edu.dm.omd.mining

import edu.dm.omd.domain.{Aspect, Word, Property}
import edu.dm.omd.tt4j.PartOfSpeechTransformator

import scala.collection.mutable.ListBuffer

object PropertyFinder {

  private val HAS_PROPERTIES: String = " has the following properties: "

  def findProperties(sentences: String): Unit = {
    sentences
      .split("\\.")
      .foreach(sentence => {
        println(sentence)
        val wordList = PartOfSpeechTransformator.transformToWordList(SentencePreprocessor.processSentences(sentence.split(" ")))
        findPropertiesInSentence(wordList)
      })
  }

  private def findPropertiesInSentence(wordList: java.util.List[Word]): Unit = {
    var lastNoun: Word = null
    var previousAdjectives = ListBuffer.empty[Word]
    var properties = collection.mutable.Map.empty[Word, ListBuffer[String]]
    var i = 0

    def treatAsObject(word: Word): Unit = {
      lastNoun = word
      addNounWithPropertiesToMap()
    }

    def addNounWithPropertiesToMap(): Unit = {
      properties += lastNoun -> ListBuffer.empty[String]
      if (previousAdjectives.nonEmpty) {
        addPropertyListToMap()
      }
    }

    def addPropertyListToMap(): Unit = {
      properties(lastNoun) ++= previousAdjectives.map(word => {
        word.toString
      })
    }

    def treatAsBePredicate: Unit = {
      if (wordList.get(i + 1).getPartOfSpeech.getAspect == Aspect.PROPERTY) {
        previousAdjectives = ListBuffer.empty[Word]
        i += 1
        var currentWord = wordList.get(i)
        while (currentWord != null && currentWord.getPartOfSpeech.getAspect == Aspect.PROPERTY) {
          SemanticOrientationDecider.decideSemanticOrientation(currentWord.asInstanceOf[Property])
          previousAdjectives += currentWord
          i += 1
          if (i < wordList.size()) {
            currentWord = wordList.get(i)
          } else {
            currentWord = null
          }
        }
        addPropertyListToMap()
      }
    }

    def treatAsAdverb(word: Word): Unit = {
      if (lastNoun != null) {
        var property = ""
        var verbFound = false
        if (i > 0 && wordList.get(i - 1).getPartOfSpeech.getAspect == Aspect.ACTION) {
          verbFound = true
          property += wordList.get(i - 1).getWord
        }
        property += " " + word.toString
        if (i < wordList.size() - 1) {
          val nextWord: Word = wordList.get(i + 1)
          if (nextWord.getPartOfSpeech.getAspect == Aspect.ACTION) {
            verbFound = true
            SemanticOrientationDecider.decideSemanticOrientation(nextWord.asInstanceOf[Property])
            property += " " + word.toString
          }
        }
        if (verbFound) properties(lastNoun) += property
      }
    }

    def isLikeObject(word: Word): Boolean = {
      word.getWord.equalsIgnoreCase("some") && (i < wordList.size() - 1 && wordList.get(i + 1).getPartOfSpeech.getAspect != Aspect.OBJECT) || i >= wordList.size() + 1
    }

    while(i <= wordList.size() - 1){
      val word = wordList.get(i)
      word.getPartOfSpeech.getAspect match {
        case Aspect.PROPERTY =>
          SemanticOrientationDecider.decideSemanticOrientation(word.asInstanceOf[Property])
          previousAdjectives += word
        case Aspect.ACTION =>
        case Aspect.IS_ACTION =>
          treatAsBePredicate
        case Aspect.ACTION_PROPERTY =>
          treatAsAdverb(word)
        case Aspect.OBJECT =>
          treatAsObject(word)
        case Aspect.OTHER =>
          if(isLikeObject(word)) {
              treatAsObject(word)
          }
      }
      if(word.getPartOfSpeech.getAspect != Aspect.PROPERTY) {
        previousAdjectives = ListBuffer.empty[Word]
      }
      i += 1
    }
    if (lastNoun != null && previousAdjectives.nonEmpty) {
      addNounWithPropertiesToMap()
    }
    properties.foreach(entry => {
     if(entry._2.nonEmpty) {
       println(entry._1.getWord + HAS_PROPERTIES + foldList(entry._2))
     }
    })
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
