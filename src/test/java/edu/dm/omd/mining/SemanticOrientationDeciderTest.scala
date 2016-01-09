package edu.dm.omd.mining


import edu.dm.omd.domain.{SemanticOrientation, PartOfSpeech, Property}
import org.junit.Test
import org.junit.Assert._

class SemanticOrientationDeciderTest {

  @Test
  def decideSemanticOrientationTest():Unit = {
    var word = new Property("good", new PartOfSpeech("JJ"))
    SemanticOrientationDecider.decideSemanticOrientation(word)
    assertTrue(word.getSemanticOrientation == SemanticOrientation.POSITIVE)
    word = new Property("pet", new PartOfSpeech("JJ"))
    SemanticOrientationDecider.decideSemanticOrientation(word)
    assertTrue(word.getSemanticOrientation == SemanticOrientation.UNKNOWN)
  }

}
