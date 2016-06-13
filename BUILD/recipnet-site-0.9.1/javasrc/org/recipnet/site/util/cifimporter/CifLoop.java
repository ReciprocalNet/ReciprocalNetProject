/*
 * Reciprocal Net project
 * 
 * CifLoop.java
 *
 * 06-Jun-2002: jobollin wrote skeleton
 * 10-Jun-2002: jobollin wrote first draft
 * 16-Feb-2004: jobollin updated version number format in class javadocs and
 *              removed an unused import of java.util.Map
 * 09-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.util.cifimporter;

import java.util.ArrayList;
import java.util.List;

/**
 * a class that abstracts a CIF loop construct.  It provides methods for
 * populating instances and later retrieving the data names and values.
 * {@code CifLoop} objects perform validation checks both when being
 * populated and when being read.
 * <p>
 * The main validation check that occurs at read time is that the number of
 * data values must be a positive integer multiple of the number of data names
 * (per the CIF specification).  This criterion may not be satisfied at some
 * points during the population of a CifLoop object, but must be satisfied
 * before data names or values can be retrieved from one.
 * <p>
 * At population time, a {@code CifLoop} object requires that all data
 * names be added first, and then all data values.  (This corresponds to the
 * order in which the names and values appear on the CIF.)  An
 * {@code IllegalStateException} is thrown if that restriction is
 * violated.
 * <p>
 * A {@code CifLoop} object associates data values with names and
 * associates multiple data values into records based on the order in which
 * the names and values are added to the {@code CifLoop} object.  This is
 * the approach mandated for a CIF processor by the CIF specification.
 * <p>
 * This object is not synchronized.
 *
 * @author John C. Bollinger
 * @version 0.6.2
 */
public class CifLoop {

    /**
     * a {@link java.util.List List} of the data names specified in the loop
     * header, in order of appearance
     */
    protected List<String> dataNames;

    /**
     * a {@link java.util.List List} of all the data values in the loop (data
     * for all data names) in order of appearance
     */
    protected List<Object> dataValues;

    /**
     * an internal indicator of loop validity.  Possible values are
     * {@code NOT_VALIDATED}, {@code VALID},
     * {@code NO_DATA_NAMES}, {@code NO_DATA_VALUES},
     * and {@code PARTIAL_RECORD}.  The latter three values indicate
     * invalid conditions.
     */
    protected int validationStatus;

    /**
     * a value indicating that this {@code CifLoop}'s validationStatus
     * needs to be redetermined
     */
    public final static int NOT_VALIDATED = 0;

    /**
     * a value indicating that this {@code CifLoop} contains valid data
     */
    public final static int VALID = 1;

    /**
     * a value indicating that this {@code CifLoop} contains data invalid
     * because no data names are present.  This is the initial validationStatus
     * of a new {@code CifLoop} instance.
     */
    public final static int NO_DATA_NAMES = 2;

    /**
     * a value indicating that this {@code CifLoop} contains data invalid
     * because only data names are present, not data values.
     */
    public final static int NO_DATA_VALUES = 3;

    /**
     * a value indicating that this {@code CifLoop} contains data invalid
     * because although both data names and data values are present, the number
     * of data values is not an integer multiple of the number of data names
     */
    public final static int PARTIAL_RECORD = 4;

    /**
     * constructs an empty {@code CifLoop}.
     */
    public CifLoop() {
        dataNames = new ArrayList<String>();
        dataValues = new ArrayList<Object>();
        validationStatus = NO_DATA_NAMES;
    }

    /**
     * adds a data name to this {@code CifLoop}.  This method should be
     * invoked repeatedly to add all data names before {@link #addDataValue
     * addDataValue} is used to add any data values.
     *
     * @param name a {@code String} containing the data name to add.  It
     * is not checked for CIF validity
     * @throws IllegalStateException if this data name is already in the loop
     * or if any data values have already been added.
     */
    public void addDataName(String name) {
        if (dataValues.size() != 0)
            throw new IllegalStateException(
                    "Cannot add data name "
                    + name
                    + " to loop containing data values.");
        if (dataNames.contains(name))
            throw new IllegalStateException(
                    "Cannot add data name "
                    + name
                    + " to loop twice.");
        dataNames.add(name);
        validationStatus = NOT_VALIDATED;
    }

    /**
     * adds a data value to this {@code CifLoop}.  This method should be
     * invoked repeatedly to add all data values, but only after all data names
     * have been added with {@link #addDataName addDataName}.
     *
     * @param value an {@code Object} representing the data value for the
     * next position in this {@code CifLoop}
     *
     * @throws IllegalArgumentException if for any reason the argument is not
     * acceptable.  (For example, {@code value} is not of the correct
     * runtime type for the next position in the current loop record.  Note,
     * however, that the current implementation of this method does not perform
     * type checking, or, for that matter, any argument checking at all.)
     * @throws IllegalStateException if no data names have been added
     */
    public void addDataValue(Object value) {
        if (dataNames.size() == 0)
            throw new IllegalStateException(
                    "Cannot add data value "
                    + value
                    + " to loop with no data names.");
        /* implementation note: no value checking yet */
        dataValues.add(value);
        validationStatus = NOT_VALIDATED;
    }

    /**
     * determines whether this CifLoop is in a valid state.  A valid state
     * consists of this {@code CifLoop} containing at least one data name
     * and a number of data values that is a positive integer multiple of the
     * number of data names.
     *
     * @return one of {@code VALID}, {@code NO_DATA_NAMES},
     * {@code NO_DATA_VALUES}, or {@code PARTIAL_RECORD}
     */
    public int checkValidity() {
        if (validationStatus == NOT_VALIDATED) {
            int nn = dataNames.size();
            int nv = dataValues.size();
            if (nn == 0) validationStatus = NO_DATA_NAMES;
            else if (nv == 0) validationStatus = NO_DATA_VALUES;
            else if (nv % nn != 0) validationStatus = PARTIAL_RECORD;
            else validationStatus = VALID;
        }
        return validationStatus;
    }

    /**
     * returns a the data name at position {@code index}.
     *
     * @param index the position in the {@code List} of data names of the
     * name to return
     *
     * @return a {@code String} containing the requested data name
     *
     * @throws IllegalStateException if this object is not and cannot be
     * validated
     * @throws IndexOutOfBoundsException if index does not correspond to a
     * valid position in the {@code List} of data names
     */
    public String getDataName(int index) {
        if (checkValidity() != VALID)
            throw new IllegalStateException("Loop data not valid");
        return dataNames.get(index);
    }

    /**
     * returns a copy of the complete {@code List} of data names
     *
     * @return a {@code List} containing a <em>copy</em> of the current
     * {@code List} of data names
     *
     * @throws IllegalStateException if this object is not and cannot be
     * validated
     */
    public List<String> getDataNames() {
        if (checkValidity() != VALID)
            throw new IllegalStateException("Loop data not valid");
        return new ArrayList<String>(dataNames);
    }

    /**
     * returns a {@code List} containing all the data items for the
     * specified data name in this {@code CifLoop}
     *
     * @param name a {@code String} containing the data name
     *
     * @throws IllegalArgumentException if the specified name is not among the
     * data names in this {@code CifLoop}
     * @throws IllegalStateException if this object is not and cannot be
     * validated
     */
    public List<Object> getDataForName(String name) {
        if (checkValidity() != VALID)
            throw new IllegalStateException("Loop data not valid");
        int offset = dataNames.indexOf(name);
        if (offset == -1)
            throw new IllegalArgumentException(
                    "Loop does not contain data name " + name);
        int nn = dataNames.size();
        int nv = dataValues.size();
        ArrayList<Object> data = new ArrayList<Object>();
        for ( ; offset <= nv; offset += nn) {
            data.add( dataValues.get(offset) );
        }
        return data;
    }

    /**
     * returns the {@code index}th data value for the specified data name
     * in this {@code CifLoop}
     *
     * @param name a {@code String} containing the data name
     * @param index the record number from which to select the data value
     *
     * @throws IllegalArgumentException if {@code name} is not among the
     * data names in this loop
     * @throws IllegalStateException if this {@code CifLoop} is not and
     * cannot be validated
     * @throws IndexOutOfBoundsException if {@code index} is greater than
     * or equal to the number of records, or if it is less than zero
     */
    public Object getDatumForName(String name, int index) {
        if (checkValidity() != VALID)
            throw new IllegalStateException("Loop data not valid");
        int offset = dataNames.indexOf(name);
        if (offset == -1)
            throw new IllegalArgumentException(
                    "Loop does not contain data name " + name);
        return dataValues.get(offset + dataNames.size() * index);
    }

    /**
     * returns the number of records in this {@code CifLoop}.
     *
     * @return the number of records
     *
     * @throws IllegalStateException if this {@code CifLoop} is not and
     * cannot be validated
     */
    public int getNumRecords() {
        if (checkValidity() != VALID)
            throw new IllegalStateException("Loop data not valid");
        return dataValues.size() / dataNames.size();
    }

    /**
     * returns the data values for the {@code index}th record as a
     * {@link java.util.List List}.  The {@code List} returned by
     * {@code getRecord} will be the same length as that returned by
     * {@code getDataNames}, and the items in the two {@code List}s
     * will correspond on an item by item basis.
     *
     * @param index the record number requested
     *
     * @return a {@code List} containing the record data
     *
     * @throws IndexOutOfBoundsException if index is greater than or equal to
     * the number of records, or if it is less than zero
     * @throws IllegalStateException if this {@code CifLoop} is not and
     * cannot be validated
     */
    public List<Object> getRecord(int index) {
        if (checkValidity() != VALID)
            throw new IllegalStateException("Loop data not valid");
        int nn = dataNames.size();
        int offset = nn * index;
        ArrayList<Object> record = new ArrayList<Object>(
            dataValues.subList(offset, offset + nn)
        );
        return record;
    }

    /**
     * Returns a string representation of this object
     */
    public String toString() {
        return "(looped)";
    }
}

