/*
 * Reciprocal Net Project
 *
 * @(#) Rotate3DModel.java
 *
 * 16-Oct-2002: jobollin added static method readModel as part of task 510
 * 20-Nov-2002: jobollin moved this class to org.recipnet.site.misc from *.jamm
 * 20-Feb-2003: jobollin removed Java 2 dependencies, specifically dependencies
 *              on java.io.Reader (replaced with java.io.InputStream) as part
 *              of task #682
 * 21-Feb-2003: jobollin reformatted the source code according to convention
 *              and added additional javadoc comments (part of task #746)
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.misc package to
 *              org.recipnet.site.applet.jamm
 * 06-Jan-2005: jobollin removed usage of the deprecated StreamTokenizer
 *              constructor
 */

package org.recipnet.site.applet.jamm;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import org.recipnet.site.wrapper.FileFormatException;

/**
 * encapsulates arrays of three-dimensional coordinates and of connections
 * among those coordinates; each coordinate triple may also have a type and a
 * label associated with it.  A Rotate3DModel object can calculate and return
 * its own center of mass and radius about that center.  This class maps
 * fairly directly onto a Reciprocal Net CRT file, and serves as the data
 * model for org.recipnet.site.jamm.Rotate3DPanel
 * <p>
 * This class is Java 1.1 compatible.
 * </p><p>
 * This class is not thread-safe.  If used in a multi-threaded environment then
 * access to it must be synchronized externally.
 * </p>
 */
public class Rotate3DModel {

    /**
     * A connection between nothing and nothing, used to populate unused slots
     * in the connection array
     */
    private static int[] nullCon;

    static {
        nullCon = new int[2];
        nullCon[0] = nullCon[1] = -1;
    }

    /** the connections in this model, as pairs of vertex indices */
    protected int[][] con;

    /** the labels for the vertices of this model */
    protected String[] label;

    /** the <code>int</code> types of the vertices of this model */
    protected int[] type;

    /**
     * the number of connections in this model; may be different from
     * <code>con.length</code>
     */
    protected int nCon;

    /**
     * the number of vertices in this model; may be different from
     * <code>vert.length</code>
     */
    protected int nVert;

    /** tracks those classes registered as event listeners on this object */
    EventListenerList elList;

    /** the vertices of this model, as coordinate triples */
    private float[][] vert;

    /**
     * flags whether the derived parameters have been recalculated since the
     * last change to the model
     */
    private boolean upToDate;

    /**
     * The calculated radius of the model; valid only if <code>upToDate</code>
     * is <code>true</code>
     */
    private float radius;

    /**
     * The calculated mean x coordinate of the model vertices; valid only if
     * <code>upToDate</code> is <code>true</code>
     */
    private float xMean;

    /**
     * The calculated mean y coordinate of the model vertices; valid only if
     * <code>upToDate</code> is <code>true</code>
     */
    private float yMean;

    /**
     * The calculated mean z coordinate of the model vertices; valid only if
     * <code>upToDate</code> is <code>true</code>
     */
    private float zMean;

    /**
     * Creates a new, empty <code>Rotate3DModel</code>
     */
    public Rotate3DModel() {
        vert = null;
        type = null;
        label = null;
        con = null;
        nVert = 0;
        nCon = 0;
        xMean = yMean = zMean = radius = 0f;
        upToDate = true;
        elList = new EventListenerList();
    }

    /**
     * Adds a connection from vertex number <code>p1</code> to vertex number
     * <code>p2</code>; the referenced vertices must already be in the model
     *
     * @param  p1 the index of the vertex at one end of the connection
     * @param  p2 the index of the vertex at the other end of the connection
     */
    public final void addCon(int p1, int p2) {
        if ((p1 >= nVert) || (p2 >= nVert)) {
            return;
        }
        if (con == null) {
            con = new int[50][0];
            for (int i = 0; i < con.length; i++) {
                con[i] = nullCon;
            }
        } else if (nCon >= con.length) {
            int newLength = con.length * 2;
            int[][] nv = new int[newLength][2];
            System.arraycopy(con, 0, nv, 0, con.length);
            for (int i = con.length; i < newLength; i++) {
                nv[i] = nullCon;
            }
            con = nv;
        }
        if (p1 > p2) {
            int t = p1;
            p1 = p2;
            p2 = t;
        }
        int[] tcon = new int[2];
        tcon[0] = p1;
        tcon[1] = p2;
        con[nCon++] = tcon;
        fireModelEvent(new ModelEvent(this, ModelEvent.CONNECTIONS_MASK));
    }

    /**
     * Adds a vertex to this model
     *
     * @param  x the x coordinate of the new vertex as a <code>float</code>
     * @param  y the y coordinate of the new vertex as a <code>float</code>
     * @param  z the z coordinate of the new vertex as a <code>float</code>
     * @param  num the type index of the new vertex
     * @param  lab a <code>String</code> containing the label of the new vertex
     */
    public final void addVert(float x, float y, float z, int num,
            String lab) {
        int l;

        if (vert == null) {
            vert = new float[100][3];
            type = new int[100];
            label = new String[100];
        } else if (nVert >= (l = vert.length)) {
            l *= 2;
            float[][] nv = new float[l][3];
            int[] nt = new int[l];
            String[] nl = new String[l];
            System.arraycopy(vert, 0, nv, 0, vert.length);
            System.arraycopy(type, 0, nt, 0, type.length);
            System.arraycopy(label, 0, nl, 0, label.length);
            vert = nv;
            type = nt;
            label = nl;
        }
        vert[nVert][0] = x;
        vert[nVert][1] = y;
        vert[nVert][2] = z;
        type[nVert] = num;
        label[nVert] = lab;
        nVert++;
        upToDate = false;
        fireModelEvent(new ModelEvent(this, ModelEvent.VERTICES_MASK));
    }

    /**
     * Adds a vertex of default type to this model
     *
     * @param  x the x coordinate of the new vertex as a <code>float</code>
     * @param  y the y coordinate of the new vertex as a <code>float</code>
     * @param  z the z coordinate of the new vertex as a <code>float</code>
     * @param  lab a <code>String</code> containing the label of the new vertex
     */
    public void addVert(float x, float y, float z, String lab) {
        addVert(x, y, z, 0, lab);
    }

    /**
     * Adds an unlabeled vertex to this model
     *
     * @param  x the x coordinate of the new vertex as a <code>float</code>
     * @param  y the y coordinate of the new vertex as a <code>float</code>
     * @param  z the z coordinate of the new vertex as a <code>float</code>
     * @param  num the type index of the new vertex
     */
    public void addVert(float x, float y, float z, int num) {
        addVert(x, y, z, num, "");
    }

    /**
     * Adds an unlabeled vertex of default type to this model
     *
     * @param  x the x coordinate of the new vertex as a <code>float</code>
     * @param  y the y coordinate of the new vertex as a <code>float</code>
     * @param  z the z coordinate of the new vertex as a <code>float</code>
     */
    public void addVert(float x, float y, float z) {
        addVert(x, y, z, 0, "");
    }

    /**
     * Returns the x coordinate of the model center -- the arithmetic mean of
     * the x coordinates of all the vertices in the model
     *
     * @return the x coordinate of the centroid of the model
     */
    public float getCenterX() {
        if (!upToDate) {
            derive();
        }
        return xMean;
    }

    /**
     * Returns the y coordinate of the model center -- the arithmetic mean of
     * the y coordinates of all the vertices in the model
     *
     * @return the y coordinate of the centroid of the model
     */
    public float getCenterY() {
        if (!upToDate) {
            derive();
        }
        return yMean;
    }

    /**
     * Returns the z coordinate of the model center -- the arithmetic mean of
     * the z coordinates of all the vertices in the model
     *
     * @return the z coordinate of the centroid of the model
     */
    public float getCenterZ() {
        if (!upToDate) {
            derive();
        }
        return zMean;
    }

    /**
     * returns the connections array for this model
     * 
     * @return an <code>int[][]</code> containing the connections as pairs of
     *         vertex indices
     * 
     * @see #getNumConnections() getNumConnections
     */
    public int[][] getConnections() {
        return con;
    }

    /**
     * Returns the number of connections in the model, which may differ from
     * <code>getConnections().length</code>
     *
     * @return the number of connections in the model
     * 
     * @see #getConnections() getConnections
     */
    public int getNumConnections() {
        return nCon;
    }

    /**
     * Returns the number of points (vertices) in the model
     *
     * @return the number of points in the model
     */
    public int getNumPoints() {
        return nVert;
    }

    /**
     * Returns an array of the labels of all the points in the model
     *
     * @return a <code>String[]<code> containing the point labels for this model
     */
    public String[] getPointLabels() {
        return label;
    }

    /**
     * Returns an array of the type codes for all the points in the model
     *
     * @return an <code>int[]</code> containing the point types for this model
     */
    public int[] getPointTypes() {
        return type;
    }

    /**
     * Returns all the points of this model as an array of <code>float</code>
     * coordinate triples
     *
     * @return a <code>float[][]</code> containing the coordinates of all the
     *         points in the model
     */
    public float[][] getPoints() {
        return vert;
    }

    /**
     * Returns the radius of the model, defined as the largest distance between
     * any point in the model and the unweighted centroid.  The model will
     * fit inside a sphere of this radius centered on the centroid, for any
     * orientation of the model.
     *
     * @return the radius of the model
     */
    public float getRadius() {
        if (!upToDate) {
            derive();
        }
        return radius;
    }

    /**
     * Calculates and returns the distance between the two specified points
     *
     * @param  p1 the index of the first point
     * @param  p2 the index of the second point
     *
     * @return the distance between the two specified points as a
     *         <code>float</code>
     */
    public float findDistance(int p1, int p2) {
        if ((p1 >= nVert) || (p2 >= nVert)) {
            return -1f;
        }

        double dx = vert[p1][0] - vert[p2][0];
        double dy = vert[p1][1] - vert[p2][1];
        double dz = vert[p1][2] - vert[p2][2];
        
        return (float) Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
    }

    /**
     * reads model data in CRT format from the provided InputStream and uses it
     * to construct a new Rotate3DModel
     *
     * @param  input the <code>Reader</code> from which to obtain the model
     *         data.  It should be buffered for good performance.
     *
     * @return a <code>Rotate3DModel</code> constructed from the input data, or
     *         null if an I/O or file format exception is raised during the
     *         processing (such an exception is not passed on)
     *
     * @throws FileFormatException if the data obtained from <code>input</code>
     *         contains errors or is not in CRT format at all
     * @throws IOException if one is encountered while reading the input
     */
    public static Rotate3DModel readModel(Reader input)
            throws FileFormatException, IOException {
        Rotate3DModel mod;

        /* set up a stream tokenizer on the input */
        StreamTokenizer st = new StreamTokenizer(input);
        st.ordinaryChars('\u0021', '\uffff');
        st.wordChars('\u0021', '\uffff');
        st.eolIsSignificant(true);
        st.commentChar('#');
        st.parseNumbers();

        /* Prepare an empty model */
        mod = new Rotate3DModel();

        /* Read the model input via the tokenizer */
        int num = 0;
        st.nextToken();
        scan: 
        while (true) {
            switch (st.ttype) {
            default:
                break scan;
            case StreamTokenizer.TT_EOL:
                break;
            case StreamTokenizer.TT_WORD:
                if (st.sval.compareTo("CARTESIAN") == 0) {
                    while ((st.ttype != StreamTokenizer.TT_EOL)
                            && (st.ttype != StreamTokenizer.TT_EOF)) {
                        st.nextToken();
                    }
                    st.nextToken();
                    double x;
                    double y;
                    double z;
                    String label;
                    do {
                        label = st.sval;
                        x = 0d;
                        y = 0d;
                        z = 0d;
                        if (st.nextToken() == StreamTokenizer.TT_NUMBER) {
                            x = st.nval;
                            if (st.nextToken() == StreamTokenizer.TT_NUMBER) {
                                y = st.nval;
                                if (st.nextToken() == StreamTokenizer.TT_NUMBER) {
                                    z = st.nval;
                                } else {
                                    throw new BadAtomException(label);
                                }
                            } else {
                                throw new BadAtomException(label);
                            }
                            if (st.nextToken() == StreamTokenizer.TT_NUMBER) {
                                num = (int) st.nval;
                            } else {
                                throw new BadAtomException(label);
                            }
                        } else {
                            throw new BadAtomException(label);
                        }
                        mod.addVert((float) x, (float) y, (float) z, num, label);
                        while ((st.ttype != StreamTokenizer.TT_EOL)
                                && (st.ttype != StreamTokenizer.TT_EOF)) {
                            st.nextToken();
                        }
                        if (st.ttype == StreamTokenizer.TT_EOF) {
                            break scan;
                        }
                        st.nextToken();
                    } while (st.sval.compareTo("ENDATOMS") != 0);
                } else if (st.sval.compareTo("ENDATOMS") == 0) {
                    while ((st.ttype != StreamTokenizer.TT_EOL)
                            && (st.ttype != StreamTokenizer.TT_EOF)) {
                        st.nextToken();
                    }
                    if (st.ttype == StreamTokenizer.TT_EOF) {
                        break scan;
                    }
                    int n;

                    while (st.nextToken() == StreamTokenizer.TT_NUMBER) {
                        n = (int) st.nval;
                        if (st.nextToken() != StreamTokenizer.TT_NUMBER) {
                            throw new BadBondException("bond"
                                + mod.getNumConnections());
                        }
                        mod.addCon(n - 1, (int) st.nval - 1);
                        st.nextToken();
                    }
                    break scan;
                } else if (st.sval.compareTo("ENDBONDS") == 0) {
                    break scan;
                } else {
                    while ((st.nextToken() != StreamTokenizer.TT_EOL)
                            && (st.ttype != StreamTokenizer.TT_EOF)) {
                        // do nothing
                    }
                    st.nextToken();
                }
            }
        }
        return mod;
    }

    /**
     * Sends a <code>ModelEvent</code> to all listeners registered to receive
     * such events.  A generic <code>ModelEvent</code> is supplied if
     * <code>modelEvent</code> is <code>null</code>.
     * 
     * @param  modelEvent the <code>ModelEvent</code> to fire
     */
    protected void fireModelEvent(ModelEvent modelEvent) {
        synchronized (elList) {
            // Guaranteed to return a non-null array
            Object[] listeners = elList.getListenerList();

            // Process the listeners last to first, notifying
            // those that are interested in this event
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == ModelListener.class) {
                    // Lazily create the event:
                    if (modelEvent == null) {
                        modelEvent = new ModelEvent(this);
                    }
                    ((ModelListener) listeners[i + 1]).modelChanged(modelEvent);
                }
            }
        }
    }

    /**
     * Registers the <code>ml</code> to receive <code>ModelEvents</code> from
     * this <code>Rotate3DModel</code>.  Events are fired when a new vertex or
     * connection is added to the model, and when the centroid and radius are
     * re-derived.
     * 
     * @param  ml a <code>ModelListener</code> that should notified of
     *         <code>ModelEvent</code>s that occur on this
     *         <code>Rotate3DModel</code>
     */
    void addModelListener(ModelListener ml) {
        elList.add(ModelListener.class, ml);
    }

    /**
     * De-registers <code>ml</code> for reception of <code>ModelEvents</code>
     * events from this object
     * 
     * @param  ml a <code>ModelListener</code> that should no longer be notified
     *         of <code>ModelEvent</code>s that occur on this
     *         <code>Rotate3DModel</code>
     */
    void removeModelListener(ModelListener ml) {
        elList.remove(ModelListener.class, ml);
    }

    /**
     * Calculates derived model parameters (radius and unweighted centroid) and
     * marks this object as being up to date in that regard
     */
    private void derive() {
        /* find the unweighted center of mass */
        double xt = 0d;
        double yt = 0d;
        double zt = 0d;
        
        for (int i = 0; i < nVert; i++) {
            xt += vert[i][0];
            yt += vert[i][1];
            zt += vert[i][2];
        }
        xMean = (float) (xt / nVert);
        yMean = (float) (yt / nVert);
        zMean = (float) (zt / nVert);

        /* find the radius */
        float dx;
        float dy;
        float dz;
        radius = 0f;
        for (int i = 0; i < nVert; i++) {
            dx = vert[i][0] - xMean;
            dy = vert[i][1] - yMean;
            dz = vert[i][2] - zMean;
            radius =
                (float) Math.max(radius,
                    Math.sqrt((dx * dx) + (dy * dy) + (dz * dz)));
        }
        upToDate = true;
        fireModelEvent(new ModelEvent(this, ModelEvent.DERIVED_MASK));
    }
}
