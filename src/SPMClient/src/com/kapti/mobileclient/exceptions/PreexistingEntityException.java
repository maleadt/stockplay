/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.mobileclient.exceptions;

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


}
