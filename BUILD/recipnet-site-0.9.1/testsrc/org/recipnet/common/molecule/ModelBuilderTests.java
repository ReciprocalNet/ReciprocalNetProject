/*
 * IUMSC Reciprocal Net Project
 *
 * ModelBuilderTests.java
 *
 * Nov 2, 2006: jobollin wrote first draft
 */

package org.recipnet.common.molecule;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.recipnet.common.Element;
import org.recipnet.common.files.CifFile;
import org.recipnet.common.files.cif.CifParser;

/**
 * TODO: Write class docs
 *
 * @author jobollin
 * @version 1.0
 */
public class ModelBuilderTests extends TestCase {

    private final static File cifFilePath
            = new File(new File("test_files"), "06651.cif");
    
    private CifFile cif;
    
    /**
     * Initializes a new {@code ModelBuilderTests} to run the named test
     *
     * @param testName the name of the test to run
     */
    public ModelBuilderTests(String testName) {
        super(testName);
    }

    public void setUp() throws Exception {
        super.setUp();
        
        InputStream is
                = new BufferedInputStream(new FileInputStream(cifFilePath));
        CifParser parser = new CifParser();
        
        cif = parser.parseCif(is);
        is.close();
    }
    
    private ModelBuilder getDefaultModelBuilder() {
        return new ModelBuilder(cif.blockIterator().next());
    }
    
    public void testMethod_buildSimpleModel__atomicMotions() {
        MolecularModel<FractionalAtom, Bond<FractionalAtom>> model
                = getDefaultModelBuilder().buildSimpleModel();
        
        for (FractionalAtom atom : model.getAtoms()) {
            AtomicMotion motion;
            
            assertNotNull("Model contains a null atom", atom);
            motion = atom.getAtomicMotion();
            assertNotNull("Model atom has null motion: " + atom.getLabel(),
                    motion);
            assertTrue("Model atom motion has wrong isotropicity: "
                    + atom.getLabel(),
                    motion.isAnisotropic()
                            == (atom.getElement() != Element.HYDROGEN));
        }
    }
    
    public static Test suite() {
        TestSuite ts = new TestSuite(ModelBuilderTests.class);
        
        ts.setName("ModelBuilder Tests");
        return ts;
    }
}
