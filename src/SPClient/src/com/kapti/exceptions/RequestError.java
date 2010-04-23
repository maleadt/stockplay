/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.exceptions;

/**
 *
 * @author Thijs
 */
public class RequestError extends ErrorException {

    public RequestError() {
    }

    public RequestError(String message) {
        super(message);
    }

    public RequestError(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestError(Throwable cause) {
        super(cause);
    }


}
