package edu.dm.omd.wordnet

import edu.dm.omd.domain.{PartOfSpeech, Word}
import edu.dm.omd.util.PropertiesUtil
import org.junit.{Before, Test}

class SynonymFinderTest {

  @Before
  def init(): Unit = {
    PropertiesUtil.loadSystemProperties()
  }

  @Test
  def testGetSynonyms(): Unit = {
    val word = new Word("good", new PartOfSpeech("JJ"))
    SynonymFinder.getSynonyms(word).toArray.foreach(println)
  }

}
