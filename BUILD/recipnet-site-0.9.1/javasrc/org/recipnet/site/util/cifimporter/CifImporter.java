/*
 * Reciprocal Net project
 * 
 * CifImporter.java
 *
 * 10-Jun-2002: jobollin wrote first draft
 * 19-Aug-2002: jobollin completed version 0.5.0
 * 21-Aug-2002: jobollin added a --help option and usage message
 * 11-Dec-2002: eisiorho added displayUsage() that's invoked when the
 *              command-line is empty
 * 17-Dec-2002: eisiorho added code to handle --configfile= argument
 * 05-Mar-2003: jobollin added simple-minded handling for the new exceptions
 *              (task #762)
 * 05-Mar-2003: jobollin made the --configfile= option optional, with the
 *              default specified in the usage message
 * 07-Mar-2003: jobollin commented the RuntimeException catch (task #767)
 * 22-Apr-2003: ekoperda corrected call to SampleManager.putSampleInfo() inside
 *              displayGUI()
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 29-Jan-2004: jobollin made the "store" operation simulate the standard
 *              workflow so that the data can be correctable through the webapp
 *              after import
 * 29-Jan-2004: jobollin changed the default config file location and removed
 *              explicit path strings containing system-dependant file seperator
 *              characters
 * 29-Jan-2004: jobollin modified linkToCore() to use the site host name from
 *              the config properties instead of assuming localhost
 * 16-Feb-2004: jobollin enumerated and sorted imports
 * 17-Feb-2004: jobollin added code to exclude deactivated providers from
 *              the list of providers in whose name a sample may be imported
 * 17-Feb-2004: jobollin added code to limit the length of text entered into
 *              the GUI to that permitted in the corresponding DB fields,
 *              where applicable
 * 25-Feb-2004: jobollin added a "configdir=" command-line option, and changed
 *              the default to be unspecified (only the file name to be used to
 *              find the config file)
 * 25-Feb-2004: jobollin added several missing documentation comments
 * 01-Jun-2004: cwestnea modified linkToCore() to use GenRmiPort configuration
 *              property
 * 08-Aug-2004: cwestnea made changes throughout to use SampleWorkflowBL
 * 14-Dec-2004: eisiorho changed texttype references to use new class
 *              SampleTextBL
 * 06-Oct-2005: midurbin updated references to SampleInfo.providerId
 * 11-May-2006: jobollin switched a reference from
 *              SiteManagerRemote.getLocalLabs2()
 *              to SiteManagerRemote.getLocalLabs()
 * 07-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.util.cifimporter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.naming.NameNotFoundException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.NumberFormatter;

import org.recipnet.site.OperationFailedException;
import org.recipnet.site.RecipnetException;
import org.recipnet.site.core.RepositoryManagerRemote;
import org.recipnet.site.core.SampleManagerRemote;
import org.recipnet.site.core.SiteManagerRemote;
import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.shared.db.ProviderInfo;
import org.recipnet.site.shared.db.SampleAnnotationInfo;
import org.recipnet.site.shared.db.SampleAttributeInfo;
import org.recipnet.site.shared.db.SampleDataInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.UserInfo;

/**
 * an application for reading one or more CIF files and populating the
 * Reciprocal Net database with their contents.
 *
 * @author John C. Bollinger
 * @version 0.6.2
 */
public class CifImporter implements Runnable {

    private final static String DEFAULT_CONF_DIR = null;
    private final static String DEFAULT_CONF_NAME = "recipnet-utils.conf";

    static SiteManagerRemote siteManager;
    static SampleManagerRemote sampleManager;
    static RepositoryManagerRemote repositoryManager;

    static List<LabInfo> localLabList = null;
    static Map<LabInfo, List<ProviderInfo>> labProviderMap = null;

    private static SortedMap<String, Object> blockMap = null;

    private static boolean debug = false;

    /**
     * a {@code CifParser} configured to read from the appropriate source
     */
    protected CifParser cifIn;

    /**
     * a {@code String} containing the name of the file this
     * {@code CifImporter} will read, or {@code null} if this
     * {@code CifImporter} will read the standard input
     */
    protected String fileName;

    /**
     * set true when a CifImporter instance is constructed that reads stdin.
     * Only one instance can be so configured.
     */
    protected boolean lockStdin = false;

    /*
     * a view of a Map, intended to be used with a Map such as returned
     * by CifParser.getNextDataBlock().  A {@code CifMap} caches the
     * wrapped map's data block name and error list, and maintains a flag
     * indicating whether or not this block has been stored.
     */
    static class CifMap {

        /** The underlying Map for this CifMap */
        private Map<String, Object> dataMap;

        /** The name string exposed by this CifMap */
        private String name;

        /** The error list exposed by this CifMap */
        private List<CifError> errors;

        /**
         * A flag that can be set to indicate that this CifMap's data have been
         * saved in persistent storage
         */
        private boolean stored;

        /**
         * Constructs a new, empty CifMap
         */
        CifMap() {
            init(new HashMap<String, Object>());
        }

        /**
         * Constructs a new CifMap based on the data in the specified Map
         *
         * @param  m The {@code Map} on which this CifMap is based;
         *         its contents are copied to an internal Map
         *
         * @throws NullPointerException if {@code m} is {@code null}
         */
        CifMap(Map<String, Object> m) {
            init(new HashMap<String, Object>(m));
        }

        /**
         * Invoked by the constructors to set the initial state of the CifMap;
         * only the stored flag can be modified after construction, and it only
         * once
         */
        private void init(Map<String, Object> m) {
            dataMap = m;
            name = (String) dataMap.get(CifParser.BLOCK_NAME_KEY);
            if (name == null) {
                name = "";
            }
            errors = (List<CifError>) dataMap.get(CifParser.ERRORS_KEY);
            if (errors == null) {
                errors = new ArrayList<CifError>(0);
            }
            stored = false;
        }

        /**
         * Returns the value of this CifMap's {@code stored} flag, which is
         * initially {@code false}
         *
         * @return {@code true} if and only if this CifMap has been marked
         *         as stored since its construction
         */
        public final boolean isStored() {
            return stored;
        }

        /**
         * Marks this CifMap as stored; no effect on the second or subsequent
         * invocations
         */
        public final void setStored() {
            stored = true;
        }

        /**
         * Returns an unmodifiable view of this CifMap's error list
         *
         * @return an unmodifiable view of the error list
         */
        public final List getErrors() {
            return Collections.unmodifiableList(errors);
        }

        /**
         * Returns whether or not this CifMap contains any error messages
         *
         * @return {@code true} if and only if this CifMap's error list
         *         is non-empty
         */
        public final boolean hasErrors() {
            return (errors.size() > 0);
        }

        /**
         * Returns an unmodifiable view of this CifMap's underlying map
         *
         * @return an unmodifiable view of this CifMap's data map
         */
        public final Map getMap() {
            return Collections.unmodifiableMap(dataMap);
        }

        /**
         * Returns a string representation of this CifMap; the data block name
         * stored in the underlying map is used, if present
         *
         * @return the data block name from the underlying map if present,
         *         otherwise an empty string
         */
        public final String toString() {
            return name;
        }
    }

    /*
     * a ListCellRenderer subclass used specifically for the GUI data block
     * list.  Its getListCellRendererComponent method sets the foreground
     * color of the component it returns based on the state of the item it
     * is to render.
     */
    static class CIFBlockListCellRenderer extends DefaultListCellRenderer {

        /** The Color used to render a CifMap value that contains errors */
        final Color ERROR_COLOR = Color.RED.darker();

        /** The Color used to render a CifMap value that has been stored */
        final Color STORED_COLOR = new Color(191, 191, 191);

        /** Constructs a new CifBlockListCellRenderer */
        CIFBlockListCellRenderer() {
            super();
        }

        /**
         * Returns the Component that should be used to render cells for the
         * specified value of the specified list under the specified conditions
         *
         * @param  list the JList for which a cell is to be rendered
         * @param  value the value object represented by the cell to render
         * @param  index the index in the list of the cell to be rendered
         * @param  isSelected {@code true} if the cell to be rendered is
         *         currently selected
         * @param  cellHasFocus {@code true} if the cell to be rendered
         *         currently has the input focus
         */
        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            Component renderer = super.getListCellRendererComponent(list, value,
                    index, isSelected, cellHasFocus);

            if ( value instanceof CifMap ) {
                CifMap mapVal = (CifMap) value;

                if (mapVal.isStored()) {
                    renderer.setForeground( STORED_COLOR );
                } else if ( mapVal.hasErrors() ) {
                    renderer.setForeground( ERROR_COLOR );
                }
            }

            return renderer;
        }

    }

    /**
     * A DocumentFilter that limits the length of the documents it is attached
     * to to a fixed (per filter instance) maximum value
     */
    static class ContentLengthDocumentFilter extends DocumentFilter {

        /** The maximum document length */
        private int maxLength;

        /**
         * Constructs a new ContentLengthDocumentFilter with the specified
         * document length limit
         *
         * @param  limit the maximum length permitted
         *
         * @throws IllegalArgumentException if limit is nonpositive
         */
        ContentLengthDocumentFilter(int limit) {
            if (limit < 1) {
                throw new IllegalArgumentException(
                    "Document length limit must be positive");
            } else {
                maxLength = limit;
            }
        }

        /**
         * Invoked prior to inserting the specified text into a document to
         * which this filter is attached; this version rejects insertions that
         * would make the document longer than the maximum length specified by
         * this filter
         *
         * @param  fb a DocumentFilter.FilterBypass by which the underlying
         *         document may be accessed and/or modified
         * @param  offs the offset of the insertion
         * @param  str a String containing the text to be inserted
         * @param  a the AttributeSet, if any, that applies to the inserted
         *         text; may be null if there are no attributes
         *
         * @throws BadLocationException if {@code offs} is not a valid
         *         location in the Document
         */
        public void insertString(FilterBypass fb, int offs, String str,
                                 AttributeSet a) throws BadLocationException {
            if ((fb.getDocument().getLength() + str.length()) <= maxLength) {
                super.insertString(fb, offs, str, a);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }

        /**
         * Invoked prior to replacing part of the text of a document to which
         * this filter is attached; this version rejects replacements that
         * would make the document longer than the maximum length specified by
         * this filter
         *
         * @param  fb a DocumentFilter.FilterBypass by which the underlying
         *         document may be accessed and/or modified
         * @param  offs the offset of the insertion
         * @param  length the length of the of the text to be replaced
         * @param  str a String containing the text to be inserted
         * @param  a the AttributeSet, if any, that applies to the inserted
         *         text; may be null if there are no attributes
         *
         * @throws BadLocationException if {@code offs} is not a valid
         *         location in the Document
         */
        public void replace(FilterBypass fb, int offs, int length, String str,
                                 AttributeSet a) throws BadLocationException {
            if ((fb.getDocument().getLength() + str.length() - length)
                 <= maxLength) {
                super.replace(fb, offs, length, str, a);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    /**
     * a NumberFormatter that assigns null values to null, blank, and empty text
     * strings and assigns empty text strings to null values
     */
    static class AcceptsBlankNumberFormatter extends NumberFormatter {

        /**
         * Constructs a new AcceptsBlankNumberFormatter with a default
         * NumberFormat
         */
        public AcceptsBlankNumberFormatter() {
            super();
        }

        /**
         * Constructs a new AcceptsBlankNumberFormatter with the specfied
         * NumberFormat
         *
         * @param  f the NumberFormat that this NumberFormatter should use to
         *         determine legal values
         */
        public AcceptsBlankNumberFormatter(NumberFormat f) {
            super(f);
        }

        /**
         * Overrides javax.swing.text.InternationalFormatter.valueToString so as
         * to return an empty string if the value is {@code null};
         * otherwise behaves as does InternationalFormatter's method
         *
         * @param  value the value to convert
         *
         * @throws ParseException if there is an error in the conversion
         */
        public String valueToString(Object value) throws ParseException {
            if (value == null) {
                return "";
            } else {
                return super.valueToString(value);
            }
        }

        /**
         * Overrides javax.swing.text.InternationalFormatter.stringToValue so as
         * to return {@code null} if the supplied string is null, blank,
         * or empty; otherwise behaves as does InternationalFormatter's method
         *
         * @param  text the string to convert
         *
         * @throws ParseException if there is an error in the conversion
         */
        public Object stringToValue(String text) throws ParseException {
            if (text == null || text.trim().equals("")) {
                return null;
            } else {
                return super.stringToValue(text);
            }
        }
    }

    /**
     * a JPanel subclass that manages the sample data form.  It lays out the
     * form elements as its children, can populate them from a SampleInfo or
     * from a CIF data block map, and can read them out to populate a SampleInfo
     */
    static class SampleForm extends JPanel {

        private final static char ALPHA = '\u03b1'; // Unicode Greek lower alpha
        private final static char BETA =  '\u03b2'; // Unicode Greek lower beta
        private final static char GAMMA = '\u03b3'; // Unicode Greek lower gamma
        private final static char DEGREE = '\u00b0';   // Unicode degree symbol
        private final static char CELSIUS = '\u2103';  // Unicode degree celsius symbol
        private final static char ANGSTROM = '\u00c5'; // preferred Unicode Angstrom symbol
        private final static char SUP2 = '\u00b2';     // Unicode superscript '2'
        private final static char SUP3 = '\u00b3';     // Unicode superscript '3'

        JComboBox labBox;
        JComboBox providerBox;
        private JTextField localIdField;
        private JFormattedTextField cellAField;
        private JFormattedTextField cellBField;
        private JFormattedTextField cellCField;
        private JFormattedTextField cellAlphaField;
        private JFormattedTextField cellBetaField;
        private JFormattedTextField cellGammaField;
        private JTextField spaceGroupField;
        private JFormattedTextField dcalcField;
        private JTextField colorField;
        private JFormattedTextField zField;
        private JFormattedTextField temperatureField;
        private JFormattedTextField volumeField;
        private JFormattedTextField rfField;
        private JFormattedTextField rwfField;
        private JFormattedTextField rf2Field;
        private JFormattedTextField rwf2Field;
        private JFormattedTextField goofField;
        private JTextField summaryField;
        private JTextField empiricalFormulaField;
        private JTextField structFormulaField;
        private JTextField moietyFormulaField;
        private JTextField commonNameField;
        private JTextField systematicNameField;
        private JTextField xgrapherNameField;
        private JTextField providerNameField;

        /**
         * constructs a SampleForm with all field blank
         */
        SampleForm() {
            this(null, null);
        }

        /**
         * constructs a SampleForm, populating as many fields as possible with
         * the data from a CIF block map
         *
         * @param  cifMap a map from CIF data names to their associated values
         */
        SampleForm(Map cifMap) {
            this(cifMap, null);
        }

        /**
         * constructs a SampleForm, populating as many fields as possible from
         * a SampleInfo object and a CIF block map.  Where a datum is specified
         * in both the SampleInfo and the map, the one from the map is used.
         *
         * @param  cifMap a {@code Map} from CIF data names to their
         *         associated values
         * @param  info a {@code SampleInfo} containing data with which
         *         to populate the form fields
         */
        SampleForm(Map cifMap, SampleInfo info) {
            super();
            GridBagLayout layout = new GridBagLayout();
            GridBagConstraints gbc;
            GridBagConstraints gbcLast;
            GridBagConstraints gbcFill;
            GridBagConstraints gbcLabel;
            GridBagConstraints gbcTemp;
            Color catColor = new Color(95, 95, 95);
            Box innerBox;
            JLabel label;
            FontMetrics fm;
            Dimension dim;
            NumberFormatter doubleFormatter;
            NumberFormatter intFormatter;
            DecimalFormat nf;
            DocumentFilter filter128 = new ContentLengthDocumentFilter(128);

            setLayout(layout);

            nf = new DecimalFormat();
            nf.setMinimumIntegerDigits(1);
            nf.setMinimumFractionDigits(0);
            nf.setMaximumFractionDigits(20);
            nf.setDecimalSeparatorAlwaysShown(false);
            nf.setGroupingUsed(false);
            doubleFormatter = new AcceptsBlankNumberFormatter(nf);

            nf = new DecimalFormat();
            nf.setParseIntegerOnly(true);
            nf.setMinimumIntegerDigits(1);
            nf.setGroupingUsed(false);
            intFormatter = new AcceptsBlankNumberFormatter(nf);

            /* create form elements */
            labBox = new JComboBox();
            for (LabInfo lab : localLabList) {
                labBox.addItem(lab);
            }
            labBox.setEditable(false);
            labBox.setSelectedIndex(-1);
            labBox.addItemListener(
                new ItemListener() {
                    public void itemStateChanged(ItemEvent e) {
                        if (e.getStateChange() == ItemEvent.SELECTED) {
                            List<ProviderInfo> providerList =
                                    labProviderMap.get(e.getItem());

                            if (providerList != null) {
								for (ProviderInfo provider : providerList) {
                                    providerBox.addItem(provider);
                                }
                            }
                        } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                            providerBox.setSelectedIndex(-1);
                            providerBox.removeAllItems();
                        }
                    }
                }
            );

            providerBox = new JComboBox();
            providerBox.setEditable(false);

            localIdField = new JTextField(32);
            fm = localIdField.getFontMetrics(localIdField.getFont());
            dim = localIdField.getPreferredSize();
            dim.setSize(fm.stringWidth("Mi") * 16 + 6, dim.getHeight());
            localIdField.setMinimumSize(dim);
            ((AbstractDocument) localIdField.getDocument()).setDocumentFilter(
                    new ContentLengthDocumentFilter(32));

            cellAField = new JFormattedTextField(doubleFormatter);
            cellAField.setColumns(10);
            cellAField.setFocusLostBehavior(
                    JFormattedTextField.COMMIT_OR_REVERT);
            dim = (Dimension) dim.clone();
            dim.setSize(fm.stringWidth("88.8888") + 6, dim.getHeight());
            cellAField.setMinimumSize(dim);
            cellBField = new JFormattedTextField(doubleFormatter);
            cellBField.setColumns(10);
            cellBField.setFocusLostBehavior(
                    JFormattedTextField.COMMIT_OR_REVERT);
            cellBField.setMinimumSize(dim);
            cellCField = new JFormattedTextField(doubleFormatter);
            cellCField.setColumns(10);
            cellCField.setFocusLostBehavior(
                    JFormattedTextField.COMMIT_OR_REVERT);
            cellCField.setMinimumSize(dim);
            cellAlphaField = new JFormattedTextField(doubleFormatter);
            cellAlphaField.setColumns(10);
            cellAlphaField.setFocusLostBehavior(
                    JFormattedTextField.COMMIT_OR_REVERT);
            cellAlphaField.setMinimumSize(dim);
            cellBetaField = new JFormattedTextField(doubleFormatter);
            cellBetaField.setColumns(10);
            cellBetaField.setFocusLostBehavior(
                    JFormattedTextField.COMMIT_OR_REVERT);
            cellBetaField.setMinimumSize(dim);
            cellGammaField = new JFormattedTextField(doubleFormatter);
            cellGammaField.setColumns(10);
            cellGammaField.setFocusLostBehavior(
                    JFormattedTextField.COMMIT_OR_REVERT);
            cellGammaField.setMinimumSize(dim);
            dcalcField = new JFormattedTextField(doubleFormatter);
            dcalcField.setColumns(10);
            dcalcField.setFocusLostBehavior(
                    JFormattedTextField.COMMIT_OR_REVERT);
            dcalcField.setMinimumSize(dim);
            temperatureField = new JFormattedTextField(doubleFormatter);
            temperatureField.setColumns(10);
            temperatureField.setFocusLostBehavior(
                    JFormattedTextField.COMMIT_OR_REVERT);
            temperatureField.setMinimumSize(dim);
            volumeField = new JFormattedTextField(doubleFormatter);
            volumeField.setColumns(10);
            volumeField.setFocusLostBehavior(
                    JFormattedTextField.COMMIT_OR_REVERT);
            volumeField.setMinimumSize(dim);
            rfField = new JFormattedTextField(doubleFormatter);
            rfField.setColumns(10);
            rfField.setFocusLostBehavior(
                    JFormattedTextField.COMMIT_OR_REVERT);
            rfField.setMinimumSize(dim);
            rwfField = new JFormattedTextField(doubleFormatter);
            rwfField.setColumns(10);
            rwfField.setFocusLostBehavior(
                    JFormattedTextField.COMMIT_OR_REVERT);
            rwfField.setMinimumSize(dim);
            rf2Field = new JFormattedTextField(doubleFormatter);
            rf2Field.setColumns(10);
            rf2Field.setFocusLostBehavior(
                    JFormattedTextField.COMMIT_OR_REVERT);
            rf2Field.setMinimumSize(dim);
            rwf2Field = new JFormattedTextField(doubleFormatter);
            rwf2Field.setColumns(10);
            rwf2Field.setFocusLostBehavior(
                    JFormattedTextField.COMMIT_OR_REVERT);
            rwf2Field.setMinimumSize(dim);
            goofField = new JFormattedTextField(doubleFormatter);
            goofField.setColumns(10);
            goofField.setFocusLostBehavior(
                    JFormattedTextField.COMMIT_OR_REVERT);
            goofField.setMinimumSize(dim);
            spaceGroupField = new JTextField(13);
            dim = (Dimension) dim.clone();
            dim.setSize(fm.stringWidth("P 1") * 5 + 6, dim.getHeight());
            spaceGroupField.setMinimumSize(dim);
            ((AbstractDocument)spaceGroupField.getDocument()).setDocumentFilter(
                    new ContentLengthDocumentFilter(13));

            colorField = new JTextField(20);
            dim = (Dimension) dim.clone();
            dim.setSize(fm.stringWidth("red") * 5 + 6, dim.getHeight());
            colorField.setMinimumSize(dim);
            ((AbstractDocument) colorField.getDocument()).setDocumentFilter(
                    new ContentLengthDocumentFilter(20));

            zField = new JFormattedTextField(intFormatter);
            zField.setColumns(5);
            zField.setFocusLostBehavior(
                    JFormattedTextField.COMMIT_OR_REVERT);
            dim = (Dimension) dim.clone();
            dim.setSize(fm.stringWidth("88888") + 6, dim.getHeight());
            zField.setMinimumSize(dim);

            summaryField = new JTextField(80);
            ((AbstractDocument) summaryField.getDocument()).setDocumentFilter(
                    new ContentLengthDocumentFilter(80));

            empiricalFormulaField = new JTextField(80);
            ((AbstractDocument) empiricalFormulaField.getDocument()).setDocumentFilter(
                    filter128);

            structFormulaField = new JTextField(80);
            ((AbstractDocument) structFormulaField.getDocument()).setDocumentFilter(
                    filter128);

            moietyFormulaField = new JTextField(80);
            ((AbstractDocument) moietyFormulaField.getDocument()).setDocumentFilter(
                    filter128);

            commonNameField = new JTextField(80);
            ((AbstractDocument) commonNameField.getDocument()).setDocumentFilter(
                    filter128);

            systematicNameField = new JTextField(80);

            xgrapherNameField = new JTextField(80);
            ((AbstractDocument) xgrapherNameField.getDocument()).setDocumentFilter(
                    filter128);

            providerNameField = new JTextField(80);
            ((AbstractDocument) providerNameField.getDocument()).setDocumentFilter(
                    filter128);



            /* Lay out the form */

            gbc = new GridBagConstraints();
            gbc.weighty = 1d;
            gbc.anchor = GridBagConstraints.EAST;

            gbcLast = (GridBagConstraints) gbc.clone();
            gbcLast.gridwidth = GridBagConstraints.REMAINDER;
            gbcLast.anchor = GridBagConstraints.WEST;
            gbcLast.weightx = 1d;

            gbcFill = (GridBagConstraints) gbcLast.clone();
            gbcFill.fill = GridBagConstraints.HORIZONTAL;

            gbcLabel = (GridBagConstraints) gbc.clone();
            gbcLabel.insets = new Insets(0,12,0,4);

            label = new JLabel("Identification and Attribution");
            layout.setConstraints(label, gbcLast);
            label.setForeground(catColor);
            add(label);

            label = new JLabel("Lab");
            label.setLabelFor(labBox);
            layout.setConstraints(label, gbcLabel);
            add(label);
            gbcTemp = (GridBagConstraints) gbc.clone();
            gbcTemp.anchor = GridBagConstraints.WEST;
            layout.setConstraints(labBox, gbcTemp);
            add(labBox);
            label = new JLabel("Local Sample ID");
            label.setLabelFor(localIdField);
            layout.setConstraints(label, gbcLabel);
            add(label);
            layout.setConstraints(localIdField, gbcLast);
            add(localIdField);

            label = new JLabel("Provider");
            label.setLabelFor(providerBox);
            layout.setConstraints(label, gbcLabel);
            add(label);
            gbcTemp = (GridBagConstraints) gbc.clone();
            gbcTemp.anchor = GridBagConstraints.WEST;
            layout.setConstraints(providerBox, gbcTemp);
            add(providerBox);
            label = new JLabel("Contact");
            label.setLabelFor(providerNameField);
            layout.setConstraints(label, gbcLabel);
            add(label);
            layout.setConstraints(providerNameField, gbcFill);
            add(providerNameField);

            label = new JLabel("Crystallographer");
            label.setLabelFor(xgrapherNameField);
            layout.setConstraints(label, gbcLabel);
            add(label);
            layout.setConstraints(xgrapherNameField, gbcFill);
            add(xgrapherNameField);

            label = new JLabel("Names and Formulae");
            layout.setConstraints(label, gbcLast);
            label.setForeground(catColor);
            add(label);

            label = new JLabel("Common Name");
            label.setLabelFor(commonNameField);
            layout.setConstraints(label, gbcLabel);
            add(label);
            layout.setConstraints(commonNameField, gbcFill);
            add(commonNameField);

            label = new JLabel("IUPAC Name");
            label.setLabelFor(systematicNameField);
            layout.setConstraints(label, gbcLabel);
            add(label);
            layout.setConstraints(systematicNameField, gbcFill);
            add(systematicNameField);

            label = new JLabel("Empirical Formula");
            label.setLabelFor(empiricalFormulaField);
            layout.setConstraints(label, gbcLabel);
            add(label);
            layout.setConstraints(empiricalFormulaField, gbcFill);
            add(empiricalFormulaField);

            label = new JLabel("Structural Formula");
            label.setLabelFor(structFormulaField);
            layout.setConstraints(label, gbcLabel);
            add(label);
            layout.setConstraints(structFormulaField, gbcFill);
            add(structFormulaField);

            label = new JLabel("Moiety Formula");
            label.setLabelFor(moietyFormulaField);
            layout.setConstraints(label, gbcLabel);
            add(label);
            layout.setConstraints(moietyFormulaField, gbcFill);
            add(moietyFormulaField);

            innerBox = new Box(BoxLayout.X_AXIS);
            label = new JLabel("Unit Cell Constants at ");
            label.setForeground(catColor);
            innerBox.add(label);
            innerBox.add(temperatureField);
            label = new JLabel("" + CELSIUS + "");
            innerBox.add(label);
            layout.setConstraints(innerBox, gbcLast);
            add(innerBox);

            String angStr = "" + ANGSTROM;
            String degStr = "" + DEGREE;
            innerBox = new Box(BoxLayout.X_AXIS);
            label = new JLabel("a");
            label.setLabelFor(cellAField);
            label.setBorder(new EmptyBorder(0, 12, 0, 4));
            innerBox.add(label);
            innerBox.add(cellAField);
            innerBox.add(new JLabel(angStr));
            label = new JLabel("b");
            label.setLabelFor(cellBField);
            label.setBorder(new EmptyBorder(0, 12, 0, 4));
            innerBox.add(label);
            innerBox.add(cellBField);
            innerBox.add(new JLabel(angStr));
            label = new JLabel("c");
            label.setLabelFor(cellCField);
            label.setBorder(new EmptyBorder(0, 12, 0, 4));
            innerBox.add(label);
            innerBox.add(cellCField);
            innerBox.add(new JLabel(angStr));
            label = new JLabel("" + ALPHA);
            label.setLabelFor(cellAlphaField);
            label.setBorder(new EmptyBorder(0, 12, 0, 4));
            innerBox.add(label);
            innerBox.add(cellAlphaField);
            innerBox.add(new JLabel(degStr));
            label = new JLabel("" + BETA);
            label.setLabelFor(cellBetaField);
            label.setBorder(new EmptyBorder(0, 12, 0, 4));
            innerBox.add(label);
            innerBox.add(cellBetaField);
            innerBox.add(new JLabel(degStr));
            label = new JLabel("" + GAMMA);
            label.setLabelFor(cellGammaField);
            label.setBorder(new EmptyBorder(0, 12, 0, 4));
            innerBox.add(label);
            innerBox.add(cellGammaField);
            innerBox.add(new JLabel(degStr));
            label = new JLabel("Volume");
            label.setLabelFor(volumeField);
            label.setBorder(new EmptyBorder(0, 12, 0, 4));
            innerBox.add(label);
            innerBox.add(volumeField);
            innerBox.add(new JLabel(angStr + SUP3));
            layout.setConstraints(innerBox, gbcLast);
            add(innerBox);

            label = new JLabel("Other Crystal Data");
            layout.setConstraints(label, gbcLast);
            label.setForeground(catColor);
            add(label);

            innerBox = new Box(BoxLayout.X_AXIS);
            label = new JLabel("Space Group");
            label.setBorder(new EmptyBorder(0, 12, 0, 4));
            label.setLabelFor(spaceGroupField);
            innerBox.add(label);
            innerBox.add(spaceGroupField);
            label = new JLabel("Z");
            label.setBorder(new EmptyBorder(0, 12, 0, 4));
            label.setLabelFor(zField);
            innerBox.add(label);
            innerBox.add(zField);
            label = new JLabel("d(calc)");
            label.setBorder(new EmptyBorder(0, 12, 0, 4));
            label.setLabelFor(dcalcField);
            innerBox.add(label);
            innerBox.add(dcalcField);
            innerBox.add(new JLabel("g/cm" + SUP3));
            label = new JLabel("Color");
            label.setBorder(new EmptyBorder(0, 12, 0, 4));
            label.setLabelFor(colorField);
            innerBox.add(label);
            innerBox.add(colorField);
            layout.setConstraints(innerBox, gbcLast);
            add(innerBox);

            label = new JLabel("Agreement Indices ");
            layout.setConstraints(label, gbcLast);
            label.setForeground(catColor);
            add(label);

            innerBox = new Box(BoxLayout.X_AXIS);
            label = new JLabel("R(F)");
            label.setBorder(new EmptyBorder(0, 12, 0, 4));
            label.setLabelFor(rfField);
            innerBox.add(label);
            innerBox.add(rfField);
            label = new JLabel("Rw(F)");
            label.setBorder(new EmptyBorder(0, 12, 0, 4));
            label.setLabelFor(rwfField);
            innerBox.add(label);
            innerBox.add(rwfField);
            label = new JLabel("R(F" + SUP2 + ")");
            label.setBorder(new EmptyBorder(0, 12, 0, 4));
            label.setLabelFor(rf2Field);
            innerBox.add(label);
            innerBox.add(rf2Field);
            label = new JLabel("Rw(F" + SUP2 + ")");
            label.setBorder(new EmptyBorder(0, 12, 0, 4));
            label.setLabelFor(rwf2Field);
            innerBox.add(label);
            innerBox.add(rwf2Field);
            label = new JLabel("GooF");
            label.setBorder(new EmptyBorder(0, 12, 0, 4));
            label.setLabelFor(goofField);
            innerBox.add(label);
            innerBox.add(goofField);
            layout.setConstraints(innerBox, gbcLast);
            add(innerBox);

            clear();
            update(info);
            update(cifMap);
        }

        /**
         * clears the form
         */
        synchronized void clear() {
            if (labBox.getItemCount() > 0 && labBox.getSelectedIndex() == -1) {
                labBox.setSelectedIndex(0); // will initialize providerBox
            }
            providerBox.setSelectedIndex(-1);
            localIdField.setText("");
            cellAField.setValue(null);
            cellBField.setValue(null);
            cellCField.setValue(null);
            cellAlphaField.setValue(null);
            cellBetaField.setValue(null);
            cellGammaField.setValue(null);
            spaceGroupField.setText("");
            dcalcField.setValue(null);
            colorField.setText("");
            zField.setValue(null);
            temperatureField.setValue(null);
            volumeField.setValue(null);
            rfField.setValue(null);
            rwfField.setValue(null);
            rf2Field.setValue(null);
            rwf2Field.setValue(null);
            goofField.setValue(null);
            summaryField.setText("");
            empiricalFormulaField.setText("");
            structFormulaField.setText("");
            moietyFormulaField.setText("");
            commonNameField.setText("");
            systematicNameField.setText("");
            // xgrapherNameField not cleared: should carry over
            providerNameField.setText("");
        }

        /**
         * populates the form with values from the supplied
         * {@code SampleInfo} object; not implemented in this version
         *
         * @param  info a {@code SampleInfo} containing values with which
         *         to populate the form
         */
        synchronized void update(SampleInfo info) {
            /* not implemented */
        }

        private String nullToBlank(Object in) {
            return (in == null) ? "" : in.toString();
        }

        private Double cifToDouble(Object in) {
            if (in != null) {
                String inStr = in.toString();
                int index = inStr.indexOf('(');
                try {
                    if (index > 0) {
                        return Double.valueOf(inStr.substring(0, index));
                    } else if (index < 0) {
                        return Double.valueOf(inStr);
                    }
                } catch (NumberFormatException nfe) {
                    /* do nothing */
                }
            }
            return null;
        }

        private Long cifToLong(Object in) {
            if (in != null) {
                String inStr = in.toString();
                int index = inStr.indexOf('(');
                try {
                    if (index > 0) {
                        return Long.valueOf(inStr.substring(0, index));
                    } else if (index < 0) {
                        return Long.valueOf(inStr);
                    }
                } catch (NumberFormatException nfe) {
                    /* do nothing */
                }
            }
            return null;
        }

        /**
         * populates the form with values from the supplied {@code Map}
         *
         * @param  cifMap a {@code Map} from CIF data names to their
         *         associated values
         */
        synchronized void update(Map cifMap) {
            String s;
            if (cifMap == null) {
                return;
            }
            localIdField.setText(
                    nullToBlank(cifMap.get(CifParser.BLOCK_NAME_KEY)));
            cellAField.setValue(
                    cifToDouble(cifMap.get("_cell_length_a")));
            cellBField.setValue(
                    cifToDouble(cifMap.get("_cell_length_b")));
            cellCField.setValue(
                    cifToDouble(cifMap.get("_cell_length_c")));
            cellAlphaField.setValue(
                    cifToDouble(cifMap.get("_cell_angle_alpha")));
            cellBetaField.setValue(
                    cifToDouble(cifMap.get("_cell_angle_beta")));
            cellGammaField.setValue(
                    cifToDouble(cifMap.get("_cell_angle_gamma")));
            spaceGroupField.setText(
                    nullToBlank(cifMap.get("_symmetry_space_group_name_h-m")));
            dcalcField.setValue(
                    cifToDouble(cifMap.get("_exptl_crystal_density_diffrn")));
            colorField.setText(
                    nullToBlank(cifMap.get("_exptl_crystal_colour")));
            zField.setValue(
                    cifToLong(cifMap.get("_cell_formula_units_z")));
            try {
                double d = cifToDouble(cifMap.get(
                                "_diffrn_ambient_temperature")).doubleValue();
                temperatureField.setValue(new Double(d - 273d));
            } catch (NumberFormatException nfe) {
                // do nothing -- field will be empty
            } catch (NullPointerException npe) {
                // do nothing -- field will be empty
            }
            try {
                double d = cifToDouble(cifMap.get(
                                "_diffrn_ambient_temperature")).doubleValue();
                temperatureField.setValue(new Double(d - 273d));
            } catch (NumberFormatException nfe) {
                // do nothing -- field will be empty
            } catch (NullPointerException npe) {
                // do nothing -- field will be empty
            }
            volumeField.setValue(
                    cifToDouble(cifMap.get("_cell_volume")));
            if (cifMap.containsKey("_refine_ls_r_factor_gt")) {
                rfField.setValue(
                        cifToDouble(cifMap.get("_refine_ls_r_factor_gt")));
            } else {
                rfField.setValue(
                        cifToDouble(cifMap.get("_refine_ls_r_factor_obs")));
            }
            // rwfField not set (not available in a SHELX CIF)
            rf2Field.setValue(
                    cifToDouble(cifMap.get("_refine_ls_r_factor_all")));
            if (cifMap.containsKey("_refine_ls_wr_factor_ref")) {
                rwf2Field.setValue(
                        cifToDouble(cifMap.get("_refine_ls_wr_factor_ref")));
            } else {
                rwf2Field.setValue(
                        cifToDouble(cifMap.get("_refine_ls_wr_factor_all")));
            }
            if (cifMap.containsKey("_refine_ls_goodness_of_fit_ref")) {
                goofField.setValue(
                    cifToDouble(cifMap.get("_refine_ls_goodness_of_fit_ref")));
            } else {
                goofField.setValue(
                    cifToDouble(cifMap.get("_refine_ls_goodness_of_fit_all")));
            }
            // summaryField not set (no good analogue in CIF)
            empiricalFormulaField.setText(
                    nullToBlank(cifMap.get("_chemical_formula_sum")));
            structFormulaField.setText(
                    nullToBlank(cifMap.get("_chemical_formula_structural")));
            moietyFormulaField.setText(
                    nullToBlank(cifMap.get("_chemical_formula_moiety")));
            commonNameField.setText(
                    nullToBlank(cifMap.get("_chemical_name_common")));

            /*
             * SHELXL puts a text block containing only whitespace and a '?'
             * character into its CIFs as the value for
             * _chemical_name_systematic.  That is an error (SHELXL should
             * instead insert a single, bare '?' character), but users are
             * likely to run into it a lot so we patch it up here.
             */
            s = nullToBlank(cifMap.get("_chemical_name_systematic"));
            systematicNameField.setText(s.trim().equals("?") ? "" : s);
            // xgrapherNameField not set (cannot deduce from generic CIF)
            // providerNameField not set (cannot deduce from generic CIF)
        }

        /**
         * checks whether enough data has been provided to submit the form;
         * returns an array of diagnostic messages, which will be of length
         * zero if the form is okay for submission
         *
         * @return a {@code String[]} of diagnostic messages, possibly
         *         of length zero
         */
        synchronized String[] validateForm() {
            List<String> messageList = new ArrayList<String>();
            String localId = localIdField.getText();

            if (labBox.getSelectedIndex() < 0) {
                messageList.add("A laboratory must be selected.");
            }
            if (localId == null || localId.trim().equals("")) {
                messageList.add("A non-blank local id must be specified.");
            }
            if (providerBox.getSelectedIndex() < 0) {
                messageList.add("A provider must be selected.");
            }
            
            return messageList.toArray(new String[messageList.size()]);
        }

        /**
         * commits the JFormattedTextField's current edit and extracts the
         * double value
         */
        double extractDouble(JFormattedTextField field) {
            try {
                field.commitEdit();
                Number num = (Number) field.getValue();
                if (num == null) {
                    return SampleDataInfo.INVALID_DOUBLE_VALUE;
                } else {
                    return num.doubleValue();
                }
            } catch (ParseException pe) {
                // this shouldn't happen, but is handled gracefully if it does
                return SampleDataInfo.INVALID_DOUBLE_VALUE;
            }

            /*
             * a ClassCastException could conceivably be thrown, but that
             * shouldn't happen.  If it ever does then something is horribly
             * wrong, and the application will (by design) crash with a stack
             * trace.
             */
        }

        /**
         * commits the JFormattedTextField's current edit and extracts the
         * int value
         */
        int extractInt(JFormattedTextField field) {
            try {
                field.commitEdit();
                Number num = (Number) field.getValue();
                if (num == null) {
                    return SampleDataInfo.INVALID_INT_VALUE;
                } else {
                    return num.intValue();
                }
            } catch (ParseException pe) {
                // this shouldn't happen, but is handled gracefully if it does
                return SampleDataInfo.INVALID_INT_VALUE;
            }

            /*
             * a ClassCastException could conceivably be thrown, but that
             * shouldn't happen.  If it ever does then something is horribly
             * wrong, and the application will (by design) crash with a stack
             * trace.
             */
        }

        /**
         * returns the trimmed value of a {@code String} if that is
         * non-empty, otherwise {@code null}
         */
        String blankToNull(String in) {
            String t = in.trim();
            return (t.length() == 0) ? null : t;
        }

        /**
         * Populates the specified {@code SampleInfo} with the sample
         * submission information from the form and sets the status to
         * PENDING
         *
         * @param  info the {@code SampleInfo} in which to store the
         *         submission data
         */
        synchronized void populateSubmissionInfo(SampleInfo info) {
            info.labId = ((LabInfo) labBox.getSelectedItem()).id;
            info.dataInfo.providerId =
                    ((ProviderInfo) providerBox.getSelectedItem()).id;
            info.localLabId = localIdField.getText().trim();
            addSampleAttribute(info, SampleTextBL.SAMPLE_PROVIDER_NAME,
                               providerNameField);
            info.status = SampleWorkflowBL.PENDING_STATUS;
        }

        /**
         * Populates the specified {@code SampleInfo} with the data
         * collection information from the form and sets the status to
         * REFINEMENT_PENDING
         *
         * @param  info the {@code SampleInfo} in which to store the
         *         data collection information
         */
        synchronized void populateDataCollectionInfo(SampleInfo info) {
            info.dataInfo.a = extractDouble(cellAField);
            info.dataInfo.b = extractDouble(cellBField);
            info.dataInfo.c = extractDouble(cellCField);
            info.dataInfo.alpha = extractDouble(cellAlphaField);
            info.dataInfo.beta = extractDouble(cellBetaField);
            info.dataInfo.gamma = extractDouble(cellGammaField);
            info.dataInfo.v = extractDouble(volumeField);
            info.dataInfo.color = blankToNull(colorField.getText());
            info.dataInfo.t = extractDouble(temperatureField);
            addSampleAttribute(info, SampleTextBL.CRYSTALLOGRAPHER_NAME,
                               xgrapherNameField);
            info.status = SampleWorkflowBL.REFINEMENT_PENDING_STATUS;
        }

        /**
         * Populates the specified {@code SampleInfo} with the refinement
         * information from the form and sets the status to COMPLETE
         *
         * @param  info the {@code SampleInfo} in which to store the
         *         refinement information
         */
        synchronized void populateRefinementInfo(SampleInfo info) {
            info.dataInfo.spgp = blankToNull(spaceGroupField.getText());
            info.dataInfo.dcalc = extractDouble(dcalcField);
            info.dataInfo.z = extractInt(zField);
            info.dataInfo.rf = extractDouble(rfField);
            info.dataInfo.rwf = extractDouble(rwfField);
            info.dataInfo.rf2 = extractDouble(rf2Field);
            info.dataInfo.rwf2 = extractDouble(rwf2Field);
            info.dataInfo.goof = extractDouble(goofField);
            info.dataInfo.summary = blankToNull(summaryField.getText());
            addSampleAttribute(info, SampleTextBL.EMPIRICAL_FORMULA,
                               empiricalFormulaField);
            addSampleAttribute(info, SampleTextBL.STRUCTURAL_FORMULA,
                               structFormulaField);
            addSampleAttribute(info, SampleTextBL.MOIETY_FORMULA,
                               moietyFormulaField);
            addSampleAttribute(info, SampleTextBL.COMMON_NAME, commonNameField);
            addSampleAnnotation(info, SampleTextBL.IUPAC_NAME,
                                systematicNameField);
            info.status = SampleWorkflowBL.COMPLETE_STATUS;
        }

        /**
         * Adds an attribute of the specified type to the specified sample info
         * object, based on the text from the supplied widget; has no effect if
         * the text is blank
         *
         * @param  info the {@code SampleInfo} to which the attribute
         *         should be added
         * @param  attributeType the attribute type code for the attribute to
         *         add
         * @param  fromField the {@code JTextField} from which to draw the
         *         text for the attribute
         */
        synchronized void addSampleAttribute(SampleInfo info, int attributeType,
                                             JTextField fromField) {
            String text = blankToNull(fromField.getText());

            if (text != null) {
                info.attributeInfo.add(
                        new SampleAttributeInfo(attributeType, text));
            }
        }

        /**
         * Adds an annotation of the specified type to the specified sample info
         * object, based on the text from the supplied widget; has no effect if
         * the text is blank
         *
         * @param  info the {@code SampleInfo} to which the annotation
         *         should be added
         * @param  annotationType the annotation type code for the annotation to
         *         add
         * @param  fromField the {@code JTextField} from which to draw the
         *         text for the annotation
         */
        synchronized void addSampleAnnotation(SampleInfo info,
                int annotationType, JTextField fromField) {
            String text = blankToNull(fromField.getText());

            if (text != null) {
                info.annotationInfo.add(
                        new SampleAnnotationInfo(annotationType, text));
            }
        }
    }

    /**
     * constructs a {@code CifImporter} object that reads from the
     * standard input
     */
    private CifImporter() throws FileNotFoundException {
        this(null, null);
    }

    /**
     * constructs a CifImporter object that reads from the specified file
     *
     * @param  fn the name of the file to read from; null or "-" is interpreted
     *         as a request to read System.in
     * @throws FileNotFoundException if the file named by {@code fn}
     *         is not found
     */
    private CifImporter(String fn) throws FileNotFoundException {
        this(fn, null);
    }

    /**
     * constructs a CifImporter object that reads from the specified file
     *
     * @param  fn the name of the file to read from; null or "-" is interpreted
     *         as a request to read System.in
     * @param  out a PrintWriter to which to report status and error messages
     *
     * @throws FileNotFoundException if the file named by {@code fn}
     *         is not found
     */
    private CifImporter(String fn, PrintWriter out)
            throws FileNotFoundException {
        synchronized(this.getClass()) {
            /*
             * A character encoding must be specified for the input stream in
             * case the default encoding is something strange.  The CIF standard
             * is defined over ASCII; here we use ISO-8859-1 to read the CIF
             * input because it agrees with ASCII over ASCII's range, but can
             * handle all byte streams.  The CifParser will detect and reject
             * illegal characters internally.
             */
            try {
                if ("-".equals(fn) || fn == null) {
                    if (lockStdin) {
                        throw new FileNotFoundException("stdin already locked");
                    }
                    fileName = null;
                    cifIn = new CifParser(
                            new InputStreamReader(System.in, "ISO-8859-1"),
                            out);
                    lockStdin = true;
                } else {
                    fileName = fn;
                    cifIn = new CifParser(new InputStreamReader(
                            new FileInputStream(fileName), "ISO-8859-1"), out);
                }
            } catch (UnsupportedEncodingException uee) {

                /*
                 * All Java implementations must support ISO-8859-1, so if this
                 * ever happens then something is very wrong with the local Java
                 * installation
                 */
                throw new RuntimeException("Unexpected exception", uee);
            }
        }
    }

    /**
     * displays the GUI based on the provided {@code Map} of data blocks
     *
     * @param  blockMap a {@code Map} from data block names to CIF data
     *         maps corresponding to the named data block
     */
    private static void displayGUI(Map<String, Object> blockMap) {
        final JFrame mainFrame = new JFrame("Reciprocal Net CIF Importer");
        JPanel content = new JPanel(new BorderLayout());
        JPanel innerPanel = new JPanel(new BorderLayout());
        Box box;
        DefaultListModel blockListModel;
        final Map<String, Object> blocks = blockMap;
        final DefaultListModel errorListModel = new DefaultListModel();
        final JList blockList = new JList();
        JList errorList;
        JScrollPane jsp;
        final SampleForm form = new SampleForm();
        final JButton prevButton = new JButton("< previous");
        final JButton storeButton = new JButton("store");
        final JButton nextButton = new JButton("next >");
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");

        /* configure the frame */
        mainFrame.setContentPane(content);
        mainFrame.setJMenuBar(menuBar);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(true);
        mainFrame.setFocusTraversalKeysEnabled(true);

        /* configure the menu bar */
        exitItem.addActionListener(
            new ActionListener() {
                public void actionPerformed(
                        @SuppressWarnings("unused") ActionEvent ae) {
                    System.exit(0);
                }
            }
        );
        exitItem.setMnemonic(KeyEvent.VK_X);
        fileMenu.add(exitItem);
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);

        /* build the frame content */
        blockListModel = new DefaultListModel();
        if (blockMap != null) {
            Iterator it = blocks.values().iterator();
            while (it.hasNext()) {
                blockListModel.addElement(new CifMap((Map<String, Object>)it.next()));
            }
        }
        blockList.setModel(blockListModel);
        blockList.setAutoscrolls(true);
        blockList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        blockList.setCellRenderer(new CIFBlockListCellRenderer());
        blockList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                JList list = (JList) e.getSource();
                int index = list.getSelectedIndex();
                CifMap block = (CifMap) list.getSelectedValue();
                String blockName = block.toString();
                Map cifMap = block.dataMap;

                /* update the form for the new selection */
                form.clear();
                form.update(cifMap);

                /* enable or disable previous and next buttons as appropriate */
                if (index > -1) {
                    if (index == 0) {
                        prevButton.setEnabled(false);
                    } else {
                        prevButton.setEnabled(true);
                    }
                    storeButton.setEnabled(true);
                    if (index >= list.getModel().getSize() - 1) {
                        nextButton.setEnabled(false);
                    } else {
                        nextButton.setEnabled(true);
                    }

                } else {
                    prevButton.setEnabled(false);
                    storeButton.setEnabled(false);
                    nextButton.setEnabled(false);
                }

                /* update the error list */
                errorListModel.clear();
                if (cifMap != null) {
                    List eList = (List) cifMap.get(CifParser.ERRORS_KEY);
                    if (eList != null && eList.size() > 0) {
                        Iterator it = eList.iterator();
                        while (it.hasNext()) {
                            errorListModel.addElement(it.next());
                        }
                        JOptionPane.showMessageDialog(
                                mainFrame,
                                "Errors encountered in data block "
                                + blockName
                                + "\n(extracted data may still be usable)",
                                "Warning: CIF Errors",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        }); // end of blockList's ListSelectionListener
        if (blockListModel.getSize() > 0) {
            blockList.setSelectedIndex(0);
        } else {
            prevButton.setEnabled(false);
            storeButton.setEnabled(false);
            nextButton.setEnabled(false);
        }
        jsp = new JScrollPane(blockList,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jsp.setBorder(BorderFactory.createTitledBorder("Data Blocks"));
        innerPanel.add(jsp, BorderLayout.WEST);

        errorList = new JList(errorListModel);
        errorList.setEnabled(false);
        errorList.setBackground(content.getBackground());
        jsp = new JScrollPane(errorList,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jsp.setBorder(new TitledBorder("Messages"));
        innerPanel.add(jsp, BorderLayout.SOUTH);

        form.setBorder(new TitledBorder("Sample Data"));
        innerPanel.add(form, BorderLayout.CENTER);

        content.add(innerPanel, BorderLayout.CENTER);

        innerPanel = new JPanel(new GridLayout(1,3));
        prevButton.addActionListener(
            new ActionListener() {
                public void actionPerformed(
                        @SuppressWarnings("unused") ActionEvent e)  {
                    /* select previous item */
                    blockList.setSelectedIndex(
                            blockList.getSelectedIndex() - 1);
                    blockList.ensureIndexIsVisible(
                            blockList.getSelectedIndex());
                }
            }
        );
        innerPanel.add(prevButton);
        storeButton.addActionListener(
            new ActionListener() {
                String[] progressText = new String[] {
                    "no sample information stored",
                    "data collection and refinement information not stored",
                    "refinement information not stored"
                };

                public void actionPerformed(
                        @SuppressWarnings("unused") ActionEvent e)  {
                    /* store the item */
                    String[] messages = form.validateForm();
                    if (messages == null || messages.length == 0) {
                        int progress = 0;

                        try {
                            Object value = blockList.getSelectedValue();
                            SampleInfo info = sampleManager.getSampleInfo();

                            form.populateSubmissionInfo(info);
                            info = sampleManager.putSampleInfo(info,
                                    SampleWorkflowBL.SUBMITTED,
                                    UserInfo.INVALID_USER_ID,
                                    "Automated submission by CifImporter");
                            progress++;

                            form.populateDataCollectionInfo(info);
                            info = sampleManager.putSampleInfo(info,
                                    SampleWorkflowBL.PRELIMINARY_DATA_COLLECTED,
                                    UserInfo.INVALID_USER_ID,
                                    "Data collection information gleaned from "
                                    + "CIF by CifImporter");
                            progress++;

                            form.populateRefinementInfo(info);
                            info = sampleManager.putSampleInfo(info,
                                    SampleWorkflowBL.STRUCTURE_REFINED,
                                    UserInfo.INVALID_USER_ID,
                                    "Refinement information gleaned from "
                                    + "CIF by CifImporter");
                            progress++;

                            if (value instanceof CifMap) {
                                ((CifMap) value).setStored();
                                blockList.repaint();
                            }
                            JOptionPane.showMessageDialog(
                                    mainFrame,
                                    "Sample data successfully stored",
                                    "Success",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } catch (RemoteException re) {
                            JOptionPane.showMessageDialog(
                                    mainFrame,
                                    "Error while attempting to store sample "
                                    + "information ("
                                    + re
                                    + "); "
                                    + progressText[progress],
                                    "Storage Error",
                                    JOptionPane.ERROR_MESSAGE);
                        } catch (RecipnetException re) {
                            JOptionPane.showMessageDialog(
                                    mainFrame,
                                    "Error while attempting to store sample "
                                    + "information ("
                                    + re
                                    + "); "
                                    + progressText[progress],
                                    "Storage Error",
                                    JOptionPane.ERROR_MESSAGE);
                        } catch (RuntimeException re) {

                            /*
                             * This is a catchall so that the application
                             * handles unexpected exceptions gracefully
                             */
                            JOptionPane.showMessageDialog(
                                    mainFrame,
                                    "Error while attempting to store sample "
                                    + "information ("
                                    + re
                                    + "); "
                                    + progressText[progress],
                                    "Storage Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(
                                form,
                                messages,
                                "Validation Errors",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        );
        innerPanel.add(storeButton);
        nextButton.addActionListener(
            new ActionListener() {
                public void actionPerformed(
                        @SuppressWarnings("unused") ActionEvent e)  {
                    /* select next item */
                    blockList.setSelectedIndex(
                            blockList.getSelectedIndex() + 1);
                    blockList.ensureIndexIsVisible(
                            blockList.getSelectedIndex());
                }
            }
        );
        innerPanel.add(nextButton);
        box = new Box(BoxLayout.X_AXIS);
        box.add(Box.createHorizontalGlue());
        box.add(innerPanel);
        box.add(Box.createHorizontalGlue());
        content.add(box, BorderLayout.SOUTH);

        mainFrame.setJMenuBar(menuBar);

        /* display the frame */
        mainFrame.pack();
        mainFrame.setSize(850, 600);
        mainFrame.setVisible(true);
        mainFrame.toFront();
    }

    /**
     * establishes RMI connections to the three core components and obtains
     * intial laboratory and provider information
     *
     * @param  props a {@code Properties} containing RMI names for the
     *         Reciprocal Net core components, with property names as used
     *         in the recipnetd.conf configuration file
     */
    protected static void linkToCore(Properties props)
            throws NotBoundException, MalformedURLException, RemoteException,
            NameNotFoundException, OperationFailedException {
        String siteHostName;
        String siteManagerName;
        String sampleManagerName;
        String repositoryManagerName;
        String rmiPort;
        int[] labIds;

        siteHostName = props.getProperty("SitHostName", "localhost");
        rmiPort = props.getProperty("GenRmiPort", "1099");
        siteManagerName = props.getProperty("SitRmiName");
        if (siteManagerName == null) {
            throw new NameNotFoundException(
                    "Site Manager RMI name not configured");
        }
        sampleManagerName = props.getProperty("SamRmiName");
        if (sampleManagerName == null) {
            throw new NameNotFoundException(
                    "Sample Manager RMI name not configured");
        }
        repositoryManagerName = props.getProperty("RepRmiName");
        if (repositoryManagerName == null) {
            throw new NameNotFoundException(
                    "Repository Manager RMI name not configured");
        }
        siteManager = (SiteManagerRemote) Naming.lookup(
                "//" + siteHostName + ":" + rmiPort + "/" + siteManagerName);
        sampleManager = (SampleManagerRemote) Naming.lookup(
                "//" + siteHostName + ":" + rmiPort + "/" + sampleManagerName);
        repositoryManager = (RepositoryManagerRemote) Naming.lookup(
                "//" + siteHostName + ":" + rmiPort + "/"
                + repositoryManagerName);

        labIds = siteManager.getLocalLabs();
        localLabList = new ArrayList<LabInfo>(labIds.length);
        for (int i = 0; i < labIds.length; i++) {
            LabInfo labInfo = siteManager.getLabInfo(labIds[i]);
            localLabList.add(labInfo);
        }
        refreshProviders();
    }

    /**
     * creates or updates the internal mapping from LabInfo containiners to
     * Lists of associated ProviderInfo containers
     */
    protected static void refreshProviders() throws RemoteException,
            OperationFailedException {

        if (labProviderMap == null) {
            labProviderMap = new HashMap<LabInfo, List<ProviderInfo>>();
        }

        for (LabInfo lab : localLabList) {
            List<ProviderInfo> list = new ArrayList<ProviderInfo>(
            		Arrays.asList(siteManager.getAllProviderInfo(lab.id)));

            for (Iterator it = list.iterator(); it.hasNext(); ) {
            	ProviderInfo info = (ProviderInfo) it.next();

            	if (!info.isActive) {
            		it.remove();
            	}
            }
            
            labProviderMap.put(lab, list);
        }

    }

    /**
     * reads CIF data blocks one by one from the configured source until there
     * are no more; checks each one for content and error indicators and
     * updates the database; issues warnings to System.err instead of storing
     * the CIF data when appropriate. (For instance, invalid CIFs, insufficient
     * data, and SQL problems might all cause warnings to be issued.)
     */
    public void run() {
        readBlocks:
        while (true) {
            Map cifData = cifIn.getNextDataBlock();
            if (cifData == null) {
                break readBlocks;
            }
            String blockName = (String) cifData.get(CifParser.BLOCK_NAME_KEY);
            if ( blockMap.containsKey(blockName) ) {
                System.err.println(
                        "WARNING: Multiple data blocks encountered for "
                        + blockName + " --");
                System.err.println("         retaining the latest.");
            }
            blockMap.put(blockName, cifData);
            if (debug) {
                System.out.println(cifData);
                System.out.flush();
            }
        }
    }

    /**
     * Displays correct command-line syntax for this utility.
     */
    private static void displayUsage(int exitCode) {
        System.err.println();
        System.err.println("Reciprocal Net CIF Importer");
        System.err.println(
                "Copyright (c) 2002-2004, the Trustees of Indiana University");
        System.err.println();
        System.err.println("usage:");
        System.err.println(" recipnet-cifimporter [options] [files]");
        System.err.println();
        System.err.println("options:");
        System.err.println("    --configdir=<dirname>");
        System.err.println("        find the Reciprocal Net configuration"
                + " file in the specified directory");
        System.err.println("        (default: "
                           + ((DEFAULT_CONF_DIR == null) ? "<working directory>"
                                                         : DEFAULT_CONF_DIR)
                           + " )");
        System.err.println("    --configfile=<filename>");
        System.err.println("        read the Reciprocal Net configuration"
                + " from the specifed file");
        System.err.println("        (default: " + DEFAULT_CONF_NAME + " )");
        System.err.println("    --debug");
        System.err.println("        produce debugging output");
        System.err.println("    --parallel");
        System.err.println("        process multiple CIF files in parallel");
        System.err.println("    --serial");
        System.err.println(
                "        process CIF files sequentially (the default)");
        System.err.println("    --help");
        System.err.println("        display this message");
        System.err.println("    --");
        System.err.println(
                "        marks the end of the option list (optional)");
        System.err.println();
        System.err.println("files:");
        System.err.println("    The first non-option command line argument"
                + " and all further arguments are");
        System.err.println("    interpreted as specifying CIFs to import."
                + "  The arguments are generally");
        System.err.println("    treated as file names, but a single hyphen"
                + " may appear once in the list to");
        System.err.println("    indicate that the standard input should be"
                + " read.  If no files are");
        System.err.println("    specified then the standard input is read"
                + " (as if the file list consisted");
        System.err.println("    of a single hyphen).");
        System.err.println();
        System.exit(exitCode);
    }

    /**
     * the {@code CifImporter} application's main() method.  Options are
     * introduced by a leading "--", and must precede other arguments; "--" by
     * itself indicates the end of the option list.  Non-option arguments
     * are interpreted as file names to read, except that "-" is interpreted
     * as the standard input stream.  If no such arguments are given then the
     * standard input is read by default.  The underlying software will handle
     * multiple data blocks in any file or in stdin, and all such data blocks
     * will be added if possible.
     * <p>
     * There may be some advantage to specifying multiple file names on the
     * command line and using the "--parallel" option to make the imports run in
     * parallel (in seperate {@code CifImporter} instances), but the
     * default is to process input files serially.
     *
     * @param args a {@code String[]} containing the command-line
     *        arguments to the program, if any
     */
    public static void main(String[] args) {
        int i = 0;
        boolean parallel = false;
        String confDirName = DEFAULT_CONF_DIR;
        String confFileName = DEFAULT_CONF_NAME;
        File confFile;

        /* process options */
        while ((i < args.length) && args[i].startsWith("--")) {
            String arg = args[i++];
            String argName = null;

            if (arg.length() == 2) {
                break;
            }
            argName = arg.substring(2);
            if ("parallel".equals(argName)) {
                parallel = true;
            } else if ("serial".equals(argName)) {
                parallel = false;
            } else if (argName.startsWith("configdir=")) {
                confDirName = argName.substring("configdir=".length());
            } else if (argName.startsWith("configfile=")) {
                confFileName = argName.substring("configfile=".length());
            } else if ("debug".equals(argName)) {
                debug = true;
            } else {
                int exitCode;

                if ("help".equals(argName)) {
                    exitCode = 0;
                } else {
                    System.err.println();
                    System.err.println("Unrecognized option --" + argName);
                    exitCode = 1;
                }
                displayUsage(exitCode);  // Invokes System.exit(exitCode)
            }
        }

        confFile = new File(confDirName, confFileName);

        /* Make RMI connections to the core components */
        try {
            InputStream confStream = new FileInputStream(confFile);
            Properties props = new Properties();

            props.load(confStream);
            confStream.close();
            linkToCore(props);
        } catch (FileNotFoundException fnfe) {
            System.err.println("File " + confFile + " not found.");
            if (debug) {
                fnfe.printStackTrace(System.err);
            }
            System.exit(1);
        } catch (MalformedURLException mue) {
            System.err.println(
                    "Error in configuration file -- host name malformed");
            System.exit(1);
        } catch (NameNotFoundException nnfe) {
            System.err.println("Configuration file error -- "
                    + nnfe.getMessage() );
            System.exit(1);
        } catch (NotBoundException nbe) {
            System.err.println(
                "Incorrect core component name or name not registered");
            if (debug) {
                nbe.printStackTrace(System.err);
            }
            System.exit(1);
        } catch (OperationFailedException ofe) {
            System.err.println(
                "Internal error in core; cannot obtain lab or provider "
                + "information");
            if (debug) {
                ofe.printStackTrace(System.err);
            }
            System.exit(1);
        } catch (RemoteException re) {
            System.err.println(
                "Cannot access core components; RMI registry may be offline");
            if (debug) {
                re.printStackTrace(System.err);
            }
            System.exit(1);
        } catch (IOException ioe) {
            System.err.println("IO error while reading file " + confFile
                    + ": " + ioe.getMessage());
            System.exit(1);
        }

        /* initialize internal data structures */
        blockMap = new TreeMap<String, Object>();

        /* process files */
        if (i >= args.length) {
            try {
                (new CifImporter()).run();
            } catch (FileNotFoundException fnfe) {

                /* this should never happen */

                fnfe.printStackTrace(System.err);
                System.exit(1);
            }
        } else if (parallel) {
            ThreadGroup importerGroup = new ThreadGroup("Per-CIF Threads");
            List<Thread> threadList = new LinkedList<Thread>();

            for (; i < args.length; i++) {
                try {
                    Thread t = new Thread(importerGroup,
                            new CifImporter(args[i]), args[i] + "-reader" );

                    t.start();
                    threadList.add(t);
                } catch (FileNotFoundException fnfe) {
                    System.err.println("File '" + args[i] + "' not found");
                }
            }
            try {
                while (threadList.size() > 0) {
                    threadList.remove(0).join();
                }
            } catch (InterruptedException ie) {
                System.err.println("Interrupted.");
                System.exit(1);
            }
        } else {
            for (; i < args.length; i++) {
                try {
                    (new CifImporter(args[i])).run();
                } catch (FileNotFoundException fnfe) {
                    System.err.println("File '" + args[i] + "' not found");
                }
            }
        }

        /* open the GUI */
        displayGUI(blockMap);
    }
}

