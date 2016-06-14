/*
 * Reciprocal Net Project
 *
 * CifFile.java
 *
 * 17-Jan-2005: jobollin wrote first draft
 * 22-Dec-2005: jobollin fixed a bug in DataCell.removeDataForName() that
 *              caused the tests to throw a java.lang.AssertionError (when
 *              assertions were enabled) behavior with assertions disabled is
 *              not affected
 * 06-Jun-2006: jobollin fixed various compiler warnings  
 */

package org.recipnet.common.files;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.regex.Pattern;

import org.recipnet.common.ConcurrentCollection;
import org.recipnet.common.UnionCollection;
import org.recipnet.common.files.cif.UnknownValue;

/**
 * <p>
 * A class providing a structured representation of the semantic content of a
 * CIF file.  CIF (<em>Crystallographic Information File</em>) is a dialect of
 * the STAR (<em>Self-defining Text ARchive</em>) language, particularly
 * designed as a means to express information about crystallographic experiments
 * and their results.  Technically, the term "CIF file" is redundant and its use
 * is discouraged, but it is used here to differentiate between the CIF
 * language itself ("CIF") and expressions of that language ("CIF file").
 * </p>
 * <h2>CIF</h2>
 * <p>
 * <a href="http://www.iucr.org/iucr-top/cif/">CIF</a> is a hierarchical
 * language having some similarities with XML (and many differences).  The
 * lowest-level grammatical elements with semantic content are "data names" and
 * "data values", where each value is associated with a name that identifies it.
 * Names and values may appear as isolated pairs, or they may be assembled into
 * "loop" structures.  A CIF loop can be thought of as a record definition (in
 * the form of one or more data names) followed by one or more record instances
 * (each one consisting of one data value for each data name in the definition).
 * Loops may not be nested in CIF.
 * </p><p>
 * Name / value pairs and loops are aggregated into "data cells" of
 * two types: "save frames" and "data blocks".  Within each cell, all data names
 * must be unique.  Each save frame and data block has an identifier (a
 * "frame code" or "block code", respectively).  Save frames may only appear
 * within data blocks; they are scoped to the data block in which they reside;
 * and their frame codes must be unique within that block. A CIF file consists
 * of zero or more data blocks, each with a unique block code.  Data names in a
 * data block or save frame are local to that cell and inaccessible to other
 * cells, including, for save frames, the enclosing data block.  Data names,
 * frame codes, and block codes are <em>case insensitive</em>, but it is
 * preferred that processors retain case.
 * </p><p>
 * A well-formed CIF file may contain any data names consistent with the data
 * name syntax, but <em>valid</em> CIF files must use only data names defined
 * in one or more CIF dictionaries.  A CIF dictionary defines, for one or more
 * data names, the name's "category", the meaning of the associated value, the
 * value's type, its allowed range, whether it is permitted (or required) to
 * appear in a loop, and other details.  CIF dictionaries are themselves valid
 * CIF files with their own (small) dictionary, the "Dictionary Definition
 * Language".
 * </p>
 * <h2>{@code CifFile}</h2>
 * <p>
 * This class reflects the structure of a CIF file, in that it is a mutable
 * collection of data blocks (represented by {@link CifFile.DataBlock}
 * instances).  It supports addition and removal of data blocks (with
 * enforcement of the requirement for unique block codes) and provides the
 * set of block codes and an {@code Iterator} over the data blocks.  Data
 * blocks may, but are not required to, contain save frames, data loops, and
 * name / value pairs.  The structure bears a conceptual similarity to a DOM
 * {@code Document}.
 * <p></p>
 * Instances of this class are not thread safe.
 * </p>
 * 
 * @author John C. Bollinger
 * @version 0.9.0
 */
public class CifFile {

    /**
     * A regular expression matching syntactically legal CIF data names
     */
    final static Pattern DATA_NAME_PATTERN = Pattern.compile(
            "_[-0-9A-Za-z!%&()*+,./<>=?@\\\\^`{|}~#$\"'_;:\\[\\]]+");
            
    /**
     * A regular expression matching syntactically legal CIF block codes and
     * frame codes
     */
    final static Pattern CELL_CODE_PATTERN = Pattern.compile(
            "[-0-9A-Za-z!%&()*+,./<>=?@\\\\^`{|}~#$\"'_;\\[\\]]+");
    
    /**
     * a map from data block names to associated DataBlock objects, as a whole
     * representing the entire semantic content of a CIF file
     */
    private final Map<String, DataBlock> dataBlockMap;

    /**
     * Constructs a new CifFile with empty content
     */
    public CifFile() {
        dataBlockMap = new LinkedHashMap<String, DataBlock>();
    }

    /**
     * Returns an unmodifiable view of the set of names of data blocks in this
     * {@code CifFile}.  Block names are case insensitive.
     * 
     * @return an unmodifiable {@code Set} of the names of the data blocks
     *         currently in this {@code CifFile}
     */
    public Set<String> getDataBlockNames() {
        return Collections.unmodifiableSet(dataBlockMap.keySet());
    }

    /**
     * Returns the data block from this {@code CifFile} corresponding to
     * the specified block name.  Block names are case insensitive.
     * 
     * @param  name a {@code String} containing the name of the desired
     *         data block
     *  
     * @return the {@code DataBlock} designated by the specified name in
     *         this {@code CifFile}, or {@code null} if there isn't
     *         one
     */
    public DataBlock getDataBlock(String name) {
        return dataBlockMap.get(name.toLowerCase());
    }

    /**
     * Adds the specified data block to this {@code CifFile}
     * 
     * @param  block a {@code CifFile.DataBlock} representing the data
     *         block to be added; should not be {@code null}
     * 
     * @throws IllegalArgumentException if the supplied {@code DataBlock}
     *         has the same name (ignoring case) as another data block already
     *         in this {@code CifFile} (including if the block itself has
     *         already been added)
     */
    public void addDataBlock(DataBlock block) {
        String blockName = block.getName().toLowerCase();
        
        if (dataBlockMap.containsKey(blockName)) {
            throw new IllegalArgumentException("Duplicate block name");
        } else {
            dataBlockMap.put(blockName, block);
        }
    }

    /**
     * Removes the datablock designated by the specified name from this
     * {@code CifFile}, and returns it
     * 
     * @param  name a {@code String} containing the case-insensitive name
     *         of the data block to remove; should not be {@code null}
     * 
     * @return the {@code CifFile.DataBlock} representing the removed data
     *         block, or {@code null} if there was no data block with the
     *         specified name in this {@code CifFile} 
     */
    public DataBlock removeDataBlock(String name) {
        return dataBlockMap.remove(name.toLowerCase());
    }

    /**
     * Returns an {@code Iterator} over the {@code CifFile.DataBlock}
     * objects representing the data blocks of this {@code CifFile}
     * 
     * @return an {@code Iterator} over the data blocks of this
     *         {@code CifFile}; does not support removal
     */
    public Iterator<DataBlock> blockIterator() {
        return Collections.unmodifiableCollection(
                dataBlockMap.values()).iterator();
    }
    
    /**
     * Determines whether the specified string meets the CIF syntax requirements
     * for data names.  Simply put, these require only that the string start
     * with an underscore and contain only CIF-legal, non-whitespace characters.
     * 
     * @param  name the {@code String} to test
     * 
     * @return {@code true} if {@code name} is a valid CIF data name
     */
    public static boolean isValidDataName(String name) {
        return DATA_NAME_PATTERN.matcher(name).matches();
    }

    /**
     * Represents the abstract CIF / STAR concept of a <em>data cell</em>, of
     * which data blocks and save frames are the specific kinds relevant to CIF.
     * Data cells have names and contain <em>data items</em>, which may be
     * either (name, value) pairs or looped data.  This class supports adding
     * data items of either kind, retrieving a collection of all the data names,
     * retrieving data items by data name, and removing data by data name.
     *  
     * @author John C. Bollinger
     * @version 0.9.0
     */
    public static abstract class DataCell {

        /**
         * The name of this data cell; e.g. a block code or frame code
         */
        private final String name;

        /**
         * A map from (lower case) data name to corresponding data value for
         * all the scalar (non-looped) data items in this data cell
         */
        final Map<String, ScalarData<?>> scalarMap =
                new HashMap<String, ScalarData<?>>();

        /**
         * A map from (lower case) data name to corresponding DataLoop for
         * all looped data items in this data cell
         */
        final Map<String, DataLoop> loopMap =
                new HashMap<String, DataLoop>(); 

        /**
         * A dynamic {@code Collection} view of the combined key sets of
         * the scalar and loop maps; not externally modifiable.  Unlike most
         * collections' iterators, this one is <em>not</em> guaranteed to
         * exhibit fail-fast behavior when the collection changes.
         */
        final Collection<String> definedNames = new UnionCollection<String>(
                    scalarMap.keySet(), loopMap.keySet());

        /**
         * A {@code LoopListener} with which this data cell monitors
         * changes to the data loops it contains, so as to be able to correctly
         * maintain the internal {@code loopMap} 
         */
        private final LoopListener loopListener = new LoopListener() {
                public void loopChanged(LoopEvent event) {
                    DataLoop source = event.getSource();
    
                    switch (event.getEventCode()) {
                        
                        // a new data name was added to the loop
                        case NAME_ADDED:
                            String dataName = event.getDataName().toLowerCase();
                            if (definedNames.contains(dataName)) {
                                throw new IllegalStateException(
                                        "Duplicate data name: '" + dataName
                                        + "'");
                            } else {
                                loopMap.put(dataName, source);
                            }
                            break;
                            
                        // a data name was removed from the loop
                        case NAME_REMOVED:
                            DataLoop loop = loopMap.remove(
                                    event.getDataName().toLowerCase());
                            
                            assert source == loop;
                            break;
                            
                        /*
                         * the last data value (and possibly the last name) was
                         * removed from the loop
                         */
                        case LOOP_EMPTIED:
                            for (String datumName : source.getLowerCaseNames()) {
                                loopMap.remove(datumName);
                            }
                            source.removeLoopListener(this);
                            break;
                        default:
                            // nothing to do
                            break;
                    }
                }

                public boolean approveLoopChange(LoopEvent event) {
                    switch (event.getEventCode()) {
                        
                        // a new data name is proposed for addition to the loop
                        case NAME_ADDED:
                            return !definedNames.contains(
                                    event.getDataName().toLowerCase());
                            
                        // anything else is always OK
                        default:
                            return true;
                    }
                }
            };

        /**
         * Initializes a new {@code DataCell} with the specified name 
         *
         * @param  name the new cell's name, as a {@code String}; may not
         *         be {@code null}
         */                
        public DataCell(String name) {
            if (name == null) {
                throw new NullPointerException("null data cell name");
            } else {
                this.name = name;
            }
        }

        /**
         * Returns this cell's name
         * 
         * @return this {@code DataCell}'s name, as a {@code String};
         *         will not be {@code null}
         */
        public String getName() {
            return name;
        }

        /**
         * Adds a scalar (non-looped) data item to this {@code DataCell}
         *  
         * @param  data the data item as a {@code ScalarData}; may not be
         *         {@code null}, and may not have a name matching
         *         (ignoring case) any data item already in this cell
         * 
         * @throws IllegalArgumentException if this {@code DataCell}
         *         already contains data having the specified case-insensitive
         *         name
         */
        public void addScalar(ScalarData<?> data) {
            String dataName = data.getName().toLowerCase();
            
            if (definedNames.contains(dataName)) {
                throw new IllegalArgumentException(
                        "Duplicate data name '" + dataName + "'");
            } else {
                scalarMap.put(dataName, data);
            }
        }

        /**
         * Adds a loop data structure to this {@code DataCell}.  This cell
         * will hereafter hold a reference to the loop as long as it contains
         * any data, and modifications to the loop or to this cell will be
         * appropriately reflected in the other: names added to or removed
         * from the loop will be added to or removed from the cell, and names
         * from the loop that are removed from the cell will also be removed
         * from the loop.  If the loop is ever emptied then it is automatically
         * removed from the cell (because empty loops are not permitted in CIF).
         * 
         * @param  loop the {@code DataLoop} to add; may not be
         *         {@code null} or empty of data, and may not contain data
         *         names that are already defined in this cell.
         * 
         * @throws IllegalArgumentException if {@code loop} is empty, or if it
         *        contains any data name already present in this
         *        {@code DataCell} 
         */
        public void addLoop(DataLoop loop) {
            Set<String> names = new HashSet<String>(loop.getLowerCaseNames());

            if (loop.getRecordCount() == 0) {        
                throw new IllegalArgumentException("Loop contains no data");
            } else {
                
                // There can be no data without data names
                assert (names.size() > 0);
                
                // Check for duplicates
                names.retainAll(definedNames);
                if (names.size() != 0) {
                    StringBuffer sb = new StringBuffer(
                            "Duplicate data name(s):");
        
                    for (String existingName : names) {
                        sb.append(" '").append(existingName).append("',");
                    }
                    sb.deleteCharAt(sb.length() - 1);
        
                    throw new IllegalArgumentException(sb.toString());
                } else {
                    
                    // add the loop
                    for (String datumName : loop.getLowerCaseNames()) {
                        loopMap.put(datumName, loop);
                    }
                    loop.addLoopListener(loopListener);
                }
            }
        }

        /**
         * Returns an (externally) unmodifiable {@code Collection}
         * containing all the data names defined for this {@code DataCell},
         * in canonical (lower) case.  The contents of the
         * {@code Collection} will change as do the contents of this
         * {@code DataCell}.  The returned collection's iterator is not
         * guaranteed to fail fast or even fail at all (though it may do) if the
         * collection changes during iteration.
         *   
         * @return an unmodifiable {@code Collection} view of the canonical
         *         data names in this {@code DataCell}
         */
        public Collection<String> getDataNames() {
            return definedNames;
        }

        /**
         * A convenience method that tests whether data for the specified
         * (case-insensitive) data name are contained in this
         * {@code DataCell}.
         *  
         * @param  datumName the data name of interest, as a {@code String};
         *         conformance to CIF data name syntax is not tested
         * 
         * @return {@code true} if the data name is defined for this data
         *         cell; otherwise {@code false}
         */
        public boolean containsName(String datumName) {
            return definedNames.contains(datumName.toLowerCase());
        }

        /**
         * A convenience method that tests whether data for the specified
         * (case-insensitive) data name are contained in a loop structure
         * in this {@code DataCell}.  If this method returns
         * {@code true} then {@code containsName()} will also return
         * {@code true} for the same name, but the converse does not hold.
         * 
         * @param  datumName the data name of interest, as a {@code String};
         *         conformance to CIF data name syntax is not tested
         * 
         * @return {@code true} if the data name is defined in a loop for
         *         this data cell; otherwise {@code false}
         */
        public boolean containsNameInLoop(String datumName) {
            return (loopMap.keySet().contains(datumName.toLowerCase()));
        }

        /**
         * Returns the {@code ScalarData} corresponding to the specified
         * data name, if any
         * 
         * @param  datumName a {@code String} containing the requested
         *         (case-insensitive) data name; must not be {@code null}
         * 
         * @return a {@code ScalarData} representing the CIF entry
         *         for the specified name, if present, or {@code null} if
         *         the specified name does not appear in this {@code DataCell}
         *         
         * @throws IllegalStateException if the data name is present in this
         *         {@code DataCell} but in a loop
         */
        public ScalarData<?> getScalarForName(String datumName) {
            String lcName = datumName.toLowerCase();
            
            if (definedNames.contains(lcName)) {
                ScalarData<?> data = scalarMap.get(lcName);
                
                if (data == null) {
                    throw new IllegalStateException("Data for name '" + lcName
                            + "' is looped");
                } else {
                    return data;
                }
            } else {
                return null;
            }
        }

        /**
         * Returns the {@code DataLoop} object from this
         * {@code CifFile} containing the data for the specified
         * (case-insensitive) data name
         * 
         * @param  datumName a {@code String} containing the data name of
         *         interest; should not be {@code null}
         * 
         * @return the {@code DataLoop} containing the specified data name,
         *         or {@code null} if the specified name does not appear in
         *         this {@code DataCell}
         *         
         * @throws IllegalStateException if the data name is present in this
         *         {@code DataCell} but not in a loop
         */
        public DataLoop getLoopForName(String datumName) {
            String lcName = datumName.toLowerCase();
            
            if (definedNames.contains(lcName)) {
                DataLoop data = loopMap.get(lcName);
                
                if (data == null) {
                    throw new IllegalStateException("Data for name '" + lcName
                            + "' is not looped");
                } else {
                    return data;
                }
            } else {
                return null;
            }
        }

        /**
         * Removes the specified data name and associated data value(s) from
         * this {@code CifFile}, including removing them from any
         * {@code DataLoop} object to which it may also belong (and which
         * may be externally visible).  Has no effect (but returns
         * {@code false}) if the specified name was not in this
         * {@code CifFile}
         *  
         * @param  datumName a {@code String} containing the (case-insensive)
         *         data name to remove; should not be {@code null}
         * 
         * @return {@code true} if the name was removed; {@code false}
         *         if it was not present
         */
        public boolean removeDataForName(String datumName) {
            String lcName = datumName.toLowerCase();
            
            if (containsNameInLoop(lcName)) {
                try {
                    DataLoop loop = loopMap.get(datumName.toLowerCase());
                    
                    // The loop listener will handle removing the mapping
                    loop.removeName(lcName);
                    return true;
                } catch (IllegalArgumentException iae) {
                    
                    /*
                     * This exception would indicate that although the data name
                     * is registered in this CifFile as belonging to the
                     * specified loop, the loop doesn't actually contain it.
                     * This might occur in a data race situation, but otherwise
                     * would indicate an internal programming error in this
                     * class or one of its inner classes.
                     */
                    IllegalStateException ise = new IllegalStateException();
                    
                    ise.initCause(iae);
                    throw ise;
                }
            } else {
                return (scalarMap.remove(lcName) != null);
            }
        }
    }

    /**
     * A concrete {@code DataCell} subclass representing a CIF data block.
     * In addition to the standard characteristics of data cells, data blocks
     * may contain save frames (represented in this class by
     * {@code SaveFrame} onjects), and this class adds the ability to
     * add, retrieve, remove, and iterate over contained save frames
     * 
     * @author  John C. Bollinger
     * @version 0.9.0
     */
    public static class DataBlock extends DataCell {
        
        /**
         * An internal map from lower case frame codes to the corresponding
         * {@code SaveFrame} objects
         */
        private final Map<String, SaveFrame> saveFrameMap =
                new HashMap<String, SaveFrame>();
        
        /**
         * Initializes a new {@code DataBlock} with the specified block
         * name, which is the data block header less the leading "data_"
         * substring
         * 
         * @param  name the block name; must be a syntactically valid block code
         * 
         * @throws IllegalArgumentException if {@code name} is not a valid
         *         CIF block code
         */
        public DataBlock(String name) {
            super(name);
            if (!CELL_CODE_PATTERN.matcher(name).matches()) {
                throw new IllegalArgumentException(
                        "Invalid block code '" + name + "'");
            }
        }

        /**
         * Adds the specified {@code SaveFrame} to this data block,
         * provided that its frame code is not already used by another save
         * frame in this block
         * 
         * @param  frame a {@code CifFile.SaveFrame} representing the frame
         *         to add; should not be {@code null}
         * 
         * @throws IllegalArgumentException if {@code frame}'s
         *         (case-insensitive) frame code matches that of a save frame
         *         already in this data block
         */
        public void addSaveFrame(SaveFrame frame) {
            String frameName = frame.getName().toLowerCase();
            
            if (saveFrameMap.containsKey(frameName)) {
                throw new IllegalArgumentException("Duplicate framecode: '"
                        + frameName + "'");
            }
            saveFrameMap.put(frameName, frame);
        }

        /**
         * Looks up and returns the save frame in this data block matching the
         * specified (case insensitive) name
         * 
         * @param  name a {@code String} containing the name (frame code)
         *         of the desired save frame
         * 
         * @return a {@code CifFile.SaveFrame} representing the requested
         *         frame, if one is found; otherwise {@code null}
         */
        public SaveFrame getSaveFrameForName(String name) {
            return saveFrameMap.get(name.toLowerCase());
        }

        /**
         * Removes the save frame matching the specified (case insensitive) name
         * from this data block
         * 
         * @param  name a {@code String} containing the name (frame code)
         *         of the desired save frame
         * 
         * @return a {@code CifFile.SaveFrame} representing the specified
         *         frame, if it was present; otherwise {@code null}
         */
        public SaveFrame removeSaveFrame(String name) {
            String frameName = name.toLowerCase();
            
            return saveFrameMap.remove(frameName);
        }

        /**
         * Returns an {@code Iterator} over the save frames of this data
         * block; this iterator supports object removal, which results in the
         * subject save frame being removed from this data block
         *  
         * @return an {@code Iterator} over the save frames of this block
         */
        public Iterator<SaveFrame> saveFrameIterator() {
            return saveFrameMap.values().iterator();
        }
    }

    /**
     * A concrete {@code DataCell} subclass representing a CIF save frame.
     * 
     * @author  John C. Bollinger
     * @version 0.9.0
     */
    public static class SaveFrame extends DataCell {

        /**
         * Initializes a new {@code SaveFrame} with the specified name
         * (frame code), which is the data block header less the leading
         * "save_" substring
         * 
         * @param  name the frame name; must be a syntactically valid frame code
         * 
         * @throws IllegalArgumentException if {@code name} is not a valid
         *         CIF frame code
         */
        public SaveFrame(String name) {
            super(name);
            if (!CELL_CODE_PATTERN.matcher(name).matches()) {
                throw new IllegalArgumentException(
                        "Invalid frame code '" + name + "'");
            }
        }
    }

    /**
     * A class representing a name / value pair such as appears in a CIF file.
     * Although CIF data names are case insensitive, this class preserves the
     * case of the name with which it is constructed; indeed, that is largely
     * its purpose.  Instances of this class are immutable if, and only if, the
     * {@code CifValue}s they carry are immutable. 
     * 
     * @author  John C. Bollinger
     * @version 0.9.0
     * 
     * @param  <V> The specific type of {@code CifValue} carried by this
     *         {@code ScalarData}  
     */
    public static class ScalarData<V extends CifValue> {
        
        /** The data name of this {@code ScalarData} */ 
        private final String name;
        
        /** The data value of this {@code ScalarData} */
        private final V value;

        /**
         * Initializes a new {@code ScalarData} with the specified data
         * name and data value
         *
         * @param  name a {@code String} containing the data name; must
         *         conform to CIF data name syntax
         * @param  value a {@code CifFile.CifValue} representing the
         *         data value; must not be {@code null}
         * 
         * @throws IllegalArgumentException if {@code name} does not
         *         conform to CIF data name syntax
         */
        public ScalarData(String name, V value) {
            if (value == null) {
                throw new NullPointerException("Null CIF value");
            } else if (!isValidDataName(name)) {
                throw new IllegalArgumentException("Invalid CIF data name: '"
                        + name + "'");
            } else {
                this.name = name;
                this.value = value;
            }
        }

        /**
         * Returns the data name of this {@code ScalarData}
         * 
         * @return a {@code String} containing this
         *         {@code ScalarData}'s data name
         */
        public String getName() {
            return name;
        }

        /**
         * Returns the data value of this {@code ScalarData}
         * 
         * @return a {@code CifFile.CifValue} representing this
         *         {@code ScalarData}'s data value
         */
        public V getValue() {
            return value;
        }
    }

    /**
     * <p>
     * A class representing a CIF loop structure, which consists of a sequence
     * of data names and a sequence of corresponding data values (typically
     * multiple values for each name).  {@code DataLoop}s are mutable: data
     * names (with associated values) can be added or removed, and data for the
     * existing names can be added or removed.
     * </p><p>
     * <em>Loop resets.</em>  CifFile objects cannot contain empty loops; they
     * refuse to add empty loops, and remove existing member loops if ever they
     * become empty, thus always maintaining an internal state consistent with
     * the CIF specification.  This presents a bit of a problem for CIF editing,
     * however, if a loop is emptied of data and then repopulated: it will have
     * been dropped from its CifFile(s) in the process.  This issue is addressed
     * by giving {@code DataLoop} an internal flag that is raised whenever
     * an instance goes from containing data to not; while the flag is raised,
     * any attempt to add data or data names to the loop will cause an
     * exception to be thrown.  If a program has intentionally emptied the loop
     * and wishes to repopulate it then it can lower the "emptied" flag by
     * invoking the loop's reset() method.  
     * </p>
     * 
     * @author  John C. Bollinger
     * @version 0.9.0
     */
    public static class DataLoop {

        /**
         * A {@code List} of the data names of this loop, as submitted to
         * this loop, in the order submitted 
         */
        private final List<String> names;

        /**
         * A {@code List} of the data names of this loop, converted to
         * lower case, in the order submitted 
         */
        private final List<String> lcaseNames;

        /**
         * A {@code List} of the data values of this loop; values for all
         * loop records are concatenated into a single list
         */
        private final List<CifValue> values;

        /**
         * A {@code ConcurrentCollection} of the {@code LoopListener}s
         * registered on this loop; use of a {@code ConcurrentCollection} for
         * this purpose produces sensible (and non-exceptional) behavior when
         * listeners remove themselves and / or other listeners as part of their
         * loop event handling  
         */
        private final ConcurrentCollection<LoopListener> listeners;

        /**
         * A flag raised when this loop goes from containing data (in which
         * state it can be contained in a {@code CifFile.DataCell}) to not
         * containing data (in which state it cannot be contained in a
         * {@code CifFile.DataCell}), and lowered by the {@link #reset()} method
         */
        boolean emptied;

        /**
         * An int counter incremented whenever this loop is modified; this
         * enables fail-fast behavior this loop's record iterators. 
         */
        int versionCounter;

        /**
         * Initializes a new {@code DataLoop} with no data names or values
         */        
        public DataLoop() {
            names = new ArrayList<String>();
            lcaseNames = new ArrayList<String>();
            values = new ArrayList<CifValue>();
            versionCounter = 0;
            emptied = false;
            listeners = new ConcurrentCollection<LoopListener>();
        }

        /**
         * Initializes a new {@code DataLoop} with the specified data names
         * and values
         * 
         * @param  dataNames a {@code List} containing the data names for
         *         this loop, as {@code String}s.  Each name must conform
         *         to the syntax rules for CIF data names 
         * @param  data a {@code List} containing the data values for this
         *         loop; its size must be a multiple of the number of data
         *         names, and may be zero
         */
        public DataLoop(List<String> dataNames, List<? extends CifValue> data) {
            this();

            if (dataNames.size() == 0) {
                throw new IllegalArgumentException("empty data name list");
            } else if ((data.size() % dataNames.size()) != 0) {
                throw new IllegalArgumentException("Incomplete loop record");
            } else {
                for (String name : dataNames) {
                    String lcaseName = name.toLowerCase();
                    
                    if (lcaseNames.contains(lcaseName)) {
                        throw new IllegalArgumentException(
                                "Duplicate data name: '" + name + "'");
                    } else if (isValidDataName(name)) {
                        names.add(name);
                        lcaseNames.add(lcaseName);
                    } else {
                        throw new IllegalArgumentException(
                                "Illegal data name: '" + name + "'");
                    }
                }

                // add values with data type enforcement
                for (CifValue value : data) {
                    values.add(value);
                }
            }
        }

        /**
         * Adds a data name as the last in this loop, and inserts the unknown
         * value into the corresponding position in all existing loop records
         *   
         * @param  name a {@code String} containing the data name to add,
         *         which must conform to CIF syntax requirements
         * 
         * @throws IllegalArgumentException if the specified name is not a valid
         *         CIF data name
         * @throws IllegalStateException if this loop has previously been
         *         emptied and not yet reset  
         */
        public void addName(String name) {
            addName(name, getRecordSize());
        }
        
        /**
         * Adds the specified data name at the specified position in this loop,
         * and inserts the unknown value into the corresponding position in all
         * existing loop records
         *   
         * @param  name a {@code String} containing the data name to add,
         *         which must conform to CIF syntax requirements
         * @param  position the zero-based index among this loop's data names
         *         at which to place the specified name 
         * 
         * @throws IllegalArgumentException if the specified name is not a valid
         *         CIF data name
         * @throws IndexOutOfBoundsException if {@code position} is less
         *         than zero or greater than the number of data names already
         *         in this loop
         * @throws IllegalStateException if this loop has previously been
         *         emptied and not yet reset  
         */
        public void addName(String name, int position) {
            addName(name, position, UnknownValue.instance);
        }

        /**
         * Adds the specified data name at the specified position in this loop,
         * and inserts the specified value into the corresponding position in
         * all existing loop records, subject to approval by all registered
         * loop listeners
         *   
         * @param  name a {@code String} containing the data name to add,
         *         which must conform to CIF syntax requirements
         * @param  position the zero-based index among this loop's data names
         *         at which to place the specified name 
         * @param  defaultValue a {@code CifFile.CifValue} to insert into
         *         each loop record as the value corresponding to the new name
         * 
         * @throws IllegalArgumentException if the specified name is not a valid
         *         CIF data name, or if this loop already contains the data name
         * @throws IndexOutOfBoundsException if {@code position} is less
         *         than zero or greater than the number of data names already
         *         in this loop
         * @throws IllegalStateException if this loop has previously been
         *         emptied and not yet reset  
         */
        public void addName(String name, int position, CifValue defaultValue) {
            if (!isValidDataName(name)) {
                throw new IllegalArgumentException("Invalid CIF data name: '"
                        + name + "'");
            } else if (defaultValue == null) {
                throw new NullPointerException("default value cannot be null");
            } else if (name == null) {
                throw new NullPointerException("name cannot be null");
            } else if (emptied) {
                throw new IllegalStateException("Loop emptied and not reset");
            } else {
                String lcaseName = name.toLowerCase();
                
                if (lcaseNames.contains(lcaseName)) {
                    throw new IllegalArgumentException("Duplicate data name: '"
                            + name + "'");
                } else {
                    LoopEvent event = new LoopEvent(
                            LoopEvent.EventCode.NAME_ADDED, this, name);
                    
                    if (!proposeLoopEvent(event)) {
                        throw new IllegalArgumentException("Data name '"
                            + name + "' rejected");
                    }
                }
                
                int newRecordSize = getRecordSize() + 1;
                int recordCount = getRecordCount();

                markChanged();
                names.add(position, name); // may throw IndexOutOfBoundsException
                lcaseNames.add(position, name.toLowerCase());
                for (int record = 0; record < recordCount; record++) {
                    values.add(record * newRecordSize + position, defaultValue);
                }

                fireLoopEvent(new LoopEvent(LoopEvent.EventCode.NAME_ADDED,
                                            this, name));
            }
        }

        /**
         * Determines whether this {@code DataLoop} contains the specified
         * (case insensitive) data name
         * 
         * @param  name a {@code String} containing the data name of
         *         interest; should not be {@code null}
         * 
         * @return {@code true} if the name was found; {@code false}
         *         otherwise
         */
        public boolean containsName(String name) {
            return lcaseNames.contains(name.toLowerCase());
        }

        /**
         * Removes the specified (case insensitive) data name and all
         * associated data from this {@code DataLoop}
         *  
         * @param  name a {@code String} containing the data name to
         *         remove; should not be {@code null}
         * 
         * @throws IllegalArgumentException if the specified data name is not
         *         contained in this {@code DataLoop}  
         */
        public void removeName(String name) {
            int column = lcaseNames.indexOf(name.toLowerCase());

            if (column >= 0) {
                int recordSize = getRecordSize();

                markChanged();

                /*
                 * Remove values from back to front to avoid messy indexing
                 * issues
                 */
                for (int i = ((getRecordCount() - 1) * recordSize) + column;
                     i >= 0; i -= recordSize) {
                    values.remove(i);
                }
                names.remove(column);
                lcaseNames.remove(column);
                recordSize--;
                fireLoopEvent(new LoopEvent(LoopEvent.EventCode.NAME_REMOVED,
                                            this, name));
                checkEmptied();                                            
            } else {
                throw new IllegalArgumentException("'" + name
                        + "' is not part of this loop");
            }
        }

        /**
         * Returns an unmodifiable {@code List} of all the data names in
         * this data loop
         *   
         * @return an (externally) unmodifiable {@code List} view of the
         *         data names of this loop; although the names cannot be
         *         modified via this view, the view will reliably track data
         *         name additions and deletions performed via the loop itself
         */
        public List<String> getDataNames() {
            return Collections.unmodifiableList(names);
        }

        /**
         * Returns an unmodifiable {@code List} of lowercase versions of
         * all the data names in this data loop
         *   
         * @return an (externally) unmodifiable {@code List} view of the
         *         data names of this loop; although the names cannot be
         *         modified via this view, the view will reliably track data
         *         name additions and deletions performed via the loop itself
         */
        public List<String> getLowerCaseNames() {
            return Collections.unmodifiableList(lcaseNames);
        }

        /**
         * Adds a new loop record to the end of this loop
         * 
         * @param  newValues a {@code List} of
         *         {@code CifFile.CifValue} objects representing the
         *         values for the record; the size of the list must be the same
         *         as this loop's current record size
         * 
         * @throws IllegalArgumentException if {@code newValues.size()} is
         *         different from this loop's record size
         * @throws IllegalStateException if this loop has previously been
         *         emptied and not subsequently reset
         */
        public void addRecord(List<? extends CifValue> newValues) {
            addRecord(getRecordCount(), newValues);
        }
        
        /**
         * Adds a new loop record to this loop at the specified zero-based
         * index relative to the other loop records
         * 
         * @param  index the record index at which to insert the new record
         * @param  newValues a {@code List} of
         *         {@code CifFile.CifValue} objects representing the
         *         values for the record; the size of the list must be the same
         *         as this loop's current record size
         * 
         * @throws IllegalArgumentException if {@code newValues.size()} is
         *         different from this loop's record size
         * @throws IllegalStateException if this loop has no data names
         * @throws IndexOutOfBoundsException if {@code index} is less than
         *         zero or greater than the current number of loop records
         */
        public void addRecord(int index, List<? extends CifValue> newValues) {
            int recordSize = getRecordSize();
            
            if (newValues.size() != recordSize) {
                throw new IllegalArgumentException("Wrong number of values");
            } else if (recordSize == 0) {
                throw new IllegalStateException("Loop has no data names");
            } else {
                int start = index * recordSize;
                
                markChanged();
                values.subList(start, start).addAll(newValues);
                fireLoopEvent(new LoopEvent(LoopEvent.EventCode.RECORD_ADDED,
                                            this, index));
            }
        }
        
        /**
         * Returns a {@code List} of the {@code CifFile.CifValue}s of
         * the loop record at the specified zero-based index; the returned List
         * can be modified without effect on this loop
         *  
         * @param  index the zero-based index of the desired record
         * 
         * @return a {@code List} of the {@code CifFile.CifValue}s
         *         for the specified loop record
         * 
         * @throws IndexOutOfBoundsException if {@code index} is less than
         *         zero or greater than or equal to the number of loop records
         */
        public List<CifValue> getRecordValues(int index) {
            int recordSize = getRecordSize();
            
            if (recordSize == 0) {
                throw new IndexOutOfBoundsException("No loop record #" + index);
            } else {
                int start = index * recordSize;
                
                return new ArrayList<CifValue>(
                        values.subList(start, start + recordSize));
            }
        }
        
        /**
         * Removes the loop record at the specified zero-based index and
         * returns a {@code List} of the {@code CifFile.CifValue}s of
         * the erstwhile loop record
         *  
         * @param  index the zero-based index of the desired record
         * 
         * @return a {@code List} of the {@code CifFile.CifValue}s
         *         for the removed loop record
         * 
         * @throws IndexOutOfBoundsException if {@code index} is less than
         *         zero or greater than or equal to the number of loop records
         */
        public List<CifValue> removeRecord(int index) {
            int recordSize = getRecordSize();
            
            if (recordSize == 0) {
                throw new IndexOutOfBoundsException("No loop record #" + index);
            } else {
                int start = index * recordSize;
                List<CifValue> record
                        = values.subList(start, start + recordSize);
                List<CifValue> rval = new ArrayList<CifValue>(record);

                markChanged();
                record.clear();
                fireLoopEvent(new LoopEvent(LoopEvent.EventCode.RECORD_REMOVED,
                                            this, index));
                checkEmptied();
                
                return rval;
            }
        }

        /**
         * Creates and returns a {@code List} view of the data values
         * associated with the specified data name in this loop; values appear
         * in the view in the same order that their records appear in the loop,
         * and the view tracks modifications to the loop.  If the specified
         * data name is not in this loop then all operations on the list will
         * throw IllegalStateException.
         * 
         * @param  name a {@code String} containing the data name of
         *         interest; should not be {@code null}
         * 
         * @return an unmodifiable {@code List} view of the values, if any
         *         associated with the specified data name in this loop; this
         *         view tracks the current state of the loop, and its supported
         *         methods will throw {@code IllegalStateException} if invoked
         *         while the specified name is not in the loop 
         */
        public List<CifValue> getValuesForName(final String name) {
            final List<CifValue> loopValues = this.values;
            final List<String> loopLcNames = this.lcaseNames;
            final String dataName = name.toLowerCase();

            return new AbstractList<CifValue>() {
                
                @Override
                public int size() {
                    getOffset();  // test that the data name is in the loop
                    return getRecordCount();
                }
                    
                @Override
                public CifValue get(int index) {
                    return loopValues.get(getValueIndex(index));
                }
                    
                @Override
                public Iterator<CifValue> iterator() {
                    getOffset();  // test that the data name is in the loop
                    return super.iterator();
                }
                    
                private int getValueIndex(int recordIndex) {
                    return ((recordIndex * getRecordSize()) + getOffset());
                }
                    
                private int getOffset() {
                    int offset = loopLcNames.indexOf(dataName);
                        
                    if (offset < 0) {
                        throw new IllegalStateException("no data for '"
                                + name + "' in this loop"); 
                    } else {
                        return offset;
                    }
                }
            };
        }
        
        /**
         * Returns the {@code CifFile.CifValue} associated with the
         * specified (case insensitive) data name in the loop record with the
         * specified zero-based record index
         * 
         * @param  dataName a {@code String} containing the data name for
         *         which a value is requested; should not be {@code null}
         * @param  record the index of the loop record from which a value is
         *         requested
         * 
         * @return the {@code CifFile.CifValue} for the specified data
         *         dame in the specified loop record
         * 
         * @throws IllegalArgumentException if the data name is not present
         *         in this loop
         * @throws IndexOutOfBoundsException if {@code record} is less than
         *         zero or greater than or equal to the number of loop records
         */
        public CifValue getValue(String dataName, int record) {
            int field = lcaseNames.indexOf(dataName.toLowerCase());
            
            if (field < 0) {
                throw new IllegalArgumentException("name '" + dataName
                                                   + "' is not in this loop");
            } else {
                return getValue(field, record);
            }
        }

        /**
         * Returns the {@code CifFile.CifValue} at the specified
         * zero-based field index in the loop record with the specified
         * zero-based record index
         * 
         * @param  column the field index for which a value is requested
         * @param  record the index of the loop record from which a value is
         *         requested
         * 
         * @return the {@code CifFile.CifValue} for the specified data
         *         field in the specified loop record
         * 
         * @throws IndexOutOfBoundsException if {@code column} is less than
         *         zero or greater than or equal to the number of fields (data
         *         names) in this loop, or if {@code record} is less than
         *         zero or greater than or equal to the number of loop records
         */
        CifValue getValue(int column, int record) {
            if ((column < 0) || (column >= getRecordSize())) {
                throw new IndexOutOfBoundsException("Illegal data column: "
                        + column);
            } else if ((record < 0) || (record >= getRecordCount())) {
                throw new IndexOutOfBoundsException("Illegal record number: "
                        + record);
            } else {
                return values.get((record * getRecordSize()) + column);
            }
        }

        /**
         * Sets the value associated with the specified (case insensitive) data
         * name in the loop record with the specified zero-based record index
         * to the specified {@code CifFile.CifValue} 
         * 
         * @param  dataName a {@code String} containing the data name for
         *         which a value is to be set; should not be {@code null}
         * @param  record the index of the loop record in which a value is
         *         to be set
         * @param  value the {@code CifFile.CifValue} for the specified
         *         data name in the specified loop record; should not be
         *         {@code null}
         * 
         * @throws IllegalArgumentException if data name is not present
         *         in this loop
         * @throws IndexOutOfBoundsException if {@code record} is less than
         *         zero or greater than or equal to the number of loop records
         */
        public void setValue(String dataName, int record, CifValue value) {
            int field = lcaseNames.indexOf(dataName.toLowerCase());
            
            if (field < 0) {
                throw new IllegalArgumentException("Data name '" + dataName
                                                   + "' is not in this loop");
            } else {
                setValue(field, record, value);
            }
        }

        /**
         * Sets the value at the specified zero-based field index in the loop
         * record with the specified zero-based record index to the specified
         * {@code CifFile.CifValue}
         * 
         * @param  column the field index for which a value is to be set
         * @param  record the index of the loop record in which a value is
         *         to be set
         * @param  value the {@code CifFile.CifValue} for the specified
         *         field in the specified loop record
         * 
         * @throws IndexOutOfBoundsException if {@code column} is less than
         *         zero or greater than or equal to the number of fields (data
         *         names) in this loop, or if {@code record} is less than
         *         zero or greater than or equal to the number of loop records
         */
        void setValue(int column, int record, CifValue value) {
            if ((column < 0) || (column >= getRecordSize())) {
                throw new IndexOutOfBoundsException("Illegal data column: "
                        + column);
            } else if ((record < 0) || (record >= getRecordCount())) {
                throw new IndexOutOfBoundsException("Illegal record number: "
                        + record);
            } else if (value == null) {
                throw new NullPointerException("The value may not be null");
            } else {
                markChanged();
                values.set((record * getRecordSize()) + column, value);
                fireLoopEvent(new LoopEvent(LoopEvent.EventCode.DATUM_MODIFIED,
                                            this, names.get(column), record));
            }
        }

        /**
         * Returns the current record size of this loop, which is the number
         * of fields / data names it defines
         *  
         * @return the record size of this loop
         */
        public int getRecordSize() {
            return names.size();
        }

        /**
         * Returns the current number of records in this loop
         *  
         * @return the number of records in this loop
         */
        public int getRecordCount() {
            int rsize = getRecordSize();
            
            return (rsize == 0) ? 0 : (values.size() / rsize);
        }

        /**
         * Returns an {@code Iterator} over the records of this loop.  The
         * elements of the iteration are {@code Lists} of
         * {@code CifFile.CifValue} instances representing the values in
         * each record.  The {@code List}s of this iteration can be
         * modified without affecting the underlying loop.  The returned
         * iterator supports item removal, which causes the corresponding loop
         * record to be removed.  The returned iterators are fail-fast with
         * respect to modification to this loop by means other than their own
         * remove() methods   
         * 
         * @return an {@code Iterator} over the records of this loop
         */
        public Iterator<List<CifValue>> recordIterator() {
            
            // a local reference to the values list
            final List<CifValue> loopValues = this.values;
            
            return new Iterator<List<CifValue>>() {
                
                // the index of the first value of the next record 
                private int nextPos = 0;
                
                /*
                 * the record size; may be safely cached because it can't be
                 * changed without invalidating this iterator
                 */
                private int recordSize = getRecordSize();
                
                /*
                 * The most recent record returned by this iterator
                 */
                private List<CifValue> lastRecord = null;
                
                /*
                 * The value that the containing DataLoop's version counter
                 * must match for this iterator to be valid
                 */
                private int dataVersion = DataLoop.this.versionCounter;

                /*
                 * Tests that the containing loop has not been modified without
                 * this iterator's participation
                 */
                private void assertLoopNotModified() {
                    if (dataVersion != DataLoop.this.versionCounter) {
                        throw new ConcurrentModificationException(
                                "LoopData was changed");
                    }
                }

                // see Iterator.hasNext()
                public boolean hasNext() {
                    assertLoopNotModified();
                    return (nextPos < loopValues.size());
                }

                // see Iterator.next()
                public List<CifValue> next() {
                    if (!hasNext()) {  // check for modification happens here
                        throw new NoSuchElementException("No more records");
                    } else {
                        int first = nextPos;
                        int afterLast = first + recordSize;

                        nextPos = afterLast;
                        lastRecord = loopValues.subList(first, afterLast);

                        /*
                         * need a new List in case the record is later
                         * remove()ed
                         */
                        return new ArrayList<CifValue>(lastRecord);
                    }
                }

                // see Iterator.remove()
                public void remove() {
                    assertLoopNotModified();
                    if (lastRecord != null) {
                        markChanged();
                        dataVersion = DataLoop.this.versionCounter;
                        lastRecord.clear();
                        lastRecord = null;
                        nextPos -= recordSize;
                        fireLoopEvent(
                                new LoopEvent(LoopEvent.EventCode.RECORD_REMOVED,
                                              DataLoop.this,
                                              nextPos / recordSize));
                        checkEmptied();
                    } else {
                        throw new IllegalStateException(
                                "next() not invoked on this Iterator");
                    }
                }
            };
        }

        /**
         * Records that a change to this data loop has occurred (addition or
         * removal of data names or values); this enables fail-fast behavior
         * for the record iterator 
         */
        void markChanged() {
            versionCounter++;
        }

        /**
         * Encapsulates a check for whether this loop has been emptied; intended
         * for use <strong>only</strong> immediately after a data name or data
         * value has been removed from the loop; false positives may occur if
         * this method is invoked at any other time. 
         */
        void checkEmptied() {
            if (values.size() == 0) {
                emptied = true;
                fireLoopEvent(new LoopEvent(LoopEvent.EventCode.LOOP_EMPTIED,
                                            DataLoop.this));
            }
        }
        
        /**
         * If this {@code DataLoop} has been flagged as emptied then clears
         * that flag to allow repopulation.  Data names defined for the loop (if
         * any) are not affected.  No effect if this {@code DataLoop} is
         * not flagged as emptied.
         */
        public void reset() {
            emptied = false;
        }
        
        /**
         * Registers a {@code LoopListener} to be notified of
         * {@code LoopEvents} ocurring on this {@code DataLoop}.
         * Listeners may also be asked in advance to accept proposed events
         * that have not yet taken place; currently the only events proposed
         * in advance are data name additions 
         *  
         * @param  listener the {@code LoopListener} to register; should
         *         not be {@code null}
         */
        public void addLoopListener(LoopListener listener) {
            if (listener == null) {
                throw new NullPointerException("listener must not be null");
            }
            synchronized (listeners) {
                listeners.add(listener);
            }
        }

        /**
         * Removes a registered {@code LoopListener} from this
         * {@code DataLoop}; no effect if the listener is not registered
         * with this loop or is {@code null}.  If the listener is registered
         * multiple times with this loop then only one registration is removed. 
         *  
         * @param  listener the {@code LoopListener} to remove
         */
        public void removeLoopListener(LoopListener listener) {
            synchronized (listeners) {
                listeners.remove(listener);
            }
        }

        /**
         * Notifies all registered listeners of the specified
         * {@code LoopEvent}
         *  
         * @param  event the {@code CifFile.LoopEvent} to fire; should not
         *         be {@code null}
         */
        protected void fireLoopEvent(LoopEvent event) {
            if (event == null) {
                throw new NullPointerException("event must not be null");
            }
            synchronized (listeners) {
                for (LoopListener listener : listeners) {
                    listener.loopChanged(event);
                }
            }
        }
        
        /**
         * Proposes the specified {@code LoopEvent} to all registered
         * listeners, or until one rejects it
         *  
         * @param  event the {@code CifFile.LoopEvent} to propose; should
         *         not be {@code null}
         * 
         * @return {@code true} if all listeners accepted the proposal,
         *         {@code false} if any rejected it 
         */
        protected boolean proposeLoopEvent(LoopEvent event) {
            if (event == null) {
                throw new NullPointerException("event must not be null");
            }
            synchronized (listeners) {
                for (LoopListener listener : listeners) {
                    if (!listener.approveLoopChange(event)) {
                        return false;
                    }
                }
            }
            
            return true;
        }
    }

    /**
     * An interface defining the protocol by which {@code CifFile.DataLoop}
     * instances can notify interested listeners about events that ocurr on
     * them.
     *    
     * @author  John C. Bollinger
     * @version 0.9.0
     */
    public interface LoopListener {
        
        /**
         * Receives notification of a {@code LoopEvent}
         * 
         * @param  event a {@code LoopEvent} describing the event that occurred 
         */
        void loopChanged(LoopEvent event);
        
        /**
         * Receives notification of a proposed loop modifcation before the
         * modification is performed, and indicates approval or disapproval of
         * it 
         * 
         * @param  event a {@code LoopEvent} describing the proposed event
         *  
         * @return {@code true} to indication acceptance of the event,
         *         {@code false} to reject it
         */
        boolean approveLoopChange(LoopEvent event);
    }

    /**
     * A class encapsulating information about a modification of a
     * {@code CifFile.DataLoop} object.  Includes a code describing the
     * type of event, a reference to the loop on which the event ocurred, a
     * data name to which the event specifically pertains (if any), and the
     * index of the loop record to which the event specifically pertains (if
     * any).
     * 
     * @author  John C. Bollinger
     * @version 0.9.0
     * 
     * @see DataLoop
     */
    public static class LoopEvent {
        
        /**
         * An enum of the event codes that {@code LoopEvent}s support
         */
        public static enum EventCode {
            
            /**
             * A code for events representing addition of a data name to a loop;
             * the data name added will be available from any
             * {@code LoopEvent} with this code, but such events are not
             * specific to any particular loop record
             */
            NAME_ADDED,
            
            /**
             * A code for events representing removal of a data name from a
             * loop; the data name removed will be available from any
             * {@code LoopEvent} with this code, but such events are not
             * specific to any particular loop record
             */
            NAME_REMOVED,

            /**
             * A code for events representing addition of a record to a loop;
             * the index of the added record will be available from any
             * {@code LoopEvent} with this code, but such events are not
             * specific to any particular data name
             */
            RECORD_ADDED,
            
            /**
             * A code for events representing removal of a record from a loop;
             * the index of the removed record will be available from any
             * {@code LoopEvent} with this code, but such events are not
             * specific to any particular data name
             */
            RECORD_REMOVED,
            
            /**
             * A code for events representing modification of a particular value
             * among the values in a loop; the data name to which the value
             * belongs and the index of the record in which it resides will be
             * available from any {@code LoopEvent} with this code
             */
            DATUM_MODIFIED,
            
            /**
             * A code for events representing removal of the last remaining data
             * name or record of a loop, and normally generated <em>after</em> an
             * event specific to the actual removal that ocurred; such events are
             * not specific to any particular data name or record
             */
            LOOP_EMPTIED
        }
        
        /** The code for this {@code LoopEvent} */
        private final EventCode code;
        
        /**
         * A reference to the {@code DataLoop} on which the event
         * ocurred
         */
        private final DataLoop source;
        
        /** The data name to which this {@code LoopEvent} pertains */
        private final String name;
        
        /**
         * The index of the loop record to which this {@code LoopEvent}
         * pertains
         */
        private final int recordNumber;

        /**
         * Initializes a {@code LoopEvent} with the specified event code,
         * loop event source, data name string, and affected record index
         * 
         * @param  code a {@code LoopEvent.EventCode} representing the specific
         *         nature of this event; should not be {@code null}
         * @param  eventSource the {@code CifFile.DataLoop} on which the
         *         event ocurred; should not be {@code null}
         * @param  affectedName the data name specifically affected by the event
         *         (e.g. added to the loop or removed from it);
         *         {@code null} if there is no such name
         * @param  record the zero-based index of the loop record specifically
         *         affected by the event (e.g. added or removed); less than zero
         *         if there is no such record
         */
        LoopEvent(EventCode code, DataLoop eventSource, String affectedName,
                  int record) {
            if ((code == null) || (eventSource == null)) {
                throw new NullPointerException("Null argument");
            } else {
                this.code = code;
                source = eventSource;
                name = affectedName;
                recordNumber = record;
            }
        }

        /**
         * Initializes a {@code LoopEvent} with the specified event code,
         * loop event source, and data name string, and no specified record
         * index
         * 
         * @param  code a {@code LoopEvent.EventCode} representing the specific
         *         nature of this event
         * @param  eventSource the {@code CifFile.DataLoop} on which the
         *         event ocurred
         * @param  affectedName the data name specifically affected by the event
         *         (e.g. added to the loop or removed from it);
         *         {@code null} if there is no such name
         */
        LoopEvent(EventCode code, DataLoop eventSource, String affectedName) {
            this(code, eventSource, affectedName, -1);
        }

        /**
         * Initializes a {@code LoopEvent} with the specified event code,
         * loop event source, and affected record index, but no specified data
         * name
         * 
         * @param  code a {@code LoopEvent.EventCode} representing the specific
         *         nature of this event
         * @param  eventSource the {@code CifFile.DataLoop} on which the
         *         event ocurred
         * @param  record the zero-based index of the loop record specifically
         *         affected by the event (e.g. added or removed); less than zero
         *         if there is no such record
         */
        LoopEvent(EventCode code, DataLoop eventSource, int record) {
            this(code, eventSource, null, record);
        }

        /**
         * Initializes a {@code LoopEvent} with the specified event code
         * and loop event source, but no data name string or record index
         * 
         * @param  code a {@code LoopEvent.EventCode} representing the specific
         *         nature of this event
         * @param  eventSource the {@code CifFile.DataLoop} on which the
         *         event ocurred
         */
        LoopEvent(EventCode code, DataLoop eventSource) {
            this(code, eventSource, null, -1);
        }

        /**
         * Returns the code for the event represented by this
         * {@code LoopEvent}
         * 
         * @return the event code as a {@code LoopEvent.EventCode}
         */
        public EventCode getEventCode() {
            return code;
        }

        /**
         * Returns a reference to the {@code CifFile.DataLoop} on which
         * the event represented by this {@code LoopEvent} ocurred
         * 
         * @return a reference to the source {@code CifFile.DataLoop}
         */
        public DataLoop getSource() {
            return source;
        }

        /**
         * Returns the data name to which this {@code LoopEvent} pertains,
         * or {@code null} if there is none
         * 
         * @return a {@code String} containing the data name, or
         *         {@code null}
         */
        public String getDataName() {
            return name;
        }

        /**
         * Returns the index of the loop record to which this
         * {@code LoopEvent} pertains
         * 
         * @return the record index; &lt; 0 if there is no specific record 
         */
        public int getRecordNumber() {
            return recordNumber;
        }
    }

    /**
     * An interface describing the requirements for objects representing data
     * values in a {@code CifFile}.  This interface defines no methods not
     * already implemented in {@code java.lang.Object}, but it does
     * describe a narrower contract for the {@code toString()} method.
     * 
     * @author John C. Bollinger
     * @version 0.9.0
     */
    public interface CifValue {
        
        /**
         * Returns a {@code String} representation of this
         * {@code CifValue}.  The returned string is of suitable form for
         * inclusion in a CIF as a data value.  In particular, values of type
         * "char" or "uchar" will be quoted, if necessary, and values of type
         * "numb" will be formatted appropriately.  This method does not take
         * line length into account, and if it must insert a line terminator
         * (e.g. when terminating a multi-line text block) then it uses the
         * local system's line termintion sequence. 
         * 
         * @return a {@code String} representation of this
         *         {@code CifValue} in a form suitable for inclusion as a
         *         CIF data value
         */
        String toString();
    }
}
