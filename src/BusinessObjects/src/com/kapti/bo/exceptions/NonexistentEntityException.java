/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.bo.exceptions;

/**
 *
 * @author Thijs
 */
public class NonexistentEntityException extends ErrorException {

    public NonexistentEntityException() {
    }

    public NonexistentEntityException(String message) {
        super(message);
    }

    public NonexistentEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonexistentEntityException(Throwable cause) {
        super(cause);
    }

    
}
