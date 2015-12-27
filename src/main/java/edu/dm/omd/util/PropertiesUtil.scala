package edu.dm.omd.util

import java.util.Properties
import collection.JavaConversions._

import edu.dm.omd.Main

object PropertiesUtil {

  def loadSystemProperties(): Unit = {
    val properties = new Properties()
    val inputStream = Main.getResource("system.properties")
    properties.load(inputStream)
    for(entry <- properties.entrySet()) {
      val key = entry.getKey.toString
      val value = entry.getValue.toString
      System.setProperty(key, value)
    }
  }

}
