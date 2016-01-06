package edu.dm.omd

import java.io.InputStream

import edu.dm.omd.collector.{TextPreprocessor, TextCollector}
import edu.dm.omd.mining.PropertyFinder

import edu.dm.omd.util.PropertiesUtil

import scala.io.StdIn

object Main {

  def getResource(resourceName: String): InputStream = {
    getClass.getResourceAsStream("../../../" + resourceName)
  }

  def main(args: Array[String]) {
    val sentences = TextPreprocessor.processText(TextCollector.collectText())
    //val sentences = StdIn.readLine()
    PropertyFinder.findProperties(sentences)
  }
}
