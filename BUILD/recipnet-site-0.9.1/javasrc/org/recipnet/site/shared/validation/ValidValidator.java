/*
 * Reciprocal Net Project
 *
 * ValidValidator.java
 *
 * Copyright (c) 2005 The Trustees of Indiana University.  All rights reserved.
 *
 * Oct 26, 2005: jobollin wrote first draft
 */
package org.recipnet.site.shared.validation;

import org.recipnet.common.Validator;

/**
 * A {@code Validator} that finds every value valid, including {@code null}
 *
 * @author jobollin
 * @version 0.9.0
 */
public class ValidValidator implements Validator {

    /**
     * Determines whether the specified object is valid, which it is
     *
     * @param  obj the object to validate
     * 
     * @return {@code true}
     */
    public boolean isValid(@SuppressWarnings("unused") Object obj) {
        return true;
    }

}
