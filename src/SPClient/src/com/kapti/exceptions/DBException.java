/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.exceptions;

/**
 *
 * @author Thijs
 */
public class DBException extends FatalException{

    public DBException() {
        super();
    }

    public DBException(String message) {
        super(message);
    }

    public DBException(String message, Throwable cause) {
        super(message, cause);
    }

    public DBException(Throwable cause) {
        super(cause);
    }


}
