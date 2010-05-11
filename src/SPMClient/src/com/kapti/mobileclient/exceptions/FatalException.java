/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.mobileclient.exceptions;

/**
 *
 * @author Thijs
 */
public class FatalException extends StockPlayException {


    public FatalException(String message) {
        super(message);
    }

    public FatalException() {
    }

}
