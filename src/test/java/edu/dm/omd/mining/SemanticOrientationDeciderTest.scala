package edu.dm.omd.mining

import javax.jws.soap.InitParam

import edu.dm.omd.domain.{SemanticOrientation, PartOfSpeech, Word}
import edu.dm.omd.util.PropertiesUtil
import org.junit.{Before, Test}
import org.junit.Assert._

class SemanticOrientationDeciderTest {

  @Before
  def init(): Unit = {
    //PropertiesUtil.loadApplicationProperties()
    //PropertiesUtil.loadSystemProperties()
  }

  @Test
  def decideSemanticOrientationTest():Unit = {
    var word = new Word("good", new PartOfSpeech("JJ"))
    SemanticOrientationDecider.decideSemanticOrientation(word)
    assertTrue(word.getSemanticOrientation == SemanticOrientation.POSITIVE)
    word = new Word("pet", new PartOfSpeech("JJ"))
    SemanticOrientationDecider.decideSemanticOrientation(word)
    assertTrue(word.getSemanticOrientation == SemanticOrientation.UNKNOWN)
  }

}
