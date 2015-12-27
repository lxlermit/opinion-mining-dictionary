package edu.dm.omd.repository;

import edu.dm.omd.util.PropertiesUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by User on 27-12-15.
 */
public class RepositoryTest {

    @Before
    public void before() {
        PropertiesUtil.loadApplicationProperties();
    }

    @Test
    public void testPopulatedWithSuccessPositiveWordsList() {
        assertTrue("true", Repository.INSTANCE.containsPositiveWord("Fantastic"));
        assertTrue("true", Repository.INSTANCE.containsPositiveWord("Superb"));
        assertTrue("true", Repository.INSTANCE.containsPositiveWord("Enthusiastic"));
    }

    @Test
    public void testPopulatedWithSuccessNegativeWordsList() {
        assertTrue(Repository.INSTANCE.containsNegativeWord("angry"));
        assertTrue(Repository.INSTANCE.containsNegativeWord("smelly"));
        assertTrue(Repository.INSTANCE.containsNegativeWord("yucky"));
    }

    @Test
    public void testAddedWithSuccessPositiveWord() {
        Repository.INSTANCE.addPositiveWord("nice");
        assertTrue(Repository.INSTANCE.containsPositiveWord("nice"));
    }

    @Test
    public void testAddedWithSuccessNegativeWord() {
        Repository.INSTANCE.addNegativeWord("worst");
        assertTrue(Repository.INSTANCE.containsNegativeWord("worst"));
    }
}
