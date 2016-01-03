package edu.dm.omd

import java.io.InputStream

import edu.dm.omd.collector.{Preprocessor, TextCollector}
import edu.dm.omd.mining.PropertyFinder

import edu.dm.omd.util.PropertiesUtil

import scala.io.StdIn

object Main {

  def getResource(resourceName: String): InputStream = {
    getClass.getResourceAsStream("../../../" + resourceName)
  }

  def init(): Unit = {
    PropertiesUtil.loadSystemProperties()
    PropertiesUtil.loadApplicationProperties()
  }

  def main(args: Array[String]) {
    init()
    //val sentences = Preprocessor.processText(TextCollector.collectText())
    val sentences = StdIn.readLine()
    PropertyFinder.findProperties(sentences)
  }
}
