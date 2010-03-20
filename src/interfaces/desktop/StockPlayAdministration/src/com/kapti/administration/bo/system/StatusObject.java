/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.administration.bo.system;

/**
 *
 * @author Thijs
 */
public class StatusObject {

    
    public enum Status {
      UNKNOWN,
      STOPPED,
      STARTED,
      STARTING,
      STOPPING,
      ERROR
    };

    protected String name;

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of name
     *
     * @param name new value of name
     */
    public void setName(String name) {
        this.name = name;
    }


    protected Status status;

    /**
     * Get the value of status
     *
     * @return the value of status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Set the value of status
     *
     * @param status new value of status
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    public StatusObject(String name) {
        this.name = name;
    }


}
