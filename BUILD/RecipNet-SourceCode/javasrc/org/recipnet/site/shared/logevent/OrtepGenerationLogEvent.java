/*
 * Reciprocal Net Project
 *
 * OrtepGenerationLogEvent.java
 *
 * 23-Jan-2006: jobollin wrote first draft
 * 20-Feb-2006: jobollin removed an import of
 *              org.recipnet.site.content.servlet.OrtepInstructionGenerator
 */

package org.recipnet.site.shared.logevent;

import java.util.logging.Level;

/**
 * A {@code LogEvent} produced by the
 * {@link org.recipnet.site.content.servlet.OrtepInstructionGenerator} servlet.
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class OrtepGenerationLogEvent extends LogEvent {

    /**
     * The serialization version of this log event
     */
    private static final long serialVersionUID = 1L;

    /**
     * A smart enum of the various flavors of {@code OrtepGenerationLogEvent}s.
     * The enum instances incorporate their corresponding log message templates
     * along with knowledge of which parameters should be provided in the log
     * record.
     * 
     * @author jobollin
     * @version 1.0
     */
    public enum EventType {

        /**
         * A log event type describing failure of ORTEP instruction generation
         * because the requesting user was not authorized to see the sample
         */
        UNAUTHORIZED_REQUEST(Level.INFO,
                "Unauthorized attempt to generate ORTEP instructions for "
                        + "sample {0} by user {1}.") {

            /**
             * {@inheritDoc}
             */
            @Override
            Object[] chooseParameters(@SuppressWarnings("unused")
            String crtName, @SuppressWarnings("unused")
            String sdtName, @SuppressWarnings("unused")
            String cifName, Integer sampleId, @SuppressWarnings("unused")
            Integer sampleHistoryId, Integer userId) {
                return new Object[] { sampleId, userId, null };
            }
        },

        /**
         * A log event type describing failure of ORTEP instruction generation
         * because of an I/O error while reading the specified CRT file
         */
        CRT_IO_EXCEPTION(Level.INFO,
                "I/O error while reading CRT file {0} for sample {1}, "
                        + "sample history {2}, by user {3}.") {

            /**
             * {@inheritDoc}
             */
            @Override
            Object[] chooseParameters(String crtName,
                    @SuppressWarnings( { "unused" })
                    String sdtName, @SuppressWarnings("unused")
                    String cifName, Integer sampleId, Integer sampleHistoryId,
                    Integer userId) {
                return new Object[] { crtName, sampleId, sampleHistoryId,
                        userId, null };
            }
        },

        /**
         * A log event type describing failure of ORTEP instruction generation
         * because the specified CRT file could not be parsed
         */
        CRT_PARSE_EXCEPTION(Level.INFO,
                "CRT parsing failed on CRT file {0} for sample {1}, "
                        + "sample history {2}, on behalf of user {3}.") {

            /**
             * {@inheritDoc}
             */
            @Override
            Object[] chooseParameters(String crtName,
                    @SuppressWarnings("unused")
                    String sdtName, @SuppressWarnings("unused")
                    String cifName, Integer sampleId, Integer sampleHistoryId,
                    Integer userId) {
                return new Object[] { crtName, sampleId, sampleHistoryId,
                        userId, null };
            }
        },

        /**
         * A log event type describing failure of ORTEP instruction generation
         * because of an I/O error while reading the specified CRT could not be
         * accessed
         */
        CRT_RESOURCE_EXCEPTION(Level.INFO,
                "CRT file {0} of sample {1}, sample history {2}, could not be "
                        + "accessed on behalf of user {3}.") {

            /**
             * {@inheritDoc}
             */
            @Override
            Object[] chooseParameters(String crtName,
                    @SuppressWarnings("unused")
                    String sdtName, @SuppressWarnings("unused")
                    String cifName, Integer sampleId, Integer sampleHistoryId,
                    Integer userId) {
                return new Object[] { crtName, sampleId, sampleHistoryId,
                        userId, null };
            }
        },

        /**
         * A log event type describing failure of ORTEP instruction generation
         * because of an I/O error while reading the specified CIF
         */
        CIF_IO_EXCEPTION(
                Level.INFO,
                "I/O error while reading CIF {0} of sample {1}, sample history "
                        + "{2}, for the purpose of creating ORTEP instructions "
                        + "on behalf of user {3}.") {

            /**
             * {@inheritDoc}
             */
            @Override
            Object[] chooseParameters(@SuppressWarnings("unused")
            String crtName, @SuppressWarnings("unused")
            String sdtName, String cifName, Integer sampleId,
                    Integer sampleHistoryId, Integer userId) {
                return new Object[] { cifName, sampleId, sampleHistoryId,
                        userId, null };
            }
        },

        /**
         * A log event type describing failure of ORTEP instruction generation
         * because the specified CIF could not be parsed
         */
        CIF_PARSE_EXCEPTION(
                Level.INFO,
                "Could not parse CIF {0} of sample {1}, sample history "
                        + "{2}, for the purpose of creating ORTEP instructions "
                        + "on behalf of user {3}.") {

            /**
             * {@inheritDoc}
             */
            @Override
            Object[] chooseParameters(@SuppressWarnings("unused")
            String crtName, @SuppressWarnings("unused")
            String sdtName, String cifName, Integer sampleId,
                    Integer sampleHistoryId, Integer userId) {
                return new Object[] { cifName, sampleId, sampleHistoryId,
                        userId, null };
            }
        },

        /**
         * A log event type describing failure of ORTEP instruction generation
         * because the specified CIF contained no data blocks
         */
        CIF_EMPTY(
                Level.INFO,
                "CIF {0} of sample {1}, sample history {2}, contains no data "
                        + "blocks, and could not be used to create ORTEP "
                        + "instructions on behalf of user {3}.") {

            /**
             * {@inheritDoc}
             */
            @Override
            Object[] chooseParameters(@SuppressWarnings("unused")
            String crtName, @SuppressWarnings("unused")
            String sdtName, String cifName, Integer sampleId,
                    Integer sampleHistoryId, Integer userId) {
                return new Object[] { cifName, sampleId, sampleHistoryId,
                        userId, null };
            }
        },

        /**
         * A log event type describing failure of ORTEP instruction generation
         * because the specified CIF could not be accessed
         */
        CIF_RESOURCE_EXCEPTION(
                Level.INFO,
                "CIF {0} of sample {1}, sample history {2}, could not be "
                        + "accessed for the purpose of creating ORTEP "
                        + "instructions on behalf of user {3}.") {

            /**
             * {@inheritDoc}
             */
            @Override
            Object[] chooseParameters(@SuppressWarnings("unused")
            String crtName, @SuppressWarnings("unused")
            String sdtName, String cifName, Integer sampleId,
                    Integer sampleHistoryId, Integer userId) {
                return new Object[] { cifName, sampleId, sampleHistoryId,
                        userId, null };
            }
        },

        /**
         * A log event type describing failure of ORTEP instruction generation
         * because of an I/O error while reading the specified SDT file
         */
        SDT_IO_EXCEPTION(
                Level.INFO,
                "An I/O error occurred while reading SDT file {0} of sample "
                        + "{1}, sample history {2}, for the purpose of "
                        + "creating ORTEP instructions on behalf of user {3}."){

            /**
             * {@inheritDoc}
             */
            @Override
            Object[] chooseParameters(@SuppressWarnings("unused")
            String crtName, String sdtName, @SuppressWarnings("unused")
            String cifName, Integer sampleId, Integer sampleHistoryId,
                    Integer userId) {
                return new Object[] { sdtName, sampleId, sampleHistoryId,
                        userId, null };
            }
        },

        /**
         * A log event type describing failure of ORTEP instruction generation
         * because the specified SDT file could not be parsed
         */
        SDT_PARSE_EXCEPTION(
                Level.INFO,
                "SDT file {0} of sample {1}, sample history {2}, could not be "
                        + "parsed to create ORTEP instructions on behalf of "
                        + "user {3}.") {

            /**
             * {@inheritDoc}
             */
            @Override
            Object[] chooseParameters(@SuppressWarnings("unused")
            String crtName, String sdtName, @SuppressWarnings("unused")
            String cifName, Integer sampleId, Integer sampleHistoryId,
                    Integer userId) {
                return new Object[] { sdtName, sampleId, sampleHistoryId,
                        userId, null };
            }
        },

        /**
         * A log event type describing failure of ORTEP instruction generation
         * because specified CIF file could not be accessed
         */
        SDT_RESOURCE_EXCEPTION(
                Level.INFO,
                "SDT file {0} of sample {1}, sample history {2}, could not be "
                        + "accessed to create ORTEP instructions on behalf of "
                        + "user {3}.") {

            /**
             * {@inheritDoc}
             */
            @Override
            Object[] chooseParameters(@SuppressWarnings("unused")
            String crtName, String sdtName, @SuppressWarnings("unused")
            String cifName, Integer sampleId, Integer sampleHistoryId,
                    Integer userId) {
                return new Object[] { sdtName, sampleId, sampleHistoryId,
                        userId, null };
            }
        };

        /**
         * The severity level at which events of this type should be logged
         */
        private final Level level;

        /**
         * The log message template for events of this type
         */
        private final String messageTemplate;

        /**
         * Initializes an {@code EventType} with the specified parameters
         * 
         * @param level a {@code Level} representing the severity level of
         *        events of this type
         * @param messageTemplate the log message template {@code String} for
         *        events of this type
         */
        private EventType(Level level, String messageTemplate) {
            this.level = level;
            this.messageTemplate = messageTemplate;
        }

        /**
         * Retrieves the severity level of log events of this type
         * 
         * @return a {@code Level} representing the severity of log events of
         *         this type
         */
        Level getLevel() {
            return level;
        }

        /**
         * Retrieves the log message template for events of this type
         * 
         * @return a {@code String} containing the message template for log
         *         events of this type
         */
        String getMessageTemplate() {
            return messageTemplate;
        }

        /**
         * Creates and returns a parameter array appropriate for the log message
         * template provided by this event type, choosing values from among
         * those provided
         * 
         * @param crtName the name of the CRT file for which ORTEP instruction
         *        generation was requested
         * @param sdtName the name of the SDT file, if any, from which ORTEP
         *        instruction generation was requested
         * @param cifName the name of the CIF, if any, from which ORTEP
         *        instruction generation was requested
         * @param sampleId the ID of the sample for which ORTEP instruction
         *        generation was requested
         * @param sampleHistoryId the history ID for which ORTEP instruction
         *        generation was requested
         * @param userId the ID of the user who requested ORTEP instruction
         *        generation
         *        
         * @return an {@code Object[]} containing the log message parameter,
         *         plus a final {@code null} element
         */
        abstract Object[] chooseParameters(String crtName, String sdtName,
                String cifName, Integer sampleId, Integer sampleHistoryId,
                Integer userId);
    }

    /**
     * Initializes a {@code OrtepGenerationLogEvent} with the specified
     * parameters; the event type determines which of the parameters are
     * included in the generated log record
     * 
     * @param type the {@code EventType} representing the type of this log event
     * @param crtName the name of the CRT file for which ORTEP instruction
     *        generation was requested
     * @param sdtName the name of the SDT file, if any, from which ORTEP
     *        instruction generation was requested
     * @param cifName the name of the CIF, if any, from which ORTEP instruction
     *        generation was requested
     * @param sampleId the ID of the sample for which ORTEP instruction
     *        generation was requested
     * @param sampleHistoryId the history ID for which ORTEP instruction
     *        generation was requested
     * @param userId the ID of the user who requested ORTEP instruction
     *        generation
     * @param exception the {@code Throwable}, if any, that caused the creation
     *        of this log event
     */
    public OrtepGenerationLogEvent(EventType type, String crtName,
            String sdtName, String cifName, int sampleId, int sampleHistoryId,
            int userId, Throwable exception) {
        createLogRecord(type.getLevel(), type.getMessageTemplate(),
                type.chooseParameters(crtName, sdtName, cifName, sampleId,
                        sampleHistoryId, userId), exception);
    }
}
