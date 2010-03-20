/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.exceptions;

/**
 *
 * @author Thijs
 */
public class StockPlayException extends Exception {

    public StockPlayException(Throwable cause) {
        super(cause);
    }

    public StockPlayException(String message, Throwable cause) {
        super(message, cause);
    }

    public StockPlayException(String message) {
        super(message);
    }

    public StockPlayException() {
    }

    
}
