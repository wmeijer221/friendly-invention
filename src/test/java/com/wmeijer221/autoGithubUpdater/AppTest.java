package com.wmeijer221.autoGithubUpdater;

import com.wmeijer221.autoGithubUpdater.updater.GithubAutoUpdater;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest
        extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        GithubAutoUpdater updater = new GithubAutoUpdater("wmeijer221", "friendly-invention");
        updater.fetchUpdates().join();
    }
}
