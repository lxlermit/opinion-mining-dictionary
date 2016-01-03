package edu.dm.omd.collector;

import edu.dm.omd.collector.TextCollector;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by User on 02-01-16.
 */
public class TextCollectorTest {

    @Before
    public void before() {
    }

    @Test
    public void testCollectOpinionatedText() {
        System.out.println(TextCollector.collectText());
    }

}
