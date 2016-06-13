/*
 * Reciprocal Net project
 * 
 * MsgpakUtil.java
 *
 * 07-Dec-2005: ekoperda wrote first draft
 * 12-May-2006: jobollin reformatted the source
 * 01-Jun-2006: jobollin accommodated exception-related changes to SoapUtil
 */

package org.recipnet.site.core.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.recipnet.site.InvalidDataException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.MessageDecodingException;
import org.recipnet.site.core.ResourceException;
import org.recipnet.site.core.msg.InterSiteMessage;
import org.recipnet.site.core.msg.SiteGrantISM;
import org.recipnet.site.shared.SoapUtil;
import org.recipnet.site.shared.db.SiteInfo;
import org.xml.sax.SAXException;

/**
 * <p>
 * The "msgpak" file format is a compact way of bundling and shipping multiple
 * {@code InterSiteMessage} objects between sites. The files customarily are
 * named with a suffix of {@code .msgpak}. Their data is compressed. The
 * methods of this class provide convenient means for reading from and writing
 * to such files.
 * </p><p>
 * The actual file format described by the current implementation is
 * backwards-compatible to release 0.5.2 of the Reciprocal Net site software.
 * The file is structured as a {@code .zip} file normally would be, using the
 * VM's facilities for manipulating the {@code .zip} file format. Zip files
 * contain one or more entries, and for msgpak files the entry name must
 * correlate with the class name of the kind of object whose data is expressed
 * within the entry. For example, a zip entry named {@code SiteGrantISM} would
 * contain data for reconstructing objects of class
 * {@code org.recipnet.site.core.msg.SiteGrantISM}. The various kinds of
 * entries are described below.
 * </p><p>
 * An entry named {@code InterSiteMessage} contains data suitable for
 * reconstructing objects of the {@code InterSiteMessage} class and its
 * subclasses; no particular subclass of {@code InterSiteMessage} is specified.
 * This entry is a required component of any msgpak file. The entry's data is an
 * XML document in UTF-8 encoding. The document's root element is
 * {@code messages} and no attributes on it are defined. Nested beneath the root
 * element are zero or more {@code message} elements, each of which is a
 * standard XML representation of an inter-site message as defined by the
 * {@code InterSiteMessage} class.
 * </p><p>
 * An entry named {@code SiteGrantISM} contains data suitable for reconstructing
 * an object of the {@code SiteGrantISM} class. This entry is an optional
 * component of any msgpak file. The entry's data is an XML document in UTF-8
 * encoding. The document's root element is {@code message} and is the standard
 * XML representation of a site grant message as defined by the
 * {@code SiteGrantISM} class. This sort of entry always describes exactly one
 * {@code SiteGrantISM} object. Special support for this particular class of
 * message is appropriate because a site being bootstrapped requires access to
 * its site grant before it can process any other ISMs.
 * </p>
 */
public abstract class MsgpakUtil {
    
    /**
     * <p>
     * Opens a specified msgpak file located on the filesystem, reads the entry
     * named {@code InterSiteMessage}, and returns an array of zero or more
     * strings derived from the entry's contents. Each returned string is the
     * XML representation of an {@code InterSiteMessage} object.
     * </p><p>
     * Callers which do not intend to verify digital signatures or perform other
     * validation on the messages may find
     * {@link #readAndDecodeAllMessages(File)} to be a more convenient
     * alternative to this function.
     * </p>
     * 
     * @return an array of zero or more {@code String}s, where each contains
     *         the XML representation of one {@code InterSiteMessage} object. A
     *         caller might then decode the XML by invoking
     *         {@link InterSiteMessage#fromXml(String)} or one of its relatives.
     * @param file identifies the msgpak file on the filesystem.
     * @throws InvalidDataException with a reason code of
     *         {@code MISSING_MSGPAK_ZIPENTRY} if the msgpak file does not
     *         contain an {@code InterSiteMessage} entry. This necessarily would
     *         indicate that {@code file} was malformed and/or invalid.
     * @throws ResourceException with a nested {@code IOException} and an
     *         {@code identifier} of type {@code File} on low-level I/O error.
     */
    public static String[] readAllMessages(File file)
            throws InvalidDataException, ResourceException {
        String xmlDoc = readZipFileEntry(file, "InterSiteMessage");
        
        return SoapUtil.extractFragmentsFromXmlDocument(xmlDoc, "message");
    }

    /**
     * Similar to {@link #readAllMessages(File)}, except the XML representation
     * of each message is decoded. Thus, this function returns an array of
     * {@code InterSiteMessage}. No attempt is made to verify any digital
     * signatures that may be present on the messages, or to otherwise validate
     * the ISMs.
     * 
     * @return an array of zero or more {@code InterSiteMessage} objects.
     * @param file identifies the msgpak file on the filesystem.
     * @throws InvalidDataException with a reason code of
     *         {@code MISSING_MSGPAK_ZIPENTRY} if the msgpak file does not
     *         contain an {@code InterSiteMessage} entry. This necessarily would
     *         indicate that {@code file} is malformed and/or invalid.
     * @throws MessageDecodingException with a nested {@code SAXException} if a
     *         message's XML representation could not be parsed.
     * @throws OperationFailedException if a low-level error occurred while
     *         attempting to initialize the XML transformation engine.
     * @throws ResourceException with a nested {@code IOException} and an
     *         {@code identifier} of type {@code File} on low-level I/O error.
     */
    public static InterSiteMessage[] readAndDecodeAllMessages(File file)
            throws InvalidDataException, MessageDecodingException,
            OperationFailedException, ResourceException {
        String[] ismsAsXml = readAllMessages(file);
        InterSiteMessage[] isms = new InterSiteMessage[ismsAsXml.length];

        for (int i = 0; i < ismsAsXml.length; i++) {
            try {
                isms[i] = InterSiteMessage.fromXml(ismsAsXml[i]);
            } catch (SAXException ex) {
                throw new MessageDecodingException(SiteInfo.INVALID_SITE_ID,
                        null, ismsAsXml[i], ex);
            }
        }

        return isms;
    }

    /**
     * <p>
     * Opens a specified msgpak file located on the filesystem, reads the entry
     * named {@code SiteGrantISM} and returns a string derived from the entry's
     * contents. The string should be the XML representation of a single
     * {@code SiteGrantISM} object. A typical caller might then decode the XML
     * string by invoking {@link InterSiteMessage#fromXml(String)}. Because not
     * all msgpak files necessarily contain a {@code SiteGrantISM} entry, it is
     * possible that this call may fail.
     * </p><p>
     * Callers which do not intend to verify the digital signatures or perform
     * other validation on the message may find {@code readAndDecodeSiteGrant()}
     * to be a more convenient alternative to this function.
     * </p>
     * 
     * @param file identifies the msgpak file on the filesystem.
     * @return an {@code String} containing the XML representation of a
     *         {@code SiteGrantISM} object. A caller might then decode the XML
     *         by invoking {@code InterSiteMessage.fromXml()} or one of its
     *         relatives.
     * @throws InvalidDataException with a reason code of
     *         {@code MISSING_MSGPAK_ZIPENTRY} if the msgpak file does not
     *         contain a {@code SiteGrantISM} entry. This does not necessarily
     *         indicate that {@code file} is malformed, but simply that it does
     *         not contain the desired information.
     * @throws ResourceException with a nested {@code IOException} and an
     *         {@code identifier} of type {@code File} on low-level I/O error.
     */
    public static String readSiteGrant(File file) throws InvalidDataException,
            ResourceException {
        return readZipFileEntry(file, "SiteGrantISM");
    }

    /**
     * Similar to {@link #readSiteGrant(File)}, except the XML representation
     * of the message is decoded. Thus, this function returns a
     * {@code SiteGrantISM} object. No attempt is made to verify any digital
     * signatures that may be present on the message, or to otherwise validate
     * the ISM.
     * 
     * @return a {@code SiteGrantISM} object.
     * @param file identifies the msgpak file on the filesystem.
     * @throws InvalidDataException with a reason code of
     *         {@code MISSING_MSGPAK_ZIPENTRY} if the msgpak file does not
     *         contain a {@code SiteGrantISM} entry. This does not necessarily
     *         indicate that {@code file} is malfored, but simply that it does
     *         not contain the desired information.
     * @throws MessageDecodingException with a nested {@code SAXException} if
     *         the message's XML representation could not be parsed.
     * @throws OperationFailedException if a low-level error occurred while
     *         attempting to initialize the XML transformation engine.
     * @throws ResourceException with a nested {@code IOException} and an
     *         {@code identifier} of type {@code File} on low-level I/O error.
     */
    public static SiteGrantISM readAndDecodeSiteGrant(File file)
            throws InvalidDataException, MessageDecodingException,
            OperationFailedException, ResourceException {
        String ismAsXml = readSiteGrant(file);

        try {
            return (SiteGrantISM) InterSiteMessage.fromXml(ismAsXml);
        } catch (SAXException ex) {
            throw new MessageDecodingException(SiteInfo.INVALID_SITE_ID, null,
                    ismAsXml, ex);
        }
    }

    /**
     * Creates a msgpak file at a specified location on the filesystem and
     * populates it with caller-supplied inter-site messages. The msgpak file
     * generated contains an {@code InterSiteMessage} entry and optionally a
     * {@code SiteGrantISM} entry.
     * 
     * @param targetFile identifies the location on the filesystem at which the
     *        new msgpak file is to be created. If this file already exists, it
     *        will be overwritten.
     * @param ismsAsXml an array of zero or more strings, each of which contains
     *        the XML representation of an inter-site message. These are written
     *        to the msgpak file verbatim.
     * @param siteGrantIsmAsXml optionally, the XML representation of a
     *        {@code SiteGrantISM} message. If this argument is non-null, the
     *        generated msgpak file will contain a {@code SiteGrantISM} entry
     *        and this argument's string will be written to it verbatim. If this
     *        argument is null then no such entry is included in the generated
     *        mskpak file.
     * @throws ResourceException with a nested {@code IOException} and an
     *         {@code identifier} of type {@code File} on low-level I/O error.
     */
    public static void createAndPopulate(File targetFile, String ismsAsXml[],
            String siteGrantIsmAsXml) throws ResourceException {
        try {
            /*
             * Create the output stream. It's a ZIP file with one or two
             * entries.
             */
            ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(
                    new FileOutputStream(targetFile)));

            /*
             * Put the big XML document containing potentially many messages
             * into the ZIP stream as one entry.
             */
            zos.putNextEntry(new ZipEntry("InterSiteMessage"));
            
            Writer writer = new OutputStreamWriter(zos, "UTF-8");
            
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.write("<messages>");
            for (String ismAsXml : ismsAsXml) {
                writer.write(
                        SoapUtil.dropXmlDocumentHeader(ismAsXml, "message"));
            }
            writer.write("</messages>");
            writer.flush();
            
            // abandon the Writer without closing it
            
            zos.closeEntry();

            /*
             * If the caller provided an XML representation of a SiteGrantISM
             * object, add that to the ZIP file in a separate entry so that this
             * file's recipient will have easy access to the message.
             */
            if (siteGrantIsmAsXml != null) {
                zos.putNextEntry(new ZipEntry("SiteGrantISM"));
                writer = new OutputStreamWriter(zos, "UTF-8");
                writer.write(siteGrantIsmAsXml);
                writer.flush();
                
                // abandon the Writer without closing it
                
                zos.closeEntry();
            }

            zos.close();
        } catch (IOException ex) {
            throw new ResourceException(targetFile, ex);
        }
    }

    /**
     * Internal helper function that opens a specified msgpak file on the
     * filesystem, navigates to the entry with the specified name, reads that
     * entry in its entirety, and returns its data as a string. The entry's data
     * is assumed to be in UTF-8 encoding.
     * 
     * @param file identifies the msgpak file on the filesystem.
     * @param entryName the name of the zip entry that is to be read.
     * @return a String containing the contents of the specified msgpak entry
     * @throws InvalidDataException with a reason code of
     *         {@code MISSING_MSGPAK_ZIPENTRY} if an entry with the specified
     *         name could not be found within the msgpak file.
     * @throws ResourceException with a nested {@code IOException} and an
     *         {@code identifier} of type {@code File} on low-level I/O error.
     */
    private static String readZipFileEntry(File file, String entryName)
            throws InvalidDataException, ResourceException {
        try {
            ZipFile zipFile = new ZipFile(file);

            try {
                ZipEntry ze = zipFile.getEntry(entryName);
                Reader reader;

                if (ze == null) {
                    throw new InvalidDataException(file,
                            InvalidDataException.MISSING_MSGPAK_ZIPENTRY);
                }
                reader = new InputStreamReader(zipFile.getInputStream(ze),
                        "UTF-8");
                try {
                    StringBuilder sb = new StringBuilder();
                    char[] buffer = new char[4096];
                    
                    for (;;) {
                        int charsRead = reader.read(buffer, 0, buffer.length);
                        
                        if (charsRead < 0) {
                            break;
                        } else {
                            sb.append(buffer, 0, charsRead);
                        }
                    }
                    
                    return sb.toString();
                } finally {
                    reader.close();
                }
            } finally {
                zipFile.close();
            }
        } catch (IOException ex) {
            throw new ResourceException(file, ex);
        }
    }
}
