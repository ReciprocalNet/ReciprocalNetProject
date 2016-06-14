/*
 * IUMSC Reciprocal Net Project
 *
 * PropertyFileUpdater.java
 *
 * 16-Nov-2006: jobollin wrote first draft
 */

package org.recipnet.common;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class that updates property files based on a template, optional current
 * properties and optional overriding properties.
 *
 * @author jobollin
 * @version 1.0
 */
public class PropertyFileUpdater {
    
    /**
     * A {@code Pattern} that matches one or more characters immediately
     * following a property name in the {@code Properties} file format.  In some
     * perverse cases the matched text may be part of the key; this is the case
     * if the length of the first (and only) captured group is odd.  Note that
     * when this pattern does match the end of the key, any whitespace
     * contiguous with the end of the match is insignificant, and not part of
     * the value.
     */
    private final static Pattern KEY_TERMINATOR_PATTERN
            = Pattern.compile("(\\\\*)[\\s:=]");

    /**
     * A {@code Pattern} matching the entirety of a natural line of properties
     * file input that constitutes the first line of a logical line that is
     * continued over more than one natural line.  In some perverse case this
     * pattern may match 
     */
    private final static Pattern CONTINUED_LINE_PATTERN
            = Pattern.compile(
                    // leading whitespace, greedy
                    "\\s*+"
                    /*
                     * the next (first non-whitespace) character must not be
                     * '!' or '#'
                     */
                    + "(?![!#])"
                    // anything else may follow ...
                    + ".*"
                    // up to a string of one or more backslashes (captured)
                    + "(\\\\)+");

    private final static Pattern TRAILING_NON_WS_PATTERN
            = Pattern.compile("\\s*+(.*)");

    /**
     * The name of the template file to use
     */
    private final String templateName;

    /**
     * The name of the overrides file to use
     */
    private final String overridesName;

    /**
     * Initializes a new {@code PropertyFileUpdater} with the specified
     * template file and overrides file names.  The names will be resolved at
     * run-time via this class's {@code ClassLoader}
     *
     * @param templateName the name of the template file
     * @param overridesName the name of the overrides file
     */
    public PropertyFileUpdater(String templateName, String overridesName) {
        if (templateName == null) {
            throw new NullPointerException("null template name");
        }
        this.templateName = templateName;
        this.overridesName = overridesName;
    }

    /**
     * The entry point for running this class as an application
     *
     * @param args the program arguments: zero or more {@code Strings}, or an
     *        array of the same
     *        
     * @throws IOException if an I/O error occurs
     */
    public static void main(String... args) throws IOException {
        String templateName = null;
        String overridesName = null;
        String fileName = null;
        boolean suppressUnchanged = false;
        
        /*
         * Process arguments 
         */
        
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("--")) {
                String option = args[i].substring(2);

                try {
                    if (option.equals("template")) {
                        templateName = args[++i];
                        if (templateName.startsWith("--")) {
                            printUsageMessage();
                            return;
                        }
                        continue;
                    } else if (option.equals("overrides")) {
                        overridesName = args[++i];
                        if (overridesName.startsWith("--")) {
                            printUsageMessage();
                            return;
                        }
                        continue;
                    } else if (option.equals("suppress-unchanged")) {
                        suppressUnchanged = true;
                        continue;
                    } else if (option.length() > 0) {
                        printUsageMessage();
                        return;
                    }
                } catch (IndexOutOfBoundsException ioobe) {
                    printUsageMessage();
                    return;
                }
            }
            if (i + 1 < args.length) { // only one non-option argument allowed
                printUsageMessage();
                return;
            } else {
                fileName = args[i];
                // Will exit the loop after this iteration
            }
        }
        
        if ((templateName == null) || (fileName == null)) {
            // a template and target file are required
            printUsageMessage();
            return;
        }
        
        /*
         * Perform update 
         */

        PropertyFileUpdater updater
                = new PropertyFileUpdater(templateName, overridesName);
        
        PropertyUpdateResult result
                = updater.updateProperties(new File(fileName));
        
        /*
         * Report results
         */
        System.out.println();
        System.out.println("Property file update results");
        System.out.println("============================");

        System.out.println();
        if (suppressUnchanged) {
            System.out.print('(');
            System.out.print(result.getUnchangedPropertyNames().size());
            System.out.println(" properties unchanged)");
        } else {
            System.out.println("Properties retained unchanged");
            System.out.println("-----------------------------");
            for (String propertyName : result.getUnchangedPropertyNames()) {
                System.out.println(propertyName);
                System.out.println();
            }
        }
        System.out.println();
        
        System.out.println();
        System.out.println("Properties added (value)");
        System.out.println("------------------------");
        for (Entry<String, String> prop
                : result.getAddedProperties().entrySet()) {
            System.out.print(prop.getKey());
            System.out.print(" (");
            System.out.print(prop.getValue());
            System.out.println(')');
            System.out.println();
        }
        System.out.println();
        
        System.out.println();
        System.out.println("Properties changed (new value)");
        System.out.println("------------------------------");
        for (Entry<String, String> prop
                : result.getModifiedProperties().entrySet()) {
            System.out.print(prop.getKey());
            System.out.print(" (");
            System.out.print(prop.getValue());
            System.out.println(')');
            System.out.println();
        }
        System.out.println();
        
        System.out.println();
        System.out.println("Properties removed (original value)");
        System.out.println("-----------------------------------");
        for (Entry<String, String> prop
                : result.getRemovedProperties().entrySet()) {
            System.out.print(prop.getKey());
            System.out.print(" (");
            System.out.print(prop.getValue());
            System.out.println(')');
            System.out.println();
        }
        System.out.println();
        System.out.println();
        
        // done
    }
    
    /*
     * Prints a usage message to the standard error stream descibing the
     * invocation syntax for running this class as an application
     */
    private static void printUsageMessage() {
        System.err.println("usage: PropertyFileUpdater "
                + "--template <template_name> "
                + "[--overrides <overrides_name>] "
                + "[<existing_properties_name>]"
                );
    }
    
    /**
     * Updates the properties file designated by the argument based on the
     * configured template and property override files. The program that invokes
     * this method must have sufficient system privileges to read and delete the
     * specified file, to create a new file in the same directory, and to rename
     * files it owns.
     * 
     * @param propertiesFile a {@code File} designating the property file to
     *        update
     * 
     * @return a {@code PropertyUpdateResult} representing the results of the
     *         update
     * 
     * @throws IOException if an I/O error occurs
     */
    public PropertyUpdateResult updateProperties(File propertiesFile)
            throws IOException {
        if ((propertiesFile != null) && (!propertiesFile.exists())) {
            propertiesFile.createNewFile();
        }

        Properties defaultProps = makeProperties(templateName, null);
        Properties customProps = makeProperties(propertiesFile, defaultProps);
        Properties mergedProperties = makeProperties(overridesName, customProps);
        File tempFile = File.createTempFile("conf", null,
                (propertiesFile == null) ? null
                        : propertiesFile.getAbsoluteFile().getParentFile());
        BufferedReader input = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream(templateName),
                "ISO-8859-1"));
        PrintWriter output
                = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(tempFile), "ISO-8859-1")));
        PropertyUpdateResult result
                = new PropertyUpdateResult(makeProperties(propertiesFile, null)); 
        Properties props = new Properties();
        Matcher nonwsMatcher = TRAILING_NON_WS_PATTERN.matcher("");
        Matcher termMatcher = KEY_TERMINATOR_PATTERN.matcher("");
        
        for (String logicalLine; (logicalLine = getNextLine(input)) != null;) {
            
            // Determine whether the line contains a property definition
            props.clear();
            props.load(new ByteArrayInputStream(
                    logicalLine.getBytes("ISO-8859-1")));
            
            if (props.size() > 0) { // the line contains a property definition
                
                assert props.size() == 1;
                
                String propName = (String) props.keys().nextElement();

                assert mergedProperties.getProperty(propName) != null
                        : "'" + propName + "' not present in the merged props";

                String propValue = mergedProperties.getProperty(propName);
                
                nonwsMatcher.reset(logicalLine);
                if (!nonwsMatcher.matches()) {
                    throw new IllegalStateException("can't happen: '"
                            + nonwsMatcher.pattern().pattern()
                            + "' must match every input");
                }
                
                String data = nonwsMatcher.group(1);
                int valueStart = data.length();
                
                for (termMatcher.reset(data); termMatcher.find(); ) {
                    if ((termMatcher.group(1).length() & 0x1) == 0) {
                        valueStart = termMatcher.end();
                        break;
                    }
                }
                
                // echo back the property name
                output.print(data.substring(0, valueStart - 1));

                // print a '=' for the key terminator
                output.print('=');
                
                // print the resolved property value
                printPropertyValue(propValue, output);
                result.recordPropertyResult(propName, propValue);
                
                // terminate the line
                output.println();
                
            } else { // the line does not contain a property definition
                output.println(logicalLine);
            }
        }
        
        output.close();
        input.close();
       
        if (propertiesFile != null) { 
            propertiesFile.delete();
            tempFile.renameTo(propertiesFile);
        } else {
            tempFile.delete();
        }
        
        return result;
    }
    
    /**
     * Prints the specified value to the specified output stream, in a format
     * compatible with subsequent reloading via a {@code Properties} object
     *
     * @param value the value to print
     * @param output a {@code PrintWriter} to which to direct the output
     */
    private void printPropertyValue(String value, PrintWriter output) {
        for (char c : value.toCharArray()) {
            switch (c) {
                case '\\':
                    output.print("\\\\");
                    break;
                case '\f':
                    output.print("\\f");
                    break;
                case '\n':
                    output.print("\\n");
                    break;
                case '\r':
                    output.print("\\r");
                    break;
                default:
                    if (c < 0x100) {     // a Latin-1 character
                        if (Character.isISOControl(c)) {
                            output.print("\\u");
                            output.print((c < 0x10) ? "000" : "00");
                            output.print(Integer.toHexString(c));
                        } else {
                            output.print(c);
                        }
                    } else {             // a non-Latin-1 character
                        output.print("\\u");
                        if (c < 0x1000) {
                            output.print("0");
                        }
                        output.print(Integer.toHexString(c));
                    }
            }
        }
    }
    
    /**
     * Returns the next <em>logical</em> line from the input, or {@code null} if
     * there is none
     *
     * @param input a {@code BufferedReader} from which to read natural lines
     * 
     * @return a {@code String} containing the next logical line available from
     *         the input
     *         
     * @throws IOException if an I/O error occurs while reading the input
     */
    private String getNextLine(BufferedReader input) throws IOException {
        String line = input.readLine();
        
        if (line == null) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder(line);
            Matcher continuationMatcher = CONTINUED_LINE_PATTERN.matcher("");
            Matcher nonWsMatcher = TRAILING_NON_WS_PATTERN.matcher("");
            
            for (boolean continued = false; line != null;
                    line = input.readLine()) {
            
                if (continued) {
                    
                    /*
                     * ignore leading whitespace on the second and subsequent
                     * natural lines of a continued logical line
                     */ 
                    
                    nonWsMatcher.reset(line);
                    if (!nonWsMatcher.matches()) {
                        throw new IllegalStateException("can't happen: '"
                                + nonWsMatcher.pattern().pattern()
                                + "' must match every input");
                    }
                    sb.append(nonWsMatcher.group(1));
                } // else sb already contains the line
                
                // Is the line (further) continued?
                continuationMatcher.reset(sb.toString());
                if (continuationMatcher.matches()
                        && ((continuationMatcher.group(1).length() & 0x1) == 0x1)) {
                    
                    /*
                     * The line is continued.  Remove the continuation mark and
                     * allow the loop to cycle
                     */
                    sb.delete(continuationMatcher.end(1) - 1, sb.length());
                    continued = true;
                } else {
                    
                    // The line is not continued; done with this logical line
                    break;
                }
            }
            
            return sb.toString();
        }
    }
    
    /**
     * Creates a properties object based on a properties file of the specified
     * name and on the specified default properties. The name will be resolved
     * via this class's {@code ClassLoader}
     * 
     * @param propertiesName the name of the properties file to load; if
     *        {@code null}, no attempt is made to load properties
     * @param defaults the default {@code Properties} to be supplied by the
     *        created {@code Properties} object; if {@code null}, no default
     *        properties will be provided
     * 
     * @return a new {@code Properties} object containing the properties read
     *         from the specified properties file (if any) and relying on the
     *         specified default properties (if any)
     * 
     * @throws IOException if an I/O error occurs
     */
    private Properties makeProperties(String propertiesName, Properties defaults)
            throws IOException {
        Properties properties = ((defaults == null) ? new Properties()
                : new Properties(defaults));
        
        if (propertiesName != null) {
            InputStream input = getClass().getClassLoader().getResourceAsStream(
                    propertiesName);
            
            input = new BufferedInputStream(input);
            properties.load(input);
            input.close();
        }
        
        return properties;
    }
    
    /**
     * Creates a properties object based on a properties file designated by the
     * specified {@code File} object name and on the specified default
     * properties. The name will be resolved via this class's
     * {@code ClassLoader}
     * 
     * @param propertiesFile a {@code File} designating the properties file to
     *        load; if {@code null}, no attempt is made to load properties
     * @param defaults the default {@code Properties} to be supplied by the
     *        created {@code Properties} object; if {@code null}, no default
     *        properties will be provided
     * 
     * @return a new {@code Properties} object containing the properties read
     *         from the specified properties file (if any) and relying on the
     *         specified default properties (if any)
     * 
     * @throws IOException if an I/O error occurs
     */
    private Properties makeProperties(File propertiesFile, Properties defaults)
            throws IOException {
        Properties properties = ((defaults == null) ? new Properties()
                : new Properties(defaults));
        
        if (propertiesFile != null) {
            InputStream input = new BufferedInputStream(
                    new FileInputStream(propertiesFile));
            
            properties.load(input);
            input.close();
        }
        
        return properties;
    }
    
    /**
     * A class providing a summary of the results of a property file update 
     *
     * @author jobollin
     * @version 1.0
     */
    public static class PropertyUpdateResult {
        
        /**
         * A {@code Properties} containing those properties that were added
         * during the update operation
         */
        private final Map<String, String> addedProperties
                = new LinkedHashMap<String, String>();
        
        /**
         * A {@code Properties} containing those properties that were modified
         * during the update operation (with values as of <em>after</em> the
         * update
         */
        private final Map<String, String> modifiedProperties
                = new LinkedHashMap<String, String>();
        
        /**
         * A {@code Properties} containing those properties that were removed
         * during the update operation
         */
        private final Map<String, String> originalProperties
                = new LinkedHashMap<String, String>();
        
        /**
         * A {@code Set<String>} containing the names of the unchanged
         * properties
         */
        private final Set<String> unchangedPropertyNames
                = new LinkedHashSet<String>();
        
        /**
         * Initializes a new {@code PropertyUpdateResult} with the specified
         * original properties
         *
         * @param originalProperties a {@code Properties} containing the
         *        original properties from the file that is being (or was)
         *        updated
         */
        public PropertyUpdateResult(Properties originalProperties) {
            for (Enumeration<?> propNames = originalProperties.propertyNames();
                    propNames.hasMoreElements(); ){
                String propName = (String) propNames.nextElement();
                
                this.originalProperties.put(propName,
                        originalProperties.getProperty(propName));
            }
        }
        
        /**
         * Sets a property among the added properties
         *
         * @param name the name of the property to set
         * @param value the property value to set
         */
        private void addAddedProperty(String name, String value) {
            addedProperties.put(name, value);
        }
        
        /**
         * Returns a {@code Properties} containing (as defaults) those
         * properties that were added during the update described by these
         * results
         * 
         * @return the added properties as a {@code Properties}
         */
        public Map<String, String> getAddedProperties() {
            return Collections.unmodifiableMap(addedProperties);
        }
        
        /**
         * Sets a property among the modified properties
         *
         * @param name the name of the property to set
         * @param value the property value to set
         */
        private void addModifiedProperty(String name, String value) {
            modifiedProperties.put(name, value);
        }
        
        /**
         * Returns a {@code Properties} containing (as defaults) those
         * properties that were modified during the update described by these
         * results; the property values are the ones <em>after</em> the update 
         *
         * @return the modified properties as a {@code Properties}
         */
        public Map<String, String> getModifiedProperties() {
            return Collections.unmodifiableMap(modifiedProperties);
        }
        
        /**
         * Adds a property name to those of unmodified properties
         *
         * @param name the name of the unchanged property
         */
        private void addUnchangedPropertyName(String name) {
            unchangedPropertyNames.add(name);
        }
        
        /**
         * Returns an unmodifiable set of the names of properties that were
         * retained unchanged during the update described by these results
         *
         * @return the unchangedPropertyNames as a {@code Set<String>}
         */
        public Set<String> getUnchangedPropertyNames() {
            return Collections.unmodifiableSet(unchangedPropertyNames);
        }
        
        /**
         * Determines which properties where removed during the update operation
         * by comparing the original properties with the lists of those modified
         * or removed.
         *
         * @return a {@code Properties} containing the properties that were
         *         present among the original properties but were not flagged
         *         as either modified or retained unchanged 
         */
        public Map<String, String> getRemovedProperties() {
            Map<String, String> rval
                    = new LinkedHashMap<String, String>(originalProperties);
            
            rval.keySet().removeAll(modifiedProperties.keySet());
            rval.keySet().removeAll(unchangedPropertyNames);
            
            return rval;
        }
        
        /**
         * Records the issuance of a property to the output, tracking whether
         * it is a new one (relative to the configured original properties) or
         * whether it is an existing one, either modified or not
         *
         * @param name the name of the property that was output
         * @param value the value of the property that was output
         */
        public void recordPropertyResult(String name, String value) {
            if (originalProperties.containsKey(name)) {
                if (value.equals(originalProperties.get(name))) {
                    addUnchangedPropertyName(name);
                } else {
                    addModifiedProperty(name, value);
                }
            } else {
                addAddedProperty(name, value);
            }
        }
    }
}
