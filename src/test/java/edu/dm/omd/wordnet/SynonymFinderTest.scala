package edu.dm.omd.wordnet

import edu.dm.omd.util.PropertiesUtil
import org.junit.{Before, Test}

class SynonymFinderTest {

  @Before
  def init(): Unit = {
    PropertiesUtil.loadSystemProperties()
  }

  @Test
  def testGetSynonyms(): Unit = {
    SynonymFinder.getSynonyms("quirky")
  }

}
