/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.mobileclient.exceptions;

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

    
}