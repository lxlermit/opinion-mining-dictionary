package edu.dm.omd.collector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class TextCollector {

    public static String collectText() {
        Document doc;
        String text = "";
        try {
            doc = Jsoup.connect("http://www.reviews.com/dog-food/").get();
            for (Element element : doc.select("#What_We_Learned div.content-block p")) {
               text += element.text();
            }
            return text;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
