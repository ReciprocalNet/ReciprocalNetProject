/*
 * Reciprocal Net Project
 *
 * SaveFrameTests.java
 * 
 * 30-Mar-2005: jobollin wrote first draft
 */

package org.recipnet.common.files;

/**
 * A JUnit TestCase that exercises the behavior of the {@code CifFile.SaveFrame}
 * class; most of its tests are inherited from its abstract superclass,
 * {@code DataCellTests} 
 * 
 * @author  jobollin
 * @version 0.9.0
 */
public class SaveFrameTests extends DataCellTests {
    
    private final static String FRAME_NAME = "frame";
    
    private CifFile.SaveFrame frame;

    /**
     * Initializes a new {@code SaveFrameTests} to run the named test
     * 
     * @param  testName the name of the test to run
     */
    public SaveFrameTests(String testName) {
        super(testName);
    }

    /**
     * Prepares this test case to run a test; concrete subclasses should
     * delegate to this method after performing their own set up
     */
    @Override
    public void setUp() {
        frame = new CifFile.SaveFrame(FRAME_NAME);
        super.setUp();
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.recipnet.common.files.DataCellTests#getTestSubject()
     */
    @Override
    public CifFile.SaveFrame getTestSubject() {
        return frame;
    }

    /**
     * {@inheritDoc}
     * 
     * @see DataCellTests#testConstructor_String__null()
     */
    @Override
    public void testConstructor_String__null() {
        try {
            new CifFile.SaveFrame(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see DataCellTests#testConstructor_String()
     */
    @Override
    public void testConstructor_String() {
        assertEquals("Wrong frame name", FRAME_NAME, frame.getName());
    }

}
