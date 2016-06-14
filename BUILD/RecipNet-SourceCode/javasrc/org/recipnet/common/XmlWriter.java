/*
 * Reciprocal Net project
 * 
 * XmlWriter.java
 * 
 * 06-Oct-2005: jobollin wrote first draft
 * 10-May-2006: jobollin performed small documentation tweaks
 */

package org.recipnet.common;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

/**
 * A class of objects that assist in writing data to an output stream in XML
 * format.  This class is fairly simplistic; for complex XML, it might be
 * advisable to build a DOM tree instead.  In particular, users should note
 * that though this class helps produce well-formed and even valid XML, it does
 * not <em>ensure</em> even well-formedness: it accepts arbitrary element
 * and attribute names, does not prevent multiple root elements, and, moreover,
 * contains a mechanism for including arbitrary text in the output.
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class XmlWriter {
    
    /**
     * An enum of XML versions known to the Reciprocal Net software
     * 
     * @author jobollin
     * @version 0.9.0
     */
    public enum XmlVersion {
        
        /**
         * The {@code XmlVersion} representing <a
         * href="http://www.w3.org/TR/2004/REC-xml-20040204/">XML 1.0</a>
         */
        XML_1_0("1.0"),
        
        /**
         * The {@code XmlVersion} representing <a
         * href="http://www.w3.org/TR/2004/REC-xml11-20040204/">XML 1.1</a>
         */
        XML_1_1("1.1");
        
        private final String code;
        
        XmlVersion(String versionCode) {
            code = versionCode;
        }
        
        /**
         * Returns the version code for this XML version, suitable for use as
         * the value of the {@code version} attribute of an XML declaration
         * 
         * @return the version code {@code String}
         */
        public String getCode() {
            return code;
        }
    }

    /**
     * The {@code XmlVersion} object representing the XML version being output
     * by this object
     */
    private final XmlVersion xmlVersion;
    
    /**
     * The {@code PrintWriter} to which the output of this {@code XmlWriter} is
     * directed
     */
    private final PrintWriter writer;
    
    /**
     * A flag indicating whether this writer is currently writing a start tag
     * (in which case attributes can be added) or not
     */
    private boolean inStartTag = false;

    /**
     * The default relative indentation depth for the contents of new elements
     * created by this writer, or negative for no indentation or line break
     */
    private int defaultIndent = -1;
    
    /**
     * The current element context; that is, the one currently governing content
     * being written to this writer.  It is not (yet) on the context stack. 
     */
    private ElementContext currentContext = new ElementContext(null, 0, 0);
    
    /**
     * A {@code List} of element contexts that logically encompass the current
     * context (i.e. the contexts associated with the current context's parent
     * and ancestor elements, and the document itself).  This list is used as a
     * stack, with its top at index 0. 
     */
    private final List<ElementContext> contextStack =
            new LinkedList<ElementContext>();

    /**
     * Initializes an {@code XmlWriter} with the specified underlying stream,
     * XML version, and character encoding; an XML declaration is written to
     * the provided stream as part of this process.  Default indentation is
     * intially disabled on the new writer, but may be enabled via
     * {@link #setDefaultIndent(int)}.
     * 
     * @param  stream the {@code OutputStream} to which the output of this
     *         object should be directed; users may want to consider providing
     *         a buffered stream, as this object does not perform internal
     *         buffering
     * @param  version the {@code XmlVersion} that this writer should produce;
     *         in this version it only affects the XML declaration
     * @param  encoding the name of the character encoding for this document;
     *         it will be output in the XML declaration and used to encode all
     *         text and markup written via this object
     *          
     * @throws UnsupportedEncodingException if the specified encoding is not
     *         supported by the VM
     */
    public XmlWriter(OutputStream stream, XmlVersion version, String encoding)
            throws UnsupportedEncodingException {
        xmlVersion = version;
        writer = new PrintWriter(new OutputStreamWriter(stream, encoding));
        writer.print("<?xml version=\"");
        writer.print(version.getCode());
        writer.print("\" encoding=\"");
        writer.print(encoding);
        writer.println("\" ?>");
    }
    
    /**
     * Returns the xml version that this writer was instructed to use
     * 
     * @return the xml version as an {@code XmlVersion} object
     */
    public XmlVersion getXmlVersion() {
        return xmlVersion;
    }

    /**
     * <p>
     * Sets the relative indentation level initially configured on new elements
     * when no explicit indentation level is specified (i.e. those created via
     * {@link #openElement(String)} rather than by
     * {@link #openElement(String, int)}.  See {@link #defaultIndent} for more
     * information.
     * </p><p>
     * This method does not affect the indentation level used by the current
     * element; for that use {@link #setCurrentIndent(int)}.
     * </p>
     * 
     * @param  indent the number of characters to indent, if non-negative, or
     *         negative to turn off indentation altogether
     */
    public void setDefaultIndent(int indent) {
        this.defaultIndent = indent;
    }
    
    /**
     * <p>
     * Sets the number of space characters to used to indent each child element
     * of the current element, relative to this element's own indentation.  This
     * level of indentation is also applied when {@link #addLineBreak()} is
     * invoked directly inside the current element.  An indent of zero produces
     * no relative indentation, but does still cause child elements to be
     * opened on a new line, at the same indentation level as the current
     * element.  The indentation level can be changed after the current
     * element's content is already partially written, and it will then affect
     * new content added after that point.  Users should be aware that the
     * initial context is not treated specially in this regard. This method
     * does not affect the indentation level used by elements other than the
     * current one.
     * </p><p><em>Note: this indentation feature should be disabled inside
     * elements with character or mixed content by setting a negative indent, as
     * whitespace in the output document is significant in those cases.  It may
     * be more convenient to open such elements with {@link #openElement(String,
     * int)} than to seperately invoke this method, however.</em>
     * </p>
     * 
     * @param  indent the number of characters to indent, if non-negative, or
     *         negative to turn off indentation altogether
     *         
     * @see #setDefaultIndent(int)
     */
    public void setCurrentIndent(int indent) {
        currentContext.setIncrement(indent);
    }
    
    /**
     * Commences output of a new element as the child of the previous element
     * among those opened but not yet closed that was opened most recently.  The
     * initial portion of the start tag is written to the output (first
     * completing any previous start tag if necessary), and attributes for it
     * may be written via {@link #addAttribute(String, String)} until text or a
     * child element is added, or the element is closed.  The new element will
     * use the default indentation increment set for this writer.  
     *
     * @param  name the element name
     */
    public void openElement(String name) {
        openElement(name, defaultIndent);
    }

    /**
     * Commences output of a new element just as does
     * {@link #openElement(String)}, but uses the specified indentation
     * increment for the element's children instead of this writer's default.
     * This may be useful to output a non-indenting element inside an otherwise
     * indented document. 
     *
     * @param  name the element name
     * @param  elementIndentIncrement the indent increment for the new element 
     */
    public void openElement(String name, int elementIndentIncrement) {
        if (name == null) {
            throw new NullPointerException("Null element name");
        } else if (inStartTag) {
            finishStartTag();
        }
        if (currentContext.isIndenting()) {
            currentContext.printNewLine(writer);
        }
        writer.print('<');
        writer.print(name);
        inStartTag = true;
        contextStack.add(0, currentContext);
        currentContext = ElementContext.deriveFrom(
                currentContext, name, elementIndentIncrement);
    }
    
    /**
     * <p>
     * Adds an attribute to the currently open start tag. This method should be
     * invoked after {@link #openElement(String)} and before any subsequent
     * {@link #addText(String)}, {@link #addLiteralText(String)},
     * {@link #closeElement()}, {@link #closeAllElements()}, or {@link #close()}
     * </p><p>
     * The specified value is processed to escape those characters that may not
     * appear as literals in attribute values; it should not be manually escaped
     * by the user.
     * </p>
     * 
     * @param  name the name of the attribute to add
     * @param  value the value for the attribute
     * @throws IllegalStateException if there is no start tag available to add
     *             the attribute to.
     */
    public void addAttribute(String name, String value) {
        if (name == null) {
            throw new NullPointerException("Null attribute name");
        } else if (!inStartTag) {
            throw new IllegalStateException("No start tag to add attribute to");
        } else {
            writer.print(' ');
            writer.print(name);
            writer.print("=\"");
            writer.print(escapeAttributeString(value));
            writer.print("\"");
        }
    }

    /**
     * Replaces characters that must not appear literally in XML attribute
     * values with corresponding entity references, as described in
     * <a href="http://www.w3.org/TR/2004/REC-xml11-20040204/#syntax">section
     * 2.4 of the XML spec</a>, and similarly replaces single and double quotes
     * with their corresponding entity references to simplify attribute value
     * processing.  The characters so treated are the same as handled
     * by {@link #escapeElementString(String)}, plus the apostrophe and double
     * quote characters.
     * 
     * @param  attr the data to be processed, which should be an
     *         <em>unquoted</em> attribute value; any markup (including
     *         erstwhile entity references) in this string will be escaped as
     *         entity references
     *           
     * @return a string similar to {@code attr} in which the characters that
     *         should not appear unescaped in XML attribute values have been
     *         replaced with corresponding XML entity references
     */
    private String escapeAttributeString(String attr) {
        return escapeElementString(attr).replaceAll("'", "&apos;").replaceAll(
                "\"", "&quot;");
    }
    
    /**
     * Adds text to the body of the currently open element (if any).  The text
     * is preprocessed to replace those characters that may not appear 
     * literally in xml character data with equivalent entity references.  If
     * a start tag is currently open then it is finished and the specified text
     * insrted after it.
     * 
     * @param  text the text string to add
     */
    public void addText(String text) {
        addLiteralText(escapeElementString(text));
    }
    
    /**
     * Replaces characters that must not appear literally in XML except as
     * markup delimiters with corresponding entity references, as described in
     * <a href="http://www.w3.org/TR/2004/REC-xml11-20040204/#syntax">section
     * 2.4 of the XML spec</a>.  The characters so treated are the ampersand
     * ({@code &amp;}), less-than sign ({@code &lt;}), and greater-than sign
     * ({@code &gt;}).
     * 
     * @param  cdata the data to be processed, which should be character data or
     *         an <em>unquoted</em> attribute value; any markup (including
     *         erstwhile entity references) in this string will be escaped as
     *         entity references
     *           
     * @return a string similar to {@code cdata} in which the characters that
     *         should not appear unescaped in XML character data have been
     *         replaced with corresponding XML entity references
     */
    private String escapeElementString(String cdata) {

        // Note: the '&' replacement must happen first

        return cdata.replaceAll("&", "&amp;").replaceAll(
                "<", "&lt;").replaceAll(">", "&gt;");
    }

    /**
     * Adds literal text to the output (i.e. without applying any character
     * escapes).  For standard element content {@link #addText(String)} is
     * more appropriate, but {@code addLiteralText} can be used to produce
     * document structure not otherwise supported by this class -- a DOCTYPE
     * declaration, for instance.  Like {@code addText()}, this method
     * causes any partially written start tag to be finished
     * 
     * @param  text the text to write to the output; it is written as-is
     */
    public void addLiteralText(String text) {
        if (inStartTag) {
            finishStartTag();
        }
        writer.print(text);
    }
    
    /**
     * Writes the closing angle bracket of the currently open start tag, and
     * adjusts internal state appropriately
     * 
     * @throws IllegalStateException if no start tag is open
     */
    private void finishStartTag() {
        if (!inStartTag) {
            throw new IllegalStateException("No open start tag");
        } else {
            writer.print('>');
            inStartTag = false;
        }
    }

    /**
     * Outputs a line break to the underlying stream, according to the
     * convention of the local system, and follows with a sequence of spaces
     * corresponding to the current indentation level 
     */
    public void addLineBreak() {
        currentContext.printNewLine(writer);
    }
    
    /**
     * Closes the innermost (most deeply nested) of the currently open elements.
     * If that element's start tag is still open then it is closed as an empty
     * element tag; otherwise an end tag is written.
     *  
     * @return the name of the element that was closed
     */
    public String closeElement() {
        try {
            String elementName = currentContext.getName();
            ElementContext oldContext = currentContext;
            
            currentContext = contextStack.remove(0);
            if (inStartTag) {
                writer.print("/>");
                inStartTag = false;
            } else {
                if (oldContext.isIndenting()) {
                    currentContext.printNewLine(writer);
                }
                writer.print("</");
                writer.print(elementName);
                writer.print(">");
            }
            
            return elementName;
        } catch (IndexOutOfBoundsException ioobe) {
            throw new IllegalStateException("No open element");
        }
    }

    /**
     * Closes the innermost open element having the specified name, and any
     * other open elements nested within it
     * 
     * @param  name the name of the element to close
     * 
     * @throws IllegalStateException if no such element is open 
     */
    public void closeElement(String name) {
        if (isElementOpen(name)) {
            for (; !name.equals(closeElement()); ) {
                // empty loop; the work happens in the condition above
            }
        } else {
            throw new IllegalStateException("element not open: " + name);
        }
    }
    
    /**
     * Determines whether any element of the specified name is currently open
     * (i.e. a start tag has been written for it, but no corresponding end tag)
     * 
     * @param  name the element name subject of the inquiry
     * @return {@code true} if an element of that name is open, {@code} false
     *         if not
     */
    private boolean isElementOpen(String name) {
        if (currentContext.getName().equals(name)) {
            return true;
        } else {
            for (ElementContext context : contextStack) {
                String elementName = context.getName();
                
                if (elementName == null) {
                    return false;
                } else if (context.getName().equals(name)) {
                    return true;
                }
            }
        
            /*
             * control should not reach this point because the deepest context
             * has a null name and therefore will be caught within the loop
             * above
             */
            return false;
        }
    }

    /**
     * Closes all open elements as if by successive invocations of
     * {@link #closeElement()}.  After this method is invoked, addition of
     * new elements will compromise the well-formedness of the output.
     */
    public void closeAllElements() {
        while (!contextStack.isEmpty()) {
            closeElement();
        }
        
        /*
         * Note: this will clear the context stack, but still leave the last
         * context in the currentContext variable.  That is exactly as desired.
         */
    }

    /**
     * Flushes the internal stream
     */
    public void flush() {
        writer.flush();
    }
    
    /**
     * Closes all open elements as if by successive invocations of
     * {@link #closeElement()}, and closes the underlying stream.  Additional
     * output cannot be created with this {@code XmlWriter} after this method
     * has been invoked on it.
     */
    public void close() {
        closeAllElements();
        writer.close();
    }
    
    /**
     * A simple class that helps manage indentation of nested elements being
     * output by an {@code XmlWriter}
     * 
     * @author  jobollin
     * @version 0.9.0
     */
    private static class ElementContext {
        private final String elementName;
        private final int baseIndentation;
        private int indentationIncrement;
        
        /**
         * Initializes an {@code ElementContext} with the specified parameters
         * 
         * @param  name the name of the element
         * @param  indentation the indentation level of the parent element
         * @param  increment the indentation increment for this element
         */
        public ElementContext(String name, int indentation, int increment) {
            elementName = name;
            baseIndentation = indentation;
            indentationIncrement = increment;
        }

        /**
         * Returns the element name specified for this context
         * 
         * @return the element name
         */
        public String getName() {
            return elementName;
        }
        
        /**
         * Sets the indentation increment for this context to a new value
         * 
         * @param increment the new indentation increment
         */
        public void setIncrement(int increment) {
            indentationIncrement = increment;
        }

        /**
         * Returns the indentation level of the text and elements written in
         * this element context
         * 
         * @return the non-negative number of characters by which to indent, or
         *         -1 if indentation is disabled in this context
         */
        private int getTotalIndent() {
            return (indentationIncrement < 0) 
                    ? -1 : (baseIndentation + indentationIncrement);
        }
        
        /**
         * Outputs a system-specific line terminator on the supplied stream,
         * and if indentation is enabled in this context then also outputs the
         * number of space (' ') characters required to indent the new line
         *   
         * @param  pw the {@code PrintWriter} to which the output should be
         *         directed
         */
        public void printNewLine(PrintWriter pw) {
            pw.println();
            if (isIndenting()) {
                for (int i = getTotalIndent(); i > 0; i--) {
                    pw.print(' ');
                }
            }
        }
        
        /**
         * Determines whether indentation is enabled in this context
         * 
         * @return {@code true} if indentation is enabled, {@code false} if not
         */
        public boolean isIndenting() {
            return (indentationIncrement >= 0);
        }
        
        /**
         * Produces a new {@code ElementContext} derived from another one,
         * suitable for representing a child element of the element represented
         * by the original context
         * 
         * @param  parent the ElementContext from which to derive
         * @param  name the name of the new context
         * @param  indentIncrement the indentation increment for the new context
         * 
         * @return an {@code ElementContext} configured appropriately for a
         *         child of the specified parent context
         */
        public static ElementContext deriveFrom(ElementContext parent,
                String name, int indentIncrement) {
            return new ElementContext(name, parent.getTotalIndent(),
                    indentIncrement);
        }
    }
}
