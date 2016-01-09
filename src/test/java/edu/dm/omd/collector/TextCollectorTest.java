package edu.dm.omd.collector;

import org.junit.Before;
import org.junit.Test;

public class TextCollectorTest {

    @Before
    public void before() {
    }

    @Test
    public void testCollectOpinionatedText() {
        System.out.println(TextCollector.collectText());
    }

}
