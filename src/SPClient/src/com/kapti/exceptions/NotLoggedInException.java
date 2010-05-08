/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.exceptions;

/**
 *
 * @author Thijs
 */
public class NotLoggedInException extends ErrorException {

    public NotLoggedInException() {
    }

    public NotLoggedInException(String message) {
        super(message);
    }

    public NotLoggedInException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotLoggedInException(Throwable cause) {
        super(cause);
    }

    

}
