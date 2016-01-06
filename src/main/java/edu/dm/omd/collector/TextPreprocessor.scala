package edu.dm.omd.collector

object TextPreprocessor {

  private val TEXT_REGEX = "[\"'”“`]"

  def processText(text: String): String = {
    text.foldLeft("")(
      (acc, character) => {
        if(!character.toString.matches(TEXT_REGEX)) {
          acc + character
        } else {
          acc
        }
      }
    )
  }

}
