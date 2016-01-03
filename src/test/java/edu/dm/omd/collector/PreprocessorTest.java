package edu.dm.omd.collector;

import edu.dm.omd.collector.Preprocessor;
import edu.dm.omd.collector.TextCollector;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by User on 02-01-16.
 */
public class PreprocessorTest {

    @Before
    public void before() {
    }

    @Test
    public void testProcessText() {
        System.out.println(Preprocessor.processText("Ana are mere, pere, banane."));
    }

}
