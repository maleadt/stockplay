/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.bo.exceptions;

/**
 *
 * @author Thijs
 */
public class ErrorException extends StockPlayException{

    public ErrorException(Throwable cause) {
        super(cause);
    }

    public ErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ErrorException(String message) {
        super(message);
    }

    public ErrorException() {
    }


    
}
