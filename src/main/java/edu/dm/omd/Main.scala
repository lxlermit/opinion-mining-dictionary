package edu.dm.omd

import java.io.InputStream

import edu.dm.omd.domain.PartOfSpeech
import edu.dm.omd.util.PropertiesUtil
import org.annolab.tt4j.{TokenHandler, TreeTaggerWrapper}

object Main {

  def getResource(resourceName: String): InputStream = {
    Main.getClass.getResourceAsStream("../../../"+resourceName)
  }

  def main(args: Array[String]) {
    PropertiesUtil.loadSystemProperties()
    val line = scala.io.StdIn.readLine()
    val tt = new TreeTaggerWrapper[String]()
    try {
      tt.setModel("/home/arnold/Programs/DM/treetagger/models/english-utf8.par")
      tt setHandler new TokenHandler[String]() {
        def token(token: String, pos: String, lemma: String) {
          val p = new PartOfSpeech(pos)
          if(!p.getSynsetTypes.isEmpty)
            System.out.println(token + " " + p.getAspect + " "+p.getSynsetTypes.get(0).toString)
        }
      }
      tt.process(line.split(" "));
    }
    finally {
      tt.destroy()
    }
  }
}
