/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.administration;

/**
 *
 * @author Thijs
 */
public class ValueChange<T> {
    private T delta;
    private String reason;

    public ValueChange(T emptyValue) {
        this.delta =emptyValue;
    }

    

    public ValueChange(T delta, String reason) {
        this.delta = delta;
        this.reason = reason;
    }

    public T getDelta() {
        return delta;
    }

    public void setDelta(T delta) {
        this.delta = delta;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    

}
