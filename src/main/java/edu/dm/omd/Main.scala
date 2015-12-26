package edu.dm.omd

import edu.dm.omd.util.PropertiesUtil
import org.annolab.tt4j.{TokenHandler, TreeTaggerWrapper}

object Main {

  def main(args: Array[String]) {
    PropertiesUtil.loadSystemProperties()
    val line = scala.io.StdIn.readLine()
    val tt = new TreeTaggerWrapper[String]()
    try {
      tt.setModel("/home/arnold/Programs/DM/treetagger/models/english-utf8.par")
      tt setHandler new TokenHandler[String]() {
        def token(token: String, pos: String, lemma: String) {
          System.out.println(token + "\t" + pos + "\t" + lemma)
        }
      }
      tt.process(line.split(" "));
    }
    finally {
      tt.destroy()
    }
  }
}
