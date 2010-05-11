/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.mobileclient.exceptions;

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


}
