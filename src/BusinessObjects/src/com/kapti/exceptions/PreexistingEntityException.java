/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.exceptions;

/**
 *
 * @author Thijs
 */
public class PreexistingEntityException extends ErrorException {

    public PreexistingEntityException() {
    }

    public PreexistingEntityException(String message) {
        super(message);
    }

    public PreexistingEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public PreexistingEntityException(Throwable cause) {
        super(cause);
    }
    
}
