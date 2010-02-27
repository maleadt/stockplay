/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.bo.exceptions;

/**
 *
 * @author Thijs
 */
public class FatalException extends StockPlayException {

    public FatalException(Throwable cause) {
        super(cause);
    }

    public FatalException(String message, Throwable cause) {
        super(message, cause);
    }

    public FatalException(String message) {
        super(message);
    }

    public FatalException() {
    }

}
