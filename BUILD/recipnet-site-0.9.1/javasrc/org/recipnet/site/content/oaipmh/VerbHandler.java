/*
 * Reciprocal Net Project
 *
 * VerbHandler.java
 *
 * 25-Oct-2005: jobollin extracted this class from OaiPmhResponder
 */

package org.recipnet.site.content.oaipmh;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import org.recipnet.common.Validator;
import org.recipnet.common.XmlWriter;
import org.recipnet.site.InconsistentDbException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.wrapper.CoreConnector;

/**
 * An abstract base implementation for classes whose instances process OAI-PMH
 * requests to produce those parts of the responses that are specific to
 * particular OAI-PMH verbs.  This class provides a common validation and
 * request-handling API, and a framework for request well-formedness
 * validation.
 * 
 * @author jobollin
 * @version 0.9.0
 */
public abstract class VerbHandler {
    
    /**
     * The verb handled by this handler
     */
    private final String verb;

    /**
     * A map from argument names supported by this handler to corresponding
     * {@code Validators} for their values 
     */
    private final Map<String, Validator> argumentValidators
            = new HashMap<String, Validator>();

    /**
     * A {@code CoreConnector} with which to communicate with the Reciprocal Net
     * core.
     */
    private final CoreConnector coreConnector;
    
    /**
     * Initializes a {@code VerbHandler} with the specified verb string and
     * {@code CoreConnector}
     * 
     * @param  verb a {@code String} identifying the OAI-PMH verb handled by
     *         this handler
     * @param  connector a {@code CoreConnector} with which to retrieve
     *         information from the Reciprocal Net core
     */
    public VerbHandler(String verb, CoreConnector connector) {
        this.verb = verb;
        this.coreConnector = connector;
    }

    /**
     * Returns the verb handled by this handler
     * 
     * @return the OAI-PMH verb handled by this handler, as a {@code String}
     */
    public String getVerb() {
        return verb;
    }
    
    /**
     * <p>
     * Tests the arguments to the specified OAI-PMH request to verify that they
     * individually and collectively satisfy the general OAI-PMH requirements
     * for the verb handled by this handler.  In particular, the validation
     * performed by this method is exactly that necessary to determine whether
     * or not a "badArgument" protocol error should be issued.
     * </p><p>
     * This version verifies that none of the arguments has multiple values and
     * that all are among the arguments supported by this handler.  Subclasses
     * may override this method to provide supplemental (or alternative)
     * validation rules, such as required arguments or inter-argument
     * validations.
     * </p>
     * 
     * @param  request the {@code PmhRequest} to validate
     * 
     * @throws PmhException if (and only if) the arguments are invalid; in this
     *         case the error type assigned to the exception is
     *         {@link PmhError#BAD_ARGUMENT}
     *         
     * @see #supportArgument(String, Validator)
     */
    @SuppressWarnings("unchecked")
    public void validateRequest(PmhRequest request) throws PmhException {
        Map<String, String[]> parameterMap =
                request.getRequest().getParameterMap();
        
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String[] paramValues = entry.getValue();
            String param = entry.getKey();
            
            if (!param.equals("verb")) {
                if (paramValues.length > 1) {
                    throw new PmhException(PmhError.BAD_ARGUMENT,
                            "multiple values for the '" + param
                            + "' parameter");
                } else if (!argumentValidators.containsKey(param)) {
                    throw new PmhException(PmhError.BAD_ARGUMENT,
                            "'" + param
                            + "' argument not supported for this OAI verb.");
                } else if (!argumentValidators.get(param).isValid(paramValues[0])) {
                    throw new PmhException(PmhError.BAD_ARGUMENT,
                            "'" + param
                            + "' argument has an invalid value.");
                }
            }
        }
    }
    
    /**
     * Processes the specified request to produce the XML element of a response
     * document that describes the result of acting according to the specified
     * verb and its arguments.  Implementations are expected to perform any
     * appropriate argument validation that falls outside the scope of the
     * {@link #validateRequest(PmhRequest)} method, and to throw an appropriate
     * {@code PmhException} if such validation fails.
     * 
     * @param  request a {@code PmhRequest} describing the request
     * @param  writer an {@code XmlWriter} to which the response should be
     *         directed
     * 
     * @throws RemoteException on RMI error.
     * @throws IOException if one occurs while creating the response
     * @throws InconsistentDbException if a database inconsistency was
     *         detected.
     * @throws OperationFailedException if the operation could not be
     *         completed because of a low-level error.
     * @throws PmhException if an OAI-PMH protocol error needs to be signaled.
     *         <strong>The handler should ensure that it does not write anything
     *         to the {@code XmlWriter} before it throws this
     *         exception.</strong>  Exceptions representing {@code BadArgument}
     *         errors should not be thrown; it is the responsability of the
     *         {@code validateRequest(PmhRequest)} method to catch these, and
     *         the responsability of the client to use that method to validate
     *         the request before handing it off to this method.  Also,
     *         exceptions representing {@code BadVerb} errors do not make sense
     *         in this context.
     */
    public abstract void handleRequest(PmhRequest request, XmlWriter writer)
            throws RemoteException, IOException, OperationFailedException,
            InconsistentDbException, PmhException;

    /**
     * Returns the {@code CoreConnector} configured for this handler's use
     * 
     * @return the {@code CoreConnector}
     */
    protected CoreConnector getCoreConnector() {
        return coreConnector;
    }

    /**
     * Instructs this handler that the specified string is an argument that is
     * (maybe) valid.
     * 
     * @param  argument the argument name that should be supported
     * @param  validator the {@code Validator} with which corresponding values
     *         should be tested
     */
    final protected void supportArgument(String argument, Validator validator) {
        argumentValidators.put(argument, validator);
    }
}